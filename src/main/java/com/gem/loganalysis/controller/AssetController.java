package com.gem.loganalysis.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gem.loganalysis.convert.AssetConvert;
import com.gem.loganalysis.model.PageRequest;
import com.gem.loganalysis.model.Result;
import com.gem.loganalysis.model.dto.asset.AssetDTO;
import com.gem.loganalysis.model.dto.asset.AssetQueryDTO;
import com.gem.loganalysis.model.dto.query.LambdaQueryWrapperX;
import com.gem.loganalysis.model.entity.Asset;
import com.gem.loganalysis.service.IAssetService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;
import io.swagger.annotations.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "资产模块 - 安全管理资产")
@RestController
@RequestMapping("/sop/asset")
@Validated
public class AssetController {

    @Resource
    private IAssetService assetService;

    @PostMapping("/edit")
    @ApiOperation("创建/编辑安全管理资产")
    public Result<Object> createAsset(@Valid @RequestBody AssetDTO dto) {
        return Result.ok(assetService.saveOrUpdate(AssetConvert.INSTANCE.convert(dto))?Result.ok("操作成功!"):Result.failed("操作失败!"));
    }

    @PostMapping("/pageList")
    @ApiOperation("分页查询安全管理资产")
    public Result<Object> pageList(@RequestBody PageRequest<AssetQueryDTO> dto) {
        Page<Asset> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        AssetQueryDTO data = dto.getData();
        LambdaQueryWrapperX<Asset> wrapper = new LambdaQueryWrapperX<Asset>()
                .likeIfPresent(Asset::getAssetName, data.getAssetName())
                .eqIfPresent(Asset::getAssetClass, data.getAssetClass())
                .eqIfPresent(Asset::getAssetType, data.getAssetType())
                .eqIfPresent(Asset::getIpAddress, data.getIpAddress())
                .eqIfPresent(Asset::getAssetManager, data.getAssetManager())
                .eqIfPresent(Asset::getAssetGroupId, data.getAssetGroupId())
                .eqIfPresent(Asset::getAssetOrg, data.getAssetOrg())
                .eqIfPresent(Asset::getAssetStatus, data.getAssetStatus())
                .orderByDesc(Asset::getUpdateTime);
        Page<Asset> assetPage = assetService.page(page, wrapper);
        return Result.ok(assetPage);
    }

    @PostMapping("/physicalAssetType")
    @ApiOperation("物理资产类型(之后放到数据字典，先用着)")
    public Result<Object> getPhysicalAssetType() {
        List<Map<String,String>> assetType = new ArrayList<>();
        Map<String,String> map = new HashMap<>();
        map.put("label","服务器");
        map.put("value","1");
        assetType.add(map);
        map = new HashMap<>();
        map.put("label","路由器");
        map.put("value","2");
        assetType.add(map);
        map = new HashMap<>();
        map.put("label","交换机");
        map.put("value","3");
        assetType.add(map);
        map = new HashMap<>();
        map.put("label","防火墙");
        map.put("value","4");
        assetType.add(map);
        return Result.ok(assetType);
    }

    @PostMapping("/logicalAssetType")
    @ApiOperation("物理资产(之后放到数据字典，先用着)")
    public Result<Object> getLogicalAssetType() {
        List<Map<String,String>> assetType = new ArrayList<>();
        Map<String,String> map = new HashMap<>();
        map.put("label","数据库");
        map.put("value","1");
        assetType.add(map);
        map = new HashMap<>();
        map.put("label","中间件");
        map.put("value","2");
        assetType.add(map);
        map = new HashMap<>();
        map.put("label","网络应用");
        map.put("value","3");
        assetType.add(map);
        map = new HashMap<>();
        map.put("label","域名");
        map.put("value","4");
        assetType.add(map);
        return Result.ok(assetType);
    }


}
