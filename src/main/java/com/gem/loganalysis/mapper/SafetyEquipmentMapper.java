package com.gem.loganalysis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gem.loganalysis.model.dto.query.AnalysisRuleQueryDTO;
import com.gem.loganalysis.model.entity.Log;
import com.gem.loganalysis.model.entity.SafetyEquipment;
import com.gem.loganalysis.model.vo.EquipAnalysisRuleVO;
import com.gem.loganalysis.model.vo.EquipBlockRecordVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 安全设备 Mapper 接口
 *
 * @author GuoChao
 * @since 2023-05-03
 */
@Mapper
public interface SafetyEquipmentMapper extends BaseMapper<SafetyEquipment> {

    /**
     * 查询安全设备的日志解析规则
     *
     * @param dto 查询条件
     * @return 日志解析规则
     */
    EquipAnalysisRuleVO getEquipAnalysisRuleList(AnalysisRuleQueryDTO dto);

    /**
     * 查询安全设备相关的历史封堵记录
     *
     * @param equipId 设备ID
     * @return 封堵记录
     */
    EquipBlockRecordVO getEquipBlockRecords(Integer equipId);

    /**
     * 查询安全设备归并后的日志记录
     *
     * @param equipId 设备ID
     * @return 日志记录
     */
    List<Log> getEquipLog(Integer equipId);

}
