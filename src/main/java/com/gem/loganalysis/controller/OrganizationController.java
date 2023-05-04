package com.gem.loganalysis.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gem.loganalysis.model.PageRequest;
import com.gem.loganalysis.model.Result;
import com.gem.loganalysis.model.dto.DeleteDTO;
import com.gem.loganalysis.model.dto.edit.OrganizationDTO;
import com.gem.loganalysis.model.entity.Organization;
import com.gem.loganalysis.service.IOrganizationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 组织机构 前端控制器
 *
 * @author GuoChao
 * @since 2023-05-03
 */
@RestController
@RequestMapping("/logAnalysis/organization")
@AllArgsConstructor
public class OrganizationController {

    private final IOrganizationService iOrganizationService;

    @PostMapping("/pageList")
    public Result<Object> fetchFacilityCache(@RequestBody PageRequest<String> dto) {
        Page<Organization> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        String orgName = dto.getData();
        LambdaQueryWrapper<Organization> wrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotEmpty(orgName)) {
            wrapper.like(Organization::getOrgName, orgName);
        }
        return Result.ok(iOrganizationService.page(page, wrapper));
    }

    @PostMapping("/edit")
    public Result<Object> edit(@RequestBody OrganizationDTO dto) {
        boolean result = iOrganizationService.saveOrUpdate(new Organization(dto));
        return result ? Result.ok("操作成功!") : Result.failed("操作失败!");
    }

    @PostMapping("/delete")
    public Result<Object> delete(@RequestBody DeleteDTO dto) {
        /*LambdaQueryWrapper<Organization> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Organization::getOrgId, dto.getId());*/
        boolean remove = iOrganizationService.removeById(dto.getId());
        return remove ? Result.ok("删除成功!") : Result.failed("删除失败!");
    }


}
