package com.gem.loganalysis.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gem.loganalysis.convert.AssetConvert;
import com.gem.loganalysis.model.PageRequest;
import com.gem.loganalysis.model.Result;
import com.gem.loganalysis.model.dto.asset.AssetDTO;
import com.gem.loganalysis.model.dto.asset.AssetGroupDTO;
import com.gem.loganalysis.model.dto.asset.AssetGroupQueryDTO;
import com.gem.loganalysis.model.dto.asset.AssetQueryDTO;
import com.gem.loganalysis.model.dto.query.LambdaQueryWrapperX;
import com.gem.loganalysis.model.entity.Asset;
import com.gem.loganalysis.model.entity.AssetGroup;
import com.gem.loganalysis.model.vo.asset.AssetGroupRespVO;
import com.gem.loganalysis.service.IAssetGroupService;
import com.gem.loganalysis.service.IAssetService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
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
        return Result.ok(AssetConvert.INSTANCE.convertList(assetGroupService.getList(dto)));
    }

    @PostMapping("/edit")
    @ApiOperation("创建/编辑资产分组")
    public Result<String> editGroup(@RequestBody AssetGroupDTO dto) {
        return assetGroupService.editGroup(dto);
    }

    @PostMapping("/orgList")
    @ApiOperation("部门列表(之后是金总提供的用户部门里的那一套，先用着这个)")
    public Result<Object> getOrgList() {
        List<Map<String,Object>> orgList = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        map.put("label","信息网络部");
        map.put("value",1);
        orgList.add(map);
        map = new HashMap<>();
        map.put("label","互联网部");
        map.put("value",2);
        orgList.add(map);
        map = new HashMap<>();
        map.put("label","市场部");
        map.put("value",3);
        orgList.add(map);
        return Result.ok(orgList);
    }




}
