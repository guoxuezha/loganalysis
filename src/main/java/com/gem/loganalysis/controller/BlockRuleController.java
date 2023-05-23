package com.gem.loganalysis.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gem.loganalysis.model.PageRequest;
import com.gem.loganalysis.model.Result;
import com.gem.loganalysis.model.dto.DeleteDTO;
import com.gem.loganalysis.model.dto.edit.BlockRuleDTO;
import com.gem.loganalysis.model.dto.query.BlockRecordQueryDTO;
import com.gem.loganalysis.model.dto.query.BlockRuleQueryDTO;
import com.gem.loganalysis.model.dto.query.LambdaQueryWrapperX;
import com.gem.loganalysis.model.entity.BlockRecord;
import com.gem.loganalysis.model.entity.BlockRule;
import com.gem.loganalysis.service.IBlockRecordService;
import com.gem.loganalysis.service.IBlockRuleService;
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
public class BlockRuleController {

    private final IBlockRuleService iBlockRuleService;

    private final IBlockRecordService iBlockRecordService;

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
        Page<BlockRule> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        LambdaQueryWrapper<BlockRule> wrapper = new LambdaQueryWrapper<>();
        return Result.ok(iBlockRuleService.page(page, wrapper));
    }

    @PostMapping("/edit")
    public Result<Object> edit(@RequestBody BlockRuleDTO dto) {
        boolean result = iBlockRuleService.saveOrUpdate(new BlockRule(dto));
        return result ? Result.ok("编辑成功!") : Result.failed("编辑失败!");
    }

    @PostMapping("/delete")
    public Result<Object> delete(@RequestBody DeleteDTO dto) {
        boolean deleteResult = iBlockRuleService.removeById(dto.getId());
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
        Page<BlockRecord> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        BlockRecordQueryDTO data = dto.getData();
        LambdaQueryWrapperX<BlockRecord> wrapperX = new LambdaQueryWrapperX<>();
        wrapperX.eq(BlockRecord::getBlockState, data.getRecordType())
                .eqIfPresent(BlockRecord::getAssetId, data.getAssetId())
                .eqIfPresent(BlockRecord::getBlockType, data.getBlockType())
                .eqIfPresent(BlockRecord::getBlockMode, data.getBlockMode())
                .eqIfPresent(BlockRecord::getBlockIp, data.getBlockOffIp());
        switch (data.getRecordType()) {
            case 0:
                wrapperX.geIfPresent(BlockRecord::getBlockBeginTime, data.getStartTime())
                        .leIfPresent(BlockRecord::getBlockBeginTime, data.getEndTime());
                break;
            case 1:
                wrapperX.geIfPresent(BlockRecord::getBlockEndTime, data.getStartTime())
                        .leIfPresent(BlockRecord::getBlockEndTime, data.getEndTime());
                break;
            default:
                return Result.failed("参数异常!");
        }
        return Result.ok(iBlockRecordService.page(page, wrapperX));
    }


}
