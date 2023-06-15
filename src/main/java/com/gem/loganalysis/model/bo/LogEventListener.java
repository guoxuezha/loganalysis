package com.gem.loganalysis.model.bo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.gem.gemada.dal.db.pools.DAO;
import com.gem.loganalysis.config.BusinessConfigInfo;
import com.gem.loganalysis.model.BaseConstant;
import com.gem.loganalysis.model.dto.block.BlockExecuteDTO;
import com.gem.loganalysis.util.MinioUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.gem.loganalysis.util.BlockFileUtil.getBlockFileRootPath;
import static com.gem.loganalysis.util.BlockFileUtil.getMultipartFile;

/**
 * 事件监听
 *
 * @author GuoChao
 * @version 1.0
 * @date 2023/5/11 12:00
 */
@Component
@Slf4j
public class LogEventListener {

    @Resource
    private LogAnalysisRulePool logAnalysisRulePool;

    @Resource
    private BusinessConfigInfo businessConfigInfo;

    @Resource
    private MinioUtil minioUtil;

    @Resource
    private DAO dao;

    /**
     * 每分钟扫描一次缓存是否需要持久化
     */
    @Scheduled(cron = "0 0/1 * * * ? ")
    public void cacheFlushScan() {
        if (CollUtil.isNotEmpty(logAnalysisRulePool.getLogAnalysisRuleBoMap())) {
            for (LogAnalysisRuleBo analysisRule : logAnalysisRulePool.getLogAnalysisRuleBoMap().values()) {
                if (analysisRule.isEnable()) {
                    analysisRule.flushCache();
                }
            }
        }
    }

    /**
     * 每30秒扫描一次是否有事件结束
     */
    @Scheduled(cron = "0/30 * * * * ? ")
    public void logEventEndScan() {
        if (CollUtil.isNotEmpty(logAnalysisRulePool.getLogAnalysisRuleBoMap())) {
            for (LogAnalysisRuleBo analysisRule : logAnalysisRulePool.getLogAnalysisRuleBoMap().values()) {
                if (analysisRule.isEnable()) {
                    analysisRule.scanAndStopEvent();
                }
            }
        }
    }

    /**
     * 查询处置状态为0的风险并尝试执行封堵
     */
//    @Scheduled(cron = "0 0/1 * * * ? ")
    public void riskHandlerScan() {
        String querySql = "SELECT A.ASSET_ID, A.RISK_LEVEL, B.EVENT_ID, B.EVENT_TYPE, B.SOURCE_IP FROM SOP_ASSET_RISK A " +
                "LEFT JOIN SOP_ASSET_EVENT B ON A.REF_EVENT_ID = B.EVENT_ID WHERE A.HANDLE_STATUS = 0";
        ArrayList<HashMap<String, String>> dataSet = dao.getDataSet(BaseConstant.DEFAULT_POOL_NAME, querySql, 0, 0);
        if (CollUtil.isNotEmpty(dataSet)) {
            BlockRuleServer blockRuleServer = BlockRuleServer.getInstance();
            for (HashMap<String, String> map : dataSet) {
                blockRuleServer.executeBlock(map.get("ASSET_ID"), map.get("EVENT_ID"), map.get("SOURCE_IP"),
                        Integer.valueOf(map.get("RISK_LEVEL")), map.get("EVENT_TYPE"));
            }
        }
    }

    /**
     * 解封扫描
     */
//    @Scheduled(cron = "0 0/1 * * * ? ")
    public void deBlockScan() {
        // 查询状态为"临时封堵中"的封堵记录
        String querySql = "SELECT A.BLOCK_RECORD_ID, A.BLOCK_IP, A.BLOCK_END_TIME, A.ASSET_ID, " +
                "B.IP_ADDRESS, B.NM_PORT, B.NM_ACCOUNT, B.NM_PASSWORD " +
                "FROM SOP_BLOCK_RECORD A " +
                "LEFT JOIN SOP_ASSET B ON A.ASSET_ID = B.ASSET_ID " +
                "LEFT JOIN SOP_BLOCK_COMMAND C ON B.ASSET_ID = C.ASSET_ID " +
                "WHERE BLOCK_STATE = 1 AND BLOCK_TYPE = 0";
        ArrayList<HashMap<String, String>> dataSet = dao.getDataSet(BaseConstant.DEFAULT_POOL_NAME, querySql, 0, 0);
        if (CollUtil.isNotEmpty(dataSet)) {
            for (HashMap<String, String> map : dataSet) {
                // 判断是否抵达封堵结束时间
                String blockEndTime = map.get("BLOCK_END_TIME");
                if (StrUtil.isNotEmpty(blockEndTime)) {
                    DateTime parse = DateUtil.parse(blockEndTime, DatePattern.PURE_DATETIME_PATTERN);
                    if (new Date().after(parse)) {
                        // 若抵达则执行解封
                        BlockExecutor.deBlock(new BlockExecuteDTO(map, map.get("BLOCK_IP")));
                        // 修改封堵状态记录
                        String updateSql = String.format("UPDATE SOP_BLOCK_RECORD SET BLOCK_STATE = 2 WHERE BLOCK_RECORD_ID = %s LIMIT 1", map.get("BLOCK_RECORD_ID"));
                        dao.execCommand(BaseConstant.DEFAULT_POOL_NAME, updateSql);
                    }
                }
            }
        }
    }


    /**
     * 每天0点扫描是否有过早的原始日志文件需要上传到Minio
     */
    @Scheduled(cron = "0 0 0 * * ? ")
    public void LogBlockFileDeleteScan() {
        // 1.扫描指定路径下的文件
        List<File> files = FileUtil.loopFiles(getBlockFileRootPath());
        DateTime earliestDay = DateUtil.offsetDay(DateUtil.parseDate(DateUtil.today()), -businessConfigInfo.getLogBlockFileRetentionDays());
        for (File file : files) {
            if (file.getName().endsWith(".DAT") || file.getName().endsWith(".IDX")) {
                int endIndex = file.getName().lastIndexOf(".");
                String fileDate = file.getName().substring(endIndex - 8, endIndex);
                // 若文件日期早于指定的最早天数,则执行Minio上传后删除本地文件
                if (DateUtil.parse(fileDate, DatePattern.PURE_DATE_PATTERN).before(earliestDay)) {
                    String upload = minioUtil.upload(getMultipartFile(file));
                    if (upload != null) {
                        boolean del = FileUtil.del(file);
                        if (!del) {
                            log.warn("本地日志文件: {} 删除失败!", file.getName());
                        }
                    }
                }
            }
        }
    }

}

