package com.gem.loganalysis.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gem.loganalysis.model.PageRequest;
import com.gem.loganalysis.model.Result;
import com.gem.loganalysis.model.entity.BlackWhiteList;
import com.gem.loganalysis.service.IBlackWhiteListService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 黑白名单 前端控制器
 *
 * @author GuoChao
 * @since 2023-05-03
 */
@RestController
@RequestMapping("/loganalysis/blackWhiteList")
@AllArgsConstructor
@Slf4j
public class BlackWhiteListController {

    private final IBlackWhiteListService iBlackWhiteListService;

    @PostMapping("/pageList")
    public Result<Object> fetchFacilityCache(@RequestBody PageRequest<String> dto) {
        Page<BlackWhiteList> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        String orgId = dto.getData();
        LambdaQueryWrapper<BlackWhiteList> wrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotEmpty(orgId)) {
            wrapper.eq(BlackWhiteList::getOrgId, orgId);
        }
        return Result.ok(iBlackWhiteListService.page(page, wrapper));
    }




}
