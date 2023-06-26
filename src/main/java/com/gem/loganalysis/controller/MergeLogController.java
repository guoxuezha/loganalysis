package com.gem.loganalysis.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.gem.loganalysis.config.MinioConfig;
import com.gem.loganalysis.model.PageRequest;
import com.gem.loganalysis.model.PageResponse;
import com.gem.loganalysis.model.Result;
import com.gem.loganalysis.model.dto.asset.AssetQueryDTO;
import com.gem.loganalysis.model.dto.query.LogContentQueryDTO;
import com.gem.loganalysis.model.dto.query.LogFileQueryDTO;
import com.gem.loganalysis.model.entity.SopAssetLogPreview;
import com.gem.loganalysis.model.vo.LogShowVO;
import com.gem.loganalysis.model.vo.SourceLogVO;
import com.gem.loganalysis.model.vo.asset.AssetLogFileVO;
import com.gem.loganalysis.model.vo.asset.AssetRespVO;
import com.gem.loganalysis.service.IAssetMergeLogService;
import com.gem.loganalysis.util.MinioUtil;
import com.gem.loganalysis.util.SpringContextUtil;
import com.gem.utils.file.MinioRW;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.gem.loganalysis.util.BlockFileUtil.getBlockFileRootPath;
import static com.gem.loganalysis.util.BlockFileUtil.getMultipartFile;

/**
 * 前端控制器
 *
 * @author GuoChao
 * @since 2023-05-03
 */
@Api(tags = "归并日志管理")
@RestController
@RequestMapping("/logAnalysis/mergeLog")
@AllArgsConstructor
public class MergeLogController {

    private final MinioConfig prop;

    private final IAssetMergeLogService iAssetMergeLogService;

    /**
     * 查询桶中日志文件列表
     *
     * @param dto 查询参数
     * @return 文件信息列表
     */
    @ApiOperation("查询桶中日志文件列表")
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
        File datFile = new File(getBlockFileRootPath() + "0220230517.DAT");
        File idxFile = new File(getBlockFileRootPath() + "0220230517.IDX");
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
    @ApiOperation("从文件中读取原始日志信息")
    @PostMapping("/getSourceLogInFile")
    public Result<Object> getSourceLogInFile(@RequestBody LogContentQueryDTO dto) {
        return Result.ok(iAssetMergeLogService.getSourceLog(dto));
    }

    @ApiOperation("查看已接收到日志的资产列表")
    @PostMapping("/getLogAsset")
    public Result<PageResponse<AssetRespVO>> getLogAsset(@RequestBody PageRequest<AssetQueryDTO> dto) {
        return Result.ok(iAssetMergeLogService.getLogAsset(dto));
    }

    @ApiOperation("根据资产ID查询原始日志样例")
    @PostMapping("/getSourceLogDemo")
    public Result<PageResponse<SopAssetLogPreview>> getSourceLogDemo(@RequestBody PageRequest<String> dto) {
        return Result.ok(iAssetMergeLogService.getSourceLogDemo(dto));
    }

    @ApiOperation("根据资产ID查询归并日志列表")
    @PostMapping("/getMergeLogByAsset")
    public Result<PageResponse<LogShowVO>> getMergeLogByAsset(@RequestBody PageRequest<String> dto) {
        return Result.ok(iAssetMergeLogService.getMergeLogByAsset(dto));
    }

    @ApiOperation("根据资产ID查询日志文件列表")
    @PostMapping("/getSourceLogFileByAsset")
    public Result<PageResponse<AssetLogFileVO>> getSourceLogFileByAsset(@RequestBody PageRequest<String> dto) {
        return Result.ok(iAssetMergeLogService.getSourceLogFileByAsset(dto));
    }

    @ApiOperation("根据日志文件查看内容")
    @PostMapping("/getSourceLogFileContent")
    public Result<SourceLogVO> getSourceLogFileContent(@RequestBody PageRequest<String> dto) {
        return Result.ok(iAssetMergeLogService.getSourceLogFileContent(dto));
    }

}
