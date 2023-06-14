package com.gem.loganalysis.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gem.loganalysis.model.PageRequest;
import com.gem.loganalysis.model.Result;
import com.gem.loganalysis.model.dto.DeleteDTO;
import com.gem.loganalysis.model.dto.GetDTO;
import com.gem.loganalysis.model.dto.asset.AssetGroupDTO;
import com.gem.loganalysis.model.dto.asset.AssetGroupQueryDTO;
import com.gem.loganalysis.model.vo.asset.AssetGroupRespVO;
import com.gem.loganalysis.service.IAssetGroupService;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "资产模块 - 资产分组")
@RestController
@RequestMapping("/sop/assetGroup")
@Validated
public class AssetGroupController {

    @Resource
    private IAssetGroupService assetGroupService;

    @PostMapping("/list")
    @ApiOperation("资产分组列表")
    public Result<List<AssetGroupRespVO>> list(@RequestBody AssetGroupQueryDTO dto) {
        return Result.ok(assetGroupService.getList(dto));
    }

    @PostMapping("/pageList")
    @ApiOperation("资产分组分页")
    public Result<Page<AssetGroupRespVO>> pageList(@RequestBody PageRequest<AssetGroupQueryDTO> dto) {
        return Result.ok(assetGroupService.getPageList(dto));
    }

    @PostMapping("/edit")
    @ApiOperation("创建/编辑资产分组")
    public Result<String> editGroup(@RequestBody AssetGroupDTO dto) {
        return assetGroupService.editGroup(dto);
    }

    @PostMapping("/get")
    @ApiOperation("根据ID获得单一资产分组信息")
    public Result<AssetGroupRespVO> getAssetGroup(@RequestBody GetDTO dto) {
        if (dto.getId() == null || dto.getId().trim().equals("")) {
            return Result.failed("请传入资产分组ID");
        }
        return Result.ok(assetGroupService.getAssetGroup(dto.getId()));
    }

    @PostMapping("/delete")
    @ApiOperation("删除资产分组")
    public Result<String> deleteGroup(@Valid @RequestBody DeleteDTO dto) {
        if(StringUtils.isBlank(dto.getId())){
            return Result.failed("请传入需要删除的资产组织ID");
        }
        return assetGroupService.removeById(dto.getId()) ? Result.ok("删除成功!") : Result.failed("删除失败!");
    }

}
