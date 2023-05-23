package com.gem.loganalysis.controller;


import cn.hutool.core.lang.tree.Tree;
import com.gem.loganalysis.model.Result;
import com.gem.loganalysis.model.dto.DeleteDTO;
import com.gem.loganalysis.model.dto.asset.AssetDTO;
import com.gem.loganalysis.model.dto.edit.OrgDTO;
import com.gem.loganalysis.model.dto.query.LambdaQueryWrapperX;
import com.gem.loganalysis.model.dto.query.OrgQueryDTO;
import com.gem.loganalysis.model.entity.M4SsoOrg;
import com.gem.loganalysis.model.vo.OrgRespVO;
import com.gem.loganalysis.model.vo.TreeRespVO;
import com.gem.loganalysis.service.IM4SsoOrgService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 组织机构 前端控制器
 * </p>
 *
 * @author czw
 * @since 2023-05-23
 */
@Api(tags = "组织机构")
@RestController
@RequestMapping("/logAnalysis/org")
public class M4SsoOrgController {

    @Resource
    private IM4SsoOrgService m4SsoOrgService;


    @PostMapping("/edit")
    @ApiOperation("创建/编辑组织机构")
    public Result<String> editOrg(@Valid @RequestBody OrgDTO dto) {
        return m4SsoOrgService.editOrg(dto)? Result.ok("操作成功!") : Result.failed("操作失败!");
    }

    @PostMapping("/list")
    @ApiOperation("组织机构树形结构")
    public Result<List<OrgRespVO>> listOrg(@Valid @RequestBody OrgQueryDTO dto) {
        return Result.ok(m4SsoOrgService.OrgList(dto));
    }

    @PostMapping("/list-simple")
    @ApiOperation("组织机构简单的树形结构 用于下拉")
    public Result<List<TreeRespVO>> listSimpleOrg() {
        return Result.ok(m4SsoOrgService.OrgSimpleList());
    }

    @PostMapping("/delete")
    @ApiOperation("删除组织机构")
    public Result<String> editOrg(@Valid @RequestBody DeleteDTO dto) {
        if(StringUtils.isBlank(dto.getId())){
            return Result.failed("请传入需要删除的组织机构的ID");
        }
        List<M4SsoOrg> list = m4SsoOrgService.list(new LambdaQueryWrapperX<M4SsoOrg>().eq(M4SsoOrg::getParentOrg, dto.getId()));
        if(list.size()!=0){
            return Result.failed("该组织机构下还有子节点,请先删除子节点");
        }
        return m4SsoOrgService.removeById(dto.getId()) ? Result.ok("删除成功!") : Result.failed("删除失败!");
    }





}
