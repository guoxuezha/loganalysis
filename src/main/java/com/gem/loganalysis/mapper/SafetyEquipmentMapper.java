package com.gem.loganalysis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gem.loganalysis.model.dto.query.AnalysisRuleQueryDTO;
import com.gem.loganalysis.model.entity.Log;
import com.gem.loganalysis.model.entity.SafetyEquipment;
import com.gem.loganalysis.model.vo.EquipAnalysisRuleVO;
import com.gem.loganalysis.model.vo.EquipBlockRecordVO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 安全设备 Mapper 接口
 *
 * @author GuoChao
 * @since 2023-05-03
 */
@Mapper
public interface SafetyEquipmentMapper extends BaseMapper<SafetyEquipment> {

    EquipAnalysisRuleVO getEquipAnalysisRuleList(AnalysisRuleQueryDTO dto);

    EquipBlockRecordVO getEquipBlockRecords(Integer equipId);

    Log getEquipLog(Integer equipId);

}
