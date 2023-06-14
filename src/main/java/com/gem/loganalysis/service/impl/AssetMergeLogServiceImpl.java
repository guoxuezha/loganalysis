package com.gem.loganalysis.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gem.gemada.dal.db.pools.DAO;
import com.gem.loganalysis.config.MinioConfig;
import com.gem.loganalysis.mapper.AssetMergeLogMapper;
import com.gem.loganalysis.model.BaseConstant;
import com.gem.loganalysis.model.PageRequest;
import com.gem.loganalysis.model.PageResponse;
import com.gem.loganalysis.model.bo.BlockFileSearchServer;
import com.gem.loganalysis.model.dto.query.LogContentQueryDTO;
import com.gem.loganalysis.model.entity.AssetMergeLog;
import com.gem.loganalysis.model.vo.LogShowVO;
import com.gem.loganalysis.model.vo.asset.AssetLogFileVO;
import com.gem.loganalysis.model.vo.asset.AssetRespVO;
import com.gem.loganalysis.service.IAssetMergeLogService;
import com.gem.loganalysis.util.MapToBeanUtil;
import com.gem.utils.file.BlockData;
import com.gem.utils.file.BlockFile;
import com.gem.utils.file.MinioRW;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.gem.loganalysis.util.BlockFileUtil.getBlockFileRootPath;

/**
 * <p>
 * 归并日志内容 服务实现类
 * </p>
 *
 * @author GuoChao
 * @since 2023-05-10
 */
@Service
public class AssetMergeLogServiceImpl extends ServiceImpl<AssetMergeLogMapper, AssetMergeLog> implements IAssetMergeLogService {

    @Resource
    private MinioConfig prop;

    @Resource
    private DAO dao;

    @Resource
    private AssetServiceImpl assetServiceImpl;

    @Override
    public List<String> getSourceLog(LogContentQueryDTO dto) {
        List<String> result = new ArrayList<>();
        String ruleRelaId = dto.getRuleRelaId();
        String day = dto.getUpdateTime().substring(0, 8);
        String fileName = ruleRelaId + day;
        // 若目标文件不存在,则尝试从minio拉取
        if (!FileUtil.exist(getBlockFileRootPath() + fileName + ".DAT")) {
            try (InputStream datInputStream = MinioRW.read(prop.getEndpoint(), prop.getAccessKey(), prop.getSecretKey(), prop.getBucketName(), fileName + ".DAT");
                 InputStream idxInputStream = MinioRW.read(prop.getEndpoint(), prop.getAccessKey(), prop.getSecretKey(), prop.getBucketName(), fileName + ".IDX");
                 FileOutputStream datOutputStream = new FileOutputStream("./" + fileName + ".DAT");
                 FileOutputStream idxOutputStream = new FileOutputStream("./" + fileName + ".IDX")) {
                IoUtil.copy(datInputStream, datOutputStream);
                IoUtil.copy(idxInputStream, idxOutputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        BlockFile blockFile = new BlockFile(getBlockFileRootPath(), fileName + ".DAT", fileName + ".IDX", false, 3, 64);
        List<BlockData> blockDataList = blockFile.readData(dto.getLogId());
        if (CollUtil.isNotEmpty(blockDataList)) {
            for (BlockData blockData : blockDataList) {
                // result.add(objectMapper.readValue(new String(blockData.getData()), MergeLog.class));
                result.add((new String(blockData.getData())));
            }
        }
        blockFile.destroy();
        return result;
    }

    @Override
    public PageResponse<AssetRespVO> getLogAsset(Integer pageNum, Integer pageSize) {
        PageResponse<AssetRespVO> result = new PageResponse<>();
        String querySql = "SELECT A.* FROM SOP_ASSET A " +
                "LEFT JOIN SOP_ASSET_LOG_PREVIEW B ON A.IP_ADDRESS = B.`HOST` " +
                "WHERE B.`HOST` IS NOT NULL AND A.DELETE_STATE = 0 GROUP BY ASSET_ID";
        long count = dao.getDataSetCount(BaseConstant.DEFAULT_POOL_NAME, querySql);
        if (count != 0) {
            ArrayList<HashMap<String, String>> dataSet = dao.getDataSet(BaseConstant.DEFAULT_POOL_NAME, querySql, pageNum, pageSize);
            List<AssetRespVO> list = MapToBeanUtil.execute(dataSet, AssetRespVO.class);
            list.forEach(e -> assetServiceImpl.changeAssetName(e));
            result.setTotal((int) count);
            result.setRecords(list);
        }
        return result;
    }

    @Override
    public PageResponse<LogShowVO> getMergeLogByAsset(PageRequest<String> dto) {
        PageResponse<LogShowVO> result = new PageResponse<>();
        String querySql = "SELECT MESSAGE " +
                "FROM SOP_ASSET_MERGE_LOG WHERE RULE_RELA_ID IN " +
                "(SELECT RULE_RELA_ID FROM SOP_LOG_ANALYSIS_RULE_RELA WHERE ASSET_ID = '" + dto.getData() + "')";
        long count = dao.getDataSetCount(BaseConstant.DEFAULT_POOL_NAME, querySql);
        if (count != 0) {
            ArrayList<HashMap<String, String>> dataSet = dao.getDataSet(BaseConstant.DEFAULT_POOL_NAME, querySql, dto.getPageNum(), dto.getPageSize());
            List<LogShowVO> list = new ArrayList<>();
            if (CollUtil.isNotEmpty(dataSet)) {
                for (HashMap<String, String> map : dataSet) {
                    list.add(new LogShowVO(map.get("MESSAGE")));
                }
            }
            result.setTotal((int) count);
            result.setRecords(list);
        }
        return result;
    }

    @Override
    public PageResponse<AssetLogFileVO> getSourceLogFileByAsset(PageRequest<String> dto) {
        PageResponse<AssetLogFileVO> result = new PageResponse<>();
        String ruleRelaQuerySql = "SELECT RULE_RELA_ID, SEVERITY, FACILITY FROM SOP_LOG_ANALYSIS_RULE_RELA WHERE ASSET_ID = '" + dto.getData() + "'";
        ArrayList<HashMap<String, String>> dataSet = dao.getDataSet(BaseConstant.DEFAULT_POOL_NAME, ruleRelaQuerySql);
        if (CollUtil.isNotEmpty(dataSet)) {
            List<AssetLogFileVO> list = new ArrayList<>();
            BlockFileSearchServer blockFileSearchServer = BlockFileSearchServer.getInstance();
            for (HashMap<String, String> map : dataSet) {
                list.addAll(blockFileSearchServer.getFileInfoByRuleRelaId(map.get("RULE_RELA_ID"), map.get("SEVERITY"), map.get("FACILITY")));
            }
            int total = list.size();
            result.setTotal(total);

            // 手动分页
            int start = (dto.getPageNum() - 1) * dto.getPageSize();
            int end = dto.getPageNum() * dto.getPageSize() - 1;
            if (total < start) {
                result.setRecords(new ArrayList<>());
            } else if (total <= end) {
                result.setRecords(list.subList(start, total));
            } else {
                result.setRecords(list.subList(start, end));
            }
        }
        return result;
    }

    @Override
    public PageResponse<String> getSourceLogFileContent(PageRequest<String> dto) {
        PageResponse<String> result = new PageResponse<>();
        String fileName = dto.getData();
        BlockFile blockFile = new BlockFile(getBlockFileRootPath(), fileName + ".DAT", fileName + ".IDX", true, 3, 64);
        long total = blockFile.getBlockCount();
        result.setTotal((int) total);

        int start = (dto.getPageNum() - 1) * dto.getPageSize();
        if (total >= start) {
            blockFile.blockPosition(start);
            List<String> records = new ArrayList<>();
            int loop = (int) Math.min(dto.getPageSize(), total - start);
            for (int i = 0; i < loop; i++) {
                BlockData fetch = blockFile.fetch();
                if (fetch != null) {
                    records.add(new String(fetch.getData(), StandardCharsets.UTF_8));
                }
            }
            result.setRecords(records);
        }
        blockFile.destroy();
        return result;
    }

}
