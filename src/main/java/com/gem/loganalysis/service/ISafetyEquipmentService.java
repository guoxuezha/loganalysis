package com.gem.loganalysis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gem.loganalysis.model.PageRequest;
import com.gem.loganalysis.model.dto.query.AnalysisRuleQueryDTO;
import com.gem.loganalysis.model.entity.Log;
import com.gem.loganalysis.model.entity.SafetyEquipment;
import com.gem.loganalysis.model.vo.EquipAnalysisRuleVO;
import com.gem.loganalysis.model.vo.EquipBlockRecordVO;

import java.util.List;

/**
 * 安全设备 服务类
 *
 * @author GuoChao
 * @since 2023-05-03
 */
public interface ISafetyEquipmentService extends IService<SafetyEquipment> {

    EquipAnalysisRuleVO getEquipAnalysisRuleList(PageRequest<AnalysisRuleQueryDTO> dto);

    EquipBlockRecordVO getEquipBlockRecords(PageRequest<Integer> equipId);

    List<Log> getEquipLog(PageRequest<Integer> equipId);

}
