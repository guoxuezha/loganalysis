package com.gem.loganalysis.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.gem.loganalysis.config.MinioConfig;
import com.gem.loganalysis.model.Result;
import com.gem.loganalysis.model.dto.query.LogContentQueryDTO;
import com.gem.loganalysis.model.dto.query.LogFileQueryDTO;
import com.gem.loganalysis.util.MinioUtil;
import com.gem.loganalysis.util.SpringContextUtil;
import com.gem.utils.file.BlockData;
import com.gem.utils.file.BlockFile;
import com.gem.utils.file.MinioRW;
import lombok.AllArgsConstructor;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.gem.loganalysis.util.BlockFileUtil.getMultipartFile;

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

    private final MinioConfig prop;

    /**
     * 查询桶中日志文件列表
     *
     * @param dto 查询参数
     * @return 文件信息列表
     */
    @PostMapping("/fileList")
    public Result<Object> fileList(@RequestBody LogFileQueryDTO dto) throws JSONException {
        List<String> result = new ArrayList<>();
        List<JSONObject> objectList = MinioRW.getObjectList(prop.getEndpoint(), prop.getAccessKey(), prop.getSecretKey(), prop.getBucketName());
        if (CollUtil.isNotEmpty(objectList)) {
            for (JSONObject item : objectList) {
                if (StrUtil.isNotEmpty(dto.getFileName()) && !item.get("FILE_NAME").toString().contains(dto.getFileName())) {
                    continue;
                }
                result.add(item.toString());
            }
        }
        return Result.ok(result);
    }

    /**
     * 测试文件上传
     *
     * @return 上传结果
     */
    @PostMapping("/writeTest")
    public Result<Object> writeTest() {
        MinioUtil minioUtil = SpringContextUtil.getBean(MinioUtil.class);
        File datFile = new File("./0220230517.DAT");
        File idxFile = new File("./0220230517.IDX");
        minioUtil.upload(getMultipartFile(datFile));
        minioUtil.upload(getMultipartFile(idxFile));
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
        // 若目标文件不存在,则尝试从minio拉取
        if (!FileUtil.exist("./" + fileName + ".DAT")) {
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
