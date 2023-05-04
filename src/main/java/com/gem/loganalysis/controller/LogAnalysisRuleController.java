package com.gem.loganalysis.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gem.loganalysis.model.PageRequest;
import com.gem.loganalysis.model.Result;
import com.gem.loganalysis.model.dto.DeleteDTO;
import com.gem.loganalysis.model.dto.edit.LogAnalysisRuleDTO;
import com.gem.loganalysis.model.entity.LogAnalysisRule;
import com.gem.loganalysis.service.ILogAnalysisRuleService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 日志解析规则 前端控制器
 *
 * @author GuoChao
 * @since 2023-05-03
 */
@RestController
@RequestMapping("/logAnalysis/analysisRule")
@AllArgsConstructor
public class LogAnalysisRuleController {

    private final ILogAnalysisRuleService iLogAnalysisRuleService;

    /**
     * 全查日志解析规则列表
     *
     * @return 日志解析规则列表
     */
    @PostMapping("/list")
    public Result<Object> list() {
        return Result.ok(iLogAnalysisRuleService.list());
    }


    /**
     * 分页查询日志解析规则
     *
     * @param dto 分页查询参数
     * @return 日志解析规则列表
     */
    @PostMapping("/pageList")
    public Result<Object> pageList(@RequestBody PageRequest<String> dto) {
        Page<LogAnalysisRule> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        return Result.ok(iLogAnalysisRuleService.page(page));
    }

    @PostMapping("/edit")
    public Result<Object> edit(@RequestBody LogAnalysisRuleDTO dto) {
        boolean result = iLogAnalysisRuleService.saveOrUpdate(new LogAnalysisRule(dto));
        return result ? Result.ok("编辑成功") : Result.failed("编辑失败!");
    }

    @PostMapping("/delete")
    public Result<Object> delete(@RequestBody DeleteDTO dto) {
        boolean deleteResult = iLogAnalysisRuleService.removeById(dto.getId());
        return deleteResult ? Result.ok("删除成功!") : Result.failed("删除失败!");
    }



}
