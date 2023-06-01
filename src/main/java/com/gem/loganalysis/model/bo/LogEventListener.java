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
import com.gem.loganalysis.util.MinioUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
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
                    // analysisRule.scanAndStartEvent();
                }
            }
        }
    }

    /**
     * 扫描是否有事件可自动处理
     */
    @Scheduled(cron = "0 0/5 * * * ? ")
    public void eventHandlerScan() {
        // 1.查询未处理的事件
        String querySql = "SELECT A.EVENT_ID, A.ASSET_ID, A.EVENT_ORIGIN, A.ORIGIN_ID, A.EVENT_TYPE, A.EVENT_CLASS, A.SOURCE_IP, " +
                "B.BLOCK_RULE_ID, C.BLOCK_RULE_DESC, C.BLOCK_TYPE, C.BLOCK_DURATION, C.WHITE_LIST_ENABLE, C.BLACK_LIST_ENABLE " +
                "FROM SOP_ASSET_EVENT A " +
                "LEFT JOIN SOP_EVENT_TYPE B ON A.EVENT_TYPE = B.EVENT_TYPE AND A.EVENT_CLASS = B.EVENT_CLASS " +
                "LEFT JOIN SOP_BLOCK_RULE C ON B.BLOCK_RULE_ID = C.BLOCK_RULE_ID " +
                "WHERE A.HANDLE_STATUS = 0";
        ArrayList<HashMap<String, String>> dataSet = dao.getDataSet(BaseConstant.DEFAULT_POOL_NAME, querySql, 0, 0);
        if (CollUtil.isNotEmpty(dataSet)) {
            for (HashMap<String, String> map : dataSet) {
                // 如果事件类型关联的封堵规则为空,则新增一条闲置记录,并修改事件状态为"忽略"
                int handleStatus;
                if (StrUtil.isEmpty(map.get("BLOCK_RULE_ID"))) {
                    dao.execCommand(BaseConstant.DEFAULT_POOL_NAME,
                            String.format("INSERT IGNORE INTO SOP_EVENT_TYPE(EVENT_TYPE, EVENT_CLASS, BLOCK_RULE_ID) VALUE('%s','%s','%s')",
                                    map.get("EVENT_TYPE"), map.get("EVENT_CLASS"), ""));
                    handleStatus = 3;
                } else {
                    // 否则根据对应的封堵规则执行封堵
                    log.info("事件类型:{}, 事件级别:{}, 对IP: {} 进行 {}, 封堵时长为: {} 分钟",
                            map.get("EVENT_TYPE"), map.get("EVENT_CLASS"), map.get("SOURCE_IP"),
                            "0".equals(map.get("BLOCK_TYPE")) ? "临时封堵" : "永久封堵", "0".equals(map.get("BLOCK_TYPE")) ? map.get("BLOCK_DURATION") : "∞");
                    handleStatus = 1;
                }
                String updateSql = "UPDATE SOP_ASSET_EVENT SET HANDLE_STATUS = " + handleStatus + " WHERE EVENT_ID = '" + map.get("EVENT_ID") + "' LIMIT 1";
                dao.execCommand(BaseConstant.DEFAULT_POOL_NAME, updateSql);
            }
        }
    }


    /**
     * 解封扫描
     */
    public void unsealScan() {

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

