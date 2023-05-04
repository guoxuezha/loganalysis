package com.gem.loganalysis.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gem.loganalysis.model.PageRequest;
import com.gem.loganalysis.model.Result;
import com.gem.loganalysis.model.dto.DeleteDTO;
import com.gem.loganalysis.model.dto.edit.BlockRuleDTO;
import com.gem.loganalysis.model.dto.query.BlockRecordQueryDTO;
import com.gem.loganalysis.model.dto.query.BlockRuleQueryDTO;
import com.gem.loganalysis.model.entity.BlockOffRule;
import com.gem.loganalysis.service.IBlockOffRuleService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 封堵规则 前端控制器
 *
 * @author GuoChao
 * @since 2023-05-03
 */
@RestController
@RequestMapping("/logAnalysis/blockRule")
@AllArgsConstructor
public class BlockOffRuleController {

    private final IBlockOffRuleService iBlockOffRuleService;

    /**
     * 分页查询设备的封堵规则
     *
     * @param dto 入参对象
     * @return 封堵规则列表
     */
    @PostMapping("/pageList")
    public Result<Object> pageList(@RequestBody PageRequest<BlockRuleQueryDTO> dto) {
        BlockRuleQueryDTO data = dto.getData();
        if (data.getEquipId() == null) {
            return Result.failed("设备ID不得为空!");
        }
        Page<BlockOffRule> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        LambdaQueryWrapper<BlockOffRule> wrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotEmpty(data.getSeverity())) {
            wrapper.eq(BlockOffRule::getSeverity, data.getSeverity());
        }
        if (StrUtil.isNotEmpty(data.getFacility())) {
            wrapper.eq(BlockOffRule::getSeverity, data.getFacility());
        }
        return Result.ok(iBlockOffRuleService.page(page, wrapper));
    }

    @PostMapping("/edit")
    public Result<Object> edit(@RequestBody BlockRuleDTO dto) {
        boolean result = iBlockOffRuleService.saveOrUpdate(new BlockOffRule(dto));
        return result ? Result.ok("编辑成功!") : Result.failed("编辑失败!");
    }

    @PostMapping("/delete")
    public Result<Object> delete(@RequestBody DeleteDTO dto) {
        boolean deleteResult = iBlockOffRuleService.removeById(dto.getId());
        return deleteResult ? Result.ok("删除成功!") : Result.failed("删除失败!");
    }

    /**
     * 分页查询封堵记录
     *
     * @param dto 分页查询参数
     * @return 封堵记录列表
     */
    @PostMapping("/blockOffRecords")
    public Result<Object> blockOffRecords(@RequestBody PageRequest<BlockRecordQueryDTO> dto) {

        return Result.ok();
    }


}
