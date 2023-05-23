package com.gem.loganalysis.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gem.loganalysis.config.MinioConfig;
import com.gem.loganalysis.mapper.AssetMergeLogMapper;
import com.gem.loganalysis.model.dto.query.LogContentQueryDTO;
import com.gem.loganalysis.model.entity.AssetMergeLog;
import com.gem.loganalysis.service.IAssetMergeLogService;
import com.gem.utils.file.BlockData;
import com.gem.utils.file.BlockFile;
import com.gem.utils.file.MinioRW;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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

    @Override
    public List<String> getSourceLog(LogContentQueryDTO dto) {
        List<String> result = new ArrayList<>();
        String ruleRelaId = dto.getRuleRelaId();
        String day = dto.getUpdateTime().substring(0, 8);
        String fileName = ruleRelaId + day;
        // 若目标文件不存在,则尝试从minio拉取
        if (!FileUtil.exist(getBlockFileRootPath() + fileName + ".DAT")) {
            try (InputStream datInputStream = MinioRW.read(prop.getEndpoint(), prop.getAccessKey(), prop.getSecretKey(), prop.getBucketName(), fileName + ".DAT"); InputStream idxInputStream = MinioRW.read(prop.getEndpoint(), prop.getAccessKey(), prop.getSecretKey(), prop.getBucketName(), fileName + ".IDX"); FileOutputStream datOutputStream = new FileOutputStream("./" + fileName + ".DAT"); FileOutputStream idxOutputStream = new FileOutputStream("./" + fileName + ".IDX")) {
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
        return result;
    }

}
