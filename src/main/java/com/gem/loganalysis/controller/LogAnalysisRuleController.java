package com.gem.loganalysis.controller;


import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gem.loganalysis.model.PageRequest;
import com.gem.loganalysis.model.Result;
import com.gem.loganalysis.model.bo.LogAnalysisRuleBo;
import com.gem.loganalysis.model.bo.LogAnalysisRulePool;
import com.gem.loganalysis.model.bo.LogNormalFormTree;
import com.gem.loganalysis.model.dto.DeleteDTO;
import com.gem.loganalysis.model.dto.GetDTO;
import com.gem.loganalysis.model.dto.edit.LogAnalysisRuleDTO;
import com.gem.loganalysis.model.dto.edit.LogAnalysisRuleRelaDTO;
import com.gem.loganalysis.model.dto.edit.LogFieldMappingDTO;
import com.gem.loganalysis.model.dto.query.AnalysisRuleQueryDTO;
import com.gem.loganalysis.model.entity.Asset;
import com.gem.loganalysis.model.entity.LogAnalysisRule;
import com.gem.loganalysis.model.entity.LogAnalysisRuleRela;
import com.gem.loganalysis.service.ILogAnalysisRuleRelaService;
import com.gem.loganalysis.service.ILogAnalysisRuleService;
import com.gem.loganalysis.service.impl.AssetServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 日志解析规则 前端控制器
 *
 * @author GuoChao
 * @since 2023-05-03
 */
@Api(tags = "日志分析规则")
@RestController
@RequestMapping("/logAnalysis/analysisRule")
@AllArgsConstructor
public class LogAnalysisRuleController {

    private final AssetServiceImpl assetService;

    private final ILogAnalysisRuleService iLogAnalysisRuleService;

    private final ILogAnalysisRuleRelaService iLogAnalysisRuleRelaService;

    private final LogAnalysisRulePool logAnalysisRulePool;

    @ApiOperation("查看日志范式树")
    @PostMapping("/showLogNormalForm")
    public Result<LogNormalFormTree> showLogNormalForm(@RequestBody GetDTO dto) {
        return Result.ok(iLogAnalysisRuleRelaService.showLogNormalForm(dto.getId()));
    }

    @ApiOperation("编辑解析规则日志字段映射关系")
    @PostMapping("/editLogFieldMapping")
    public Result<Object> editLogFieldMapping(@RequestBody LogFieldMappingDTO dto) {
        return iLogAnalysisRuleRelaService.editLogFieldMapping(dto) ? Result.ok("编辑成功！") : Result.failed("编辑失败！");
    }

    @ApiOperation("全查日志解析规则列表")
    @PostMapping("/list")
    public Result<Object> list() {
        return Result.ok(iLogAnalysisRuleService.list());
    }

    @ApiOperation("分页查询日志解析规则")
    @PostMapping("/pageList")
    public Result<Object> pageList(@RequestBody PageRequest<String> dto) {
        Page<LogAnalysisRule> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        return Result.ok(iLogAnalysisRuleService.page(page));
    }

    @ApiOperation("编辑日志解析规则")
    @PostMapping("/edit")
    public Result<Object> edit(@RequestBody LogAnalysisRuleDTO dto) {
        boolean result = iLogAnalysisRuleService.saveOrUpdate(new LogAnalysisRule(dto));
        return result ? Result.ok("编辑成功") : Result.failed("编辑失败!");
    }

    @ApiOperation("删除日志解析规则")
    @PostMapping("/delete")
    public Result<Object> delete(@RequestBody DeleteDTO dto) {
        boolean deleteResult = iLogAnalysisRuleService.removeById(dto.getId());
        return deleteResult ? Result.ok("删除成功!") : Result.failed("删除失败!");
    }

    @ApiOperation("分页查询设备的日志解析规则")
    @PostMapping("/getAnalysisRules")
    public Result<Object> getAnalysisRules(@RequestBody PageRequest<AnalysisRuleQueryDTO> dto) {
        return Result.ok(iLogAnalysisRuleRelaService.getAnalysisRules(dto));
    }

    @ApiOperation("配置数据资产的日志解析规则")
    @PostMapping("/setAnalysisRuleRela")
    public Result<Object> setAnalysisRuleRela(@RequestBody LogAnalysisRuleRelaDTO dto) {
        LogAnalysisRuleRela ruleRela = new LogAnalysisRuleRela(dto);
        boolean editResult;
        if (StrUtil.isBlank(ruleRela.getRuleRelaId())) {
            ruleRela.setRuleRelaId(IdUtil.fastSimpleUUID());
            editResult = iLogAnalysisRuleRelaService.save(ruleRela);
        } else {
            editResult = iLogAnalysisRuleRelaService.updateById(ruleRela);
            // 同步修改内存对象
            if (editResult) {
                // 先查询日志解析规则对应设备的Host
                LogAnalysisRuleRela byId = iLogAnalysisRuleRelaService.getById(ruleRela.getRuleRelaId());
                LambdaQueryWrapper<Asset> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(Asset::getAssetId, byId.getAssetId());
                Asset one = assetService.getOne(queryWrapper);
                if (one != null) {
                    String ipAddress = one.getIpAddress();
                    LogAnalysisRuleBo logAnalysisRuleBo = logAnalysisRulePool.getLogAnalysisRuleBoMap().get(ipAddress + "~" + byId.getFacility() + "~" + byId.getSeverity());
                    if (logAnalysisRuleBo != null) {
                        logAnalysisRuleBo.reloadInfo(dto);
                    }
                }
            }
        }
        return editResult ? Result.ok("编辑成功!") : Result.failed("编辑失败！");
    }

    /**
     * 删除日志解析规则映射关系
     *
     * @param dto 入参
     * @return 删除结果
     */
    @ApiOperation("删除日志解析规则映射关系")
    @PostMapping("/deleteAnalysisRuleRela")
    public Result<Object> deleteAnalysisRuleRela(@RequestBody DeleteDTO dto) {
        return iLogAnalysisRuleRelaService.removeById(dto.getId()) ? Result.ok("删除成功!") : Result.failed("删除失败!");
    }

    /**
     * 分页查询设备的日志记录
     *
     * @param dto 分页查询参数
     * @return 设备日志列表
     */
    @ApiOperation("分页查询设备的日志记录")
    @PostMapping("/getLogRecordsByAsset")
    public Result<Object> getLogRecordsByAsset(@RequestBody PageRequest<String> dto) {
        return Result.ok(iLogAnalysisRuleRelaService.getLogRecordsByAsset(dto));
    }

    /**
     * 分页查询某解析规则下的日志记录
     *
     * @param dto 分页查询参数
     * @return 规则日志列表
     */
    @ApiOperation("分页查询某解析规则下的日志记录")
    @PostMapping("/getLogRecordsByRuleRela")
    public Result<Object> getLogRecordsByRuleRela(@RequestBody PageRequest<String> dto) {
        return Result.ok(iLogAnalysisRuleRelaService.getLogRecordsByRuleRela(dto));
    }

    /**
     * 查看日志解析规则对象概况
     *
     * @return 日志解析对象内存占用情况
     */
    @ApiOperation("查看日志解析规则对象概况")
    @PostMapping("/fetchFacilityCache")
    public Result<Object> fetchFacilityCache() {
        HashMap<String, Object> map = new HashMap<>(8);
        for (Map.Entry<String, LogAnalysisRuleBo> entry : logAnalysisRulePool.getLogAnalysisRuleBoMap().entrySet()) {
            map.put(entry.getKey(), entry.getValue().getCacheInfo());
        }
        return Result.ok(map);
    }

}
