package com.gem.loganalysis.controller;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gem.loganalysis.convert.AssetConvert;
import com.gem.loganalysis.enmu.AssetClass;
import com.gem.loganalysis.exception.ServiceException;
import com.gem.loganalysis.model.PageRequest;
import com.gem.loganalysis.model.Result;
import com.gem.loganalysis.model.dto.DeleteDTO;
import com.gem.loganalysis.model.dto.GetDTO;
import com.gem.loganalysis.model.dto.asset.AssetDTO;
import com.gem.loganalysis.model.dto.asset.AssetQueryDTO;
import com.gem.loganalysis.model.dto.asset.AssetUpdateDTO;
import com.gem.loganalysis.model.entity.Asset;
import com.gem.loganalysis.model.vo.ImportRespVO;
import com.gem.loganalysis.model.vo.asset.*;
import com.gem.loganalysis.service.IAssetService;
import com.gem.loganalysis.util.ExcelUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;
import io.swagger.annotations.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "资产模块 - 安全管理资产")
@RestController
@RequestMapping("/sop/asset")
@Validated
public class AssetController {

    @Resource
    private IAssetService assetService;

    @PostMapping("/edit")
    @ApiOperation("创建/编辑安全管理资产")
    public Result<String> editAsset(@Valid @RequestBody AssetDTO dto) {
        return assetService.editAsset(dto);
    }

    @PostMapping("/batchInsert")
    @ApiOperation("批量插入安全管理资产")
    public Result<String> editAsset(@Valid @RequestBody List<AssetDTO> dto) {
        return assetService.saveBatch(AssetConvert.INSTANCE.convertList01(dto))?
                Result.ok("插入成功"):Result.failed("插入失败");
    }

    @PostMapping("/pageList")
    @ApiOperation("分页查询安全管理资产")
    public Result<Page<AssetRespVO>> pageList(@RequestBody PageRequest<AssetQueryDTO> dto) {
        return Result.ok(assetService.getPageList(dto));
    }

    @PostMapping("/list")
    @ApiOperation("查询安全管理资产列表")
    public Result<List<AssetRespVO>> pageList(@RequestBody AssetQueryDTO dto) {
        return Result.ok(assetService.getAssetList(dto));
    }

    @PostMapping("/get")
    @ApiOperation("根据ID获得单一资产信息")
    public Result<AssetRespVO> getAsset(@RequestBody GetDTO dto) {
        if(dto.getId()==null||dto.getId().trim().equals("")){
            return Result.failed("请传入资产唯一编码ID");
        }
        return Result.ok(assetService.getAsset(dto.getId()));
    }

    @PostMapping("/getAccount")
    @ApiOperation("根据ID获得网管密码")
    public Result<AssetAccountRespVO> getAssetAccount(@RequestBody GetDTO dto) {
        if(dto.getId()==null||dto.getId().trim().equals("")){
            return Result.failed("请传入资产唯一编码ID");
        }
        return Result.ok(assetService.getAssetAccount(dto.getId()));
    }

    @PostMapping("/delete")
    @ApiOperation("删除资产")
    public Result<String> deleteAsset(@Valid @RequestBody DeleteDTO dto) {
        if(StringUtils.isBlank(dto.getId())){
            return Result.failed("请传入需要删除的资产ID");
        }
        return assetService.removeById(dto.getId()) ? Result.ok("删除成功!") : Result.failed("删除失败!");
    }

    @PostMapping("/overview")
    @ApiOperation("资产总览")
    public Result<AssetOverviewVO> overview() {
        return Result.ok(assetService.getOverviewInfo());
    }

    @PostMapping("/addAssetTag")
    @ApiOperation("添加标签")
    public Result<String> addAssetTag(@Valid @RequestBody AssetUpdateDTO dto) {
        if(StringUtils.isBlank(dto.getAssetTag())){
            return Result.failed("请传入需要添加的资产标签");
        }
        Asset asset = assetService.getById(dto.getAssetId());
        if(asset==null){
            return Result.failed("该资产不存在");
        }
        asset.setAssetTag(dto.getAssetTag());
        return assetService.updateById(asset)?Result.ok("添加成功"):Result.failed("修改失败");
    }

    @PostMapping("/updateAssetManager")
    @ApiOperation("更换负责人")
    public Result<String> updateAssetManager(@Valid @RequestBody AssetUpdateDTO dto) {
        if(StringUtils.isBlank(dto.getAssetManager())){
            return Result.failed("请传入需要更换的负责人");
        }
        Asset asset = assetService.getById(dto.getAssetId());
        if(asset==null){
            return Result.failed("该资产不存在");
        }
        asset.setAssetManager(dto.getAssetManager());
        return assetService.updateById(asset)?Result.ok("更换成功"):Result.failed("更换失败");
    }

    @PostMapping("/updateGroupId")
    @ApiOperation("修改分组")
    public Result<String> updateGroupId(@Valid @RequestBody AssetUpdateDTO dto) {
        if(StringUtils.isBlank(dto.getAssetGroupId())){
            return Result.failed("请传入更改后的分组ID");
        }
        Asset asset = assetService.getById(dto.getAssetId());
        if(asset==null){
            return Result.failed("该资产不存在");
        }
        asset.setAssetGroupId(dto.getAssetGroupId());
        return assetService.updateById(asset)?Result.ok("修改成功"):Result.failed("修改失败");
    }

    @PostMapping("/physical-import-template")
    @ApiOperation("物理资产导入模板")
    public void importPhysicalTemplate(HttpServletResponse response) throws IOException {
        // 输出
        ExcelUtils.write(response, "物理资产导入模板.xlsx", "物理资产", PhysicalAssetExcelVO.class, null);
    }

    @PostMapping("/logical-import-export")
    @ApiOperation("逻辑资产导入模板")
    public void importLogicalTemplate(HttpServletResponse response) throws IOException {
        // 输出
        ExcelUtils.write(response, "逻辑资产导入模板.xlsx", "逻辑资产", LogicalAssetExcelVO.class, null);
    }

    @PostMapping("/export-physical")
    @ApiOperation("导出物理资产")
    public void exportPhysical(HttpServletResponse response) throws IOException {
        List<AssetRespVO> assetList = assetService.getAssetList(new AssetQueryDTO().setAssetClass(AssetClass.PHYSICAL.getId()));
        // 输出
        ExcelUtils.write(response, "物理资产列表.xlsx", "物理资产"
                , PhysicalAssetExcelVO.class, AssetConvert.INSTANCE.convertList14(assetList));
    }

    @PostMapping("/export-logical")
    @ApiOperation("导出逻辑资产")
    public void exportLogical(HttpServletResponse response) throws IOException {
        List<AssetRespVO> assetList = assetService.getAssetList(new AssetQueryDTO().setAssetClass(AssetClass.LOGICAL.getId()));
        // 输出
        ExcelUtils.write(response, "逻辑资产列表.xlsx", "逻辑资产"
                , LogicalAssetExcelVO.class, AssetConvert.INSTANCE.convertList15(assetList));
    }

    @PostMapping("/import-physical")
    @ApiOperation("导入物理资产")
    @ApiImplicitParam(name = "file", value = "Excel 文件", required = true)
    public Result<ImportRespVO> importPhysicalExcel(@RequestParam("file") MultipartFile file) throws Exception {
        List<PhysicalAssetExcelVO> list = ExcelUtils.read(file, PhysicalAssetExcelVO.class);
        if (CollUtil.isEmpty(list)) {
            throw new ServiceException("导入的资产数据不能为空");
        }
        return Result.ok(assetService.importPhysicalExcel(list));
    }

    @PostMapping("/import-logical")
    @ApiOperation("导入逻辑资产")
    @ApiImplicitParam(name = "file", value = "Excel 文件", required = true)
    public Result<ImportRespVO> importLogicalExcel(@RequestParam("file") MultipartFile file) throws Exception {
        List<LogicalAssetExcelVO> list = ExcelUtils.read(file, LogicalAssetExcelVO.class);
        if (CollUtil.isEmpty(list)) {
            throw new ServiceException("导入的资产数据不能为空");
        }
        return Result.ok(assetService.importLogicalExcel(list));
    }



}
