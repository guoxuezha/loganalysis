package com.gem.loganalysis.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gem.loganalysis.facility.Facility;
import com.gem.loganalysis.model.PageRequest;
import com.gem.loganalysis.model.Result;
import com.gem.loganalysis.model.dto.query.LogQueryDTO;
import com.gem.loganalysis.model.entity.Log;
import com.gem.loganalysis.service.ILogService;
import com.gem.loganalysis.task.LogInStorageTask;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Set;

/**
 * Description: 日志相关交互接口
 * Date: 2023/4/26 9:30
 *
 * @author GuoChao
 **/
@RequestMapping("/log")
@RestController
@Slf4j
@AllArgsConstructor
public class LogManagerController {

    private final ILogService iLogService;

    private final LogInStorageTask logInStorageTask;

    /**
     * 分页查询日志
     *
     * @param dto 封装入参
     * @return 结果
     */
    @PostMapping("/pageList")
    public Result<Object> pageList(@RequestBody PageRequest<LogQueryDTO> dto) {
        Page<Log> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        LogQueryDTO data = dto.getData();
        LambdaQueryWrapper<Log> wrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotEmpty(data.getHost())) {
            wrapper.like(Log::getHost, data.getHost());
        }
        if (StrUtil.isNotEmpty(data.getFacility())) {
            wrapper.like(Log::getFacility, data.getFacility());
        }
        if (StrUtil.isNotEmpty(data.getSeverity())) {
            wrapper.like(Log::getSeverity, data.getSeverity());
        }
        return Result.ok(iLogService.page(page, wrapper));
    }

    @PostMapping("/fetchFacilityCache")
    public Result<Object> fetchFacilityCache() {
        Set<Facility> instanceSet = logInStorageTask.getFacilitySetInstance();
        HashMap<String, Object> map = new HashMap<>(8);
        for (Facility facility : instanceSet) {
            map.put(facility.getClass().getSimpleName(), facility.getCacheInfo());
        }
        return Result.ok(map);
    }


}
