package com.gem.loganalysis.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gem.loganalysis.mapper.SafetyEquipmentMapper;
import com.gem.loganalysis.model.PageRequest;
import com.gem.loganalysis.model.dto.query.AnalysisRuleQueryDTO;
import com.gem.loganalysis.model.entity.Log;
import com.gem.loganalysis.model.entity.SafetyEquipment;
import com.gem.loganalysis.model.vo.EquipAnalysisRuleVO;
import com.gem.loganalysis.model.vo.EquipBlockRecordVO;
import com.gem.loganalysis.service.ISafetyEquipmentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 安全设备 服务实现类
 *
 * @author GuoChao
 * @since 2023-05-03
 */
@Service
public class SafetyEquipmentServiceImpl extends ServiceImpl<SafetyEquipmentMapper, SafetyEquipment> implements ISafetyEquipmentService {

    @Resource
    private SafetyEquipmentMapper safetyEquipmentMapper;

    @Override
    public EquipAnalysisRuleVO getEquipAnalysisRuleList(PageRequest<AnalysisRuleQueryDTO> dto) {
        return safetyEquipmentMapper.getEquipAnalysisRuleList(dto.getData());
    }
    @Override
    public EquipBlockRecordVO getEquipBlockRecords(PageRequest<Integer> equipId) {
        return safetyEquipmentMapper.getEquipBlockRecords(equipId.getData());
    }

    @Override
    public Log getEquipLog(PageRequest<Integer> equipId) {
        return safetyEquipmentMapper.getEquipLog(equipId.getData());
    }

}
