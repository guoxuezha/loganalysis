package com.gem.loganalysis.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gem.loganalysis.model.PageRequest;
import com.gem.loganalysis.model.Result;
import com.gem.loganalysis.model.dto.DeleteDTO;
import com.gem.loganalysis.model.dto.edit.SafetyEquipmentDTO;
import com.gem.loganalysis.model.dto.query.BlockRuleQueryDTO;
import com.gem.loganalysis.model.dto.query.EquipmentQueryDTO;
import com.gem.loganalysis.model.entity.BlockOffRule;
import com.gem.loganalysis.model.entity.SafetyEquipment;
import com.gem.loganalysis.service.IBlockOffRuleService;
import com.gem.loganalysis.service.ISafetyEquipmentService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 安全设备 前端控制器
 *
 * @author GuoChao
 * @since 2023-05-03
 */
@RestController
@RequestMapping("/logAnalysis/equipment")
@AllArgsConstructor
public class SafetyEquipmentController {

    private final ISafetyEquipmentService iSafetyEquipmentService;


    /**
     * 分页查询安全设备基本信息
     *
     * @param dto 查询条件
     * @return 安全设备列表
     */
    @PostMapping("/pageList")
    public Result<Object> pageList(@RequestBody PageRequest<EquipmentQueryDTO> dto) {
        Page<SafetyEquipment> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        EquipmentQueryDTO queryDTO = dto.getData();
        LambdaQueryWrapper<SafetyEquipment> wrapper = new LambdaQueryWrapper<>();
        if (queryDTO != null) {
            if (StrUtil.isNotEmpty(queryDTO.getOrgId())) {
                wrapper.eq(SafetyEquipment::getPort, queryDTO.getOrgId());
            }
            if (StrUtil.isNotEmpty(queryDTO.getPort())) {
                wrapper.eq(SafetyEquipment::getPort, queryDTO.getPort());
            }
            if (StrUtil.isNotEmpty(queryDTO.getEquipType())) {
                wrapper.eq(SafetyEquipment::getPort, queryDTO.getEquipType());
            }
            if (StrUtil.isNotEmpty(queryDTO.getIp())) {
                wrapper.like(SafetyEquipment::getPort, queryDTO.getIp());
            }
            if (StrUtil.isNotEmpty(queryDTO.getEquipName())) {
                wrapper.like(SafetyEquipment::getPort, queryDTO.getEquipName());
            }
        }
        return Result.ok(iSafetyEquipmentService.page(page, wrapper));
    }

    /**
     * 新增/修改安全设备
     *
     * @param dto 设备基本信息
     * @return 编辑结果
     */
    @PostMapping("/edit")
    public Result<Object> edit(@RequestBody SafetyEquipmentDTO dto) {
        boolean result = iSafetyEquipmentService.saveOrUpdate(new SafetyEquipment(dto));
        return result ? Result.ok("编辑成功!") : Result.failed("编辑失败!");
    }

    /**
     * 删除安全设备
     *
     * @param dto 设备ID
     * @return 删除结果
     */
    @PostMapping("/delete")
    public Result<Object> delete(@RequestBody DeleteDTO dto) {
        boolean deleteResult = iSafetyEquipmentService.removeById(dto.getId());
        return deleteResult ? Result.ok("删除成功!") : Result.failed("删除失败!");
    }

    /**
     * 分页查询设备的日志解析规则
     *
     * @param dto 分页查询参数
     * @return 日志解析规则列表
     */
    @PostMapping("/getAnalysisRules")
    public Result<Object> getAnalysisRules(@RequestBody PageRequest<Integer> dto) {

        return Result.ok();
    }

    /**
     * 分页查询设备的日志记录
     *
     * @param dto 分页查询参数
     * @return 设备日志列表
     */
    @PostMapping("/getLogRecords")
    public Result<Object> getLogRecords(@RequestBody PageRequest<Integer> dto) {

        return Result.ok();
    }


}
