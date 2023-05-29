package com.gem.loganalysis.controller;

import com.gem.loganalysis.convert.AssetConvert;
import com.gem.loganalysis.model.Result;
import com.gem.loganalysis.model.dto.DeleteDTO;
import com.gem.loganalysis.model.dto.asset.AssetTypeDTO;
import com.gem.loganalysis.model.dto.asset.LogicalAssetDiscoveryRuleDTO;
import com.gem.loganalysis.model.dto.asset.PhysicalAssetDiscoveryRuleDTO;
import com.gem.loganalysis.model.entity.LogicalAssetDiscoveryRule;
import com.gem.loganalysis.model.entity.PhysicalAssetDiscoveryRule;
import com.gem.loganalysis.model.vo.asset.LogicalAssetDiscoveryRuleVO;
import com.gem.loganalysis.model.vo.asset.PhysicalAssetDiscoveryRuleVO;
import com.gem.loganalysis.service.ILogicalAssetDiscoveryService;
import com.gem.loganalysis.service.IPhysicalAssetDiscoveryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "资产模块 - 资产发现规则")
@RestController
@RequestMapping("/sop/assetDiscoveryRule")
@Validated
public class AssetDiscoveryRuleController {

    @Resource
    private ILogicalAssetDiscoveryService logicalAssetDiscoveryService;
    @Resource
    private IPhysicalAssetDiscoveryService physicalAssetDiscoveryService;




    @PostMapping("/editPhysicalRule")
    @ApiOperation("新增编辑物理资产规则(带ID是编辑，不带是新增)")
    public Result<String> editPhysicalRule(@RequestBody PhysicalAssetDiscoveryRuleDTO dto) {
        return Result.ok(physicalAssetDiscoveryService.saveOrUpdate(AssetConvert.INSTANCE.convert(dto))?"操作成功":"操作失败");
    }

    @PostMapping("/editLogicalRule")
    @ApiOperation("新增编辑物理资产规则(带ID是编辑，不带是新增)")
    public Result<String> editLogicalRule(@RequestBody LogicalAssetDiscoveryRuleDTO dto) {
        return Result.ok(logicalAssetDiscoveryService.saveOrUpdate(AssetConvert.INSTANCE.convert(dto))?"操作成功":"操作失败");
    }

    @PostMapping("/deletedPhysicalRule")
    @ApiOperation("删除物理资产规则")
    public Result<String> deletedPhysicalRule(@RequestBody DeleteDTO dto) {
        if(StringUtils.isBlank(dto.getId())){
            return Result.failed("请传入要删除的物理资产规则ID");
        }
        return Result.ok(physicalAssetDiscoveryService.removeById(dto.getId())?"删除成功":"删除失败");
    }

    @PostMapping("/deletedLogicalRule")
    @ApiOperation("删除逻辑资产规则")
    public Result<String> deletedLogicalRule(@RequestBody DeleteDTO dto) {
        return Result.ok(logicalAssetDiscoveryService.removeById(dto.getId())?"删除成功":"删除失败");
    }

    @PostMapping("/physicalRuleList")
    @ApiOperation("物理资产规则列表")
    public Result<List<PhysicalAssetDiscoveryRuleVO>> physicalRuleList(@RequestBody PhysicalAssetDiscoveryRuleDTO dto) {
        List<PhysicalAssetDiscoveryRule> list = physicalAssetDiscoveryService.list();
        return Result.ok(AssetConvert.INSTANCE.convertList18(list));
    }

    @PostMapping("/logicalRuleList")
    @ApiOperation("逻辑资产规则列表")
    public Result<List<LogicalAssetDiscoveryRuleVO>> logicalRuleList(@RequestBody LogicalAssetDiscoveryRuleDTO dto) {
        List<LogicalAssetDiscoveryRule> list = logicalAssetDiscoveryService.list();
        return Result.ok(AssetConvert.INSTANCE.convertList19(list));
    }


}
