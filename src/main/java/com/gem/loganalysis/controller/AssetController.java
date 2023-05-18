package com.gem.loganalysis.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gem.loganalysis.convert.AssetConvert;
import com.gem.loganalysis.model.PageRequest;
import com.gem.loganalysis.model.Result;
import com.gem.loganalysis.model.dto.GetDTO;
import com.gem.loganalysis.model.dto.asset.AssetDTO;
import com.gem.loganalysis.model.dto.asset.AssetQueryDTO;
import com.gem.loganalysis.model.dto.query.LambdaQueryWrapperX;
import com.gem.loganalysis.model.entity.Asset;
import com.gem.loganalysis.model.vo.asset.AssetRespVO;
import com.gem.loganalysis.service.IAssetService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;
import io.swagger.annotations.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

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
        return null;
    }

    @PostMapping("/pageList")
    @ApiOperation("分页查询安全管理资产")
    public Result<Page<AssetRespVO>> pageList(@RequestBody PageRequest<AssetQueryDTO> dto) {
        return Result.ok(assetService.getPageList(dto));
    }

    @PostMapping("/get")
    @ApiOperation("根据ID获得单一资产信息")
    public Result<AssetRespVO> getAsset(@RequestBody GetDTO dto) {
        if(dto.getId()==null||dto.getId().trim().equals("")){
            return Result.failed("请传入资产唯一编码ID");
        }
        return Result.ok(assetService.getAsset(dto.getId()));
    }

}
