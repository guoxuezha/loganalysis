package com.gem.loganalysis.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gem.loganalysis.model.PageRequest;
import com.gem.loganalysis.model.Result;
import com.gem.loganalysis.model.dto.query.BlockRecordQueryDTO;
import com.gem.loganalysis.model.dto.query.LambdaQueryWrapperX;
import com.gem.loganalysis.model.entity.BlockRecord;
import com.gem.loganalysis.service.IBlockRecordService;
import com.gem.loganalysis.service.IBlockRuleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.gem.loganalysis.util.CustomDateFormatUtil.format;

/**
 * 封堵规则 前端控制器
 *
 * @author GuoChao
 * @since 2023-05-03
 */
@Api(tags = "封堵管理")
@RestController
@RequestMapping("/logAnalysis/blockRule")
@AllArgsConstructor
public class BlockRuleController {

    private final IBlockRuleService iBlockRuleService;

    private final IBlockRecordService iBlockRecordService;

    /**
     * 分页查询封堵记录
     *
     * @param dto 分页查询参数
     * @return 封堵记录列表
     */
    @ApiOperation("分页查询封堵记录")
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
            case 1:
                wrapperX.geIfPresent(BlockRecord::getBlockBeginTime, format(data.getStartTime()))
                        .leIfPresent(BlockRecord::getBlockBeginTime, format(data.getEndTime()));
                break;
            case 2:
                wrapperX.geIfPresent(BlockRecord::getBlockEndTime, format(data.getStartTime()))
                        .leIfPresent(BlockRecord::getBlockEndTime, format(data.getEndTime()));
                break;
            default:
                return Result.failed("参数异常!");
        }
        return Result.ok(iBlockRecordService.page(page, wrapperX));
    }


}
