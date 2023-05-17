package com.gem.loganalysis.controller;


import cn.hutool.core.collection.CollUtil;
import com.gem.loganalysis.model.Result;
import com.gem.loganalysis.model.dto.query.LogContentQueryDTO;
import com.gem.loganalysis.model.dto.query.LogFileQueryDTO;
import com.gem.loganalysis.util.MinioUtil;
import com.gem.utils.file.BlockData;
import com.gem.utils.file.BlockFile;
import io.minio.messages.Item;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 前端控制器
 *
 * @author GuoChao
 * @since 2023-05-03
 */
@RestController
@RequestMapping("/logAnalysis/mergeLog")
@AllArgsConstructor
public class MergeLogController {

    private final MinioUtil minioUtil;

    @PostMapping("/fileList")
    public Result<Object> fileList(@RequestBody LogFileQueryDTO dto) {
        List<Item> items = minioUtil.listObjects();

        return Result.ok();
    }

    /**
     * 从文件中读取原始日志信息
     *
     * @param dto 查询参数
     * @return 日志记录
     */
    @PostMapping("/getSourceLogInFile")
    public Result<Object> getSourceLogInFile(@RequestBody LogContentQueryDTO dto) {
        List<String> result = new ArrayList<>();
        String ruleRelaId = dto.getRuleRelaId();
        String day = dto.getUpdateTime().substring(0, 8);
        String fileName = ruleRelaId + day;
        BlockFile blockFile = new BlockFile("./", fileName + ".DAT", fileName + ".IDX", false, 3, 64);
        List<BlockData> blockDataList = blockFile.readData(dto.getLogId());
        if (CollUtil.isNotEmpty(blockDataList)) {
            for (BlockData blockData : blockDataList) {
                result.add(new String(blockData.getData()));
            }
        }
        return Result.ok(result);
    }
}
