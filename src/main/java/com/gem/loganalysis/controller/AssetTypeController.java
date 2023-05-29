package com.gem.loganalysis.controller;

import com.gem.loganalysis.model.Result;
import com.gem.loganalysis.model.dto.DeleteDTO;
import com.gem.loganalysis.model.dto.asset.AssetTypeDTO;
import com.gem.loganalysis.model.dto.asset.AssetTypeQueryDTO;
import com.gem.loganalysis.model.vo.asset.AssetTypeRespVO;
import com.gem.loganalysis.model.vo.asset.AssetTypeVO;
import com.gem.loganalysis.service.IAssetTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Api(tags = "资产模块 - 资产类型")
@RestController
@RequestMapping("/sop/assetType")
@Validated
public class AssetTypeController {

    @Resource
    private IAssetTypeService assetTypeService;

    @PostMapping("/list")
    @ApiOperation("资产类型列表")
    public Result<List<AssetTypeRespVO>> list(@RequestBody AssetTypeQueryDTO dto) {
        return Result.ok(assetTypeService.getList(dto));
    }

    @PostMapping("/map")
    @ApiOperation("资产类型Map格式")
    public Result<Map<String,List<AssetTypeRespVO>>> map(@RequestBody AssetTypeQueryDTO dto) {
        return Result.ok(assetTypeService.getTypeMap(dto));
    }

    @PostMapping("/list2")
    @ApiOperation("资产类型另一种格式的列表")
    public Result<List<AssetTypeVO>> list2(@RequestBody AssetTypeQueryDTO dto) {
        return Result.ok(assetTypeService.getAssetList(dto));
    }


    @PostMapping("/edit")
    @ApiOperation("新增编辑资产类型(带typeId是编辑，不带是新增)")
    public Result<String> edit(@RequestBody AssetTypeDTO dto) {
        return Result.ok(assetTypeService.editType(dto)?"操作成功":"操作失败");
    }

    @PostMapping("/deleted")
    @ApiOperation("删除资产类型")
    public Result<String> edit(@Valid @RequestBody DeleteDTO dto) {
        if(StringUtils.isBlank(dto.getId())){
            return Result.failed("请传入要删除的类型ID");
        }
        return Result.ok(assetTypeService.removeById(dto.getId())?"删除成功":"删除失败");
    }


}
