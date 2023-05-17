package com.gem.loganalysis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gem.loganalysis.model.bo.MergeLog;
import com.gem.loganalysis.model.dto.query.AnalysisRuleQueryDTO;
import com.gem.loganalysis.model.entity.LogAnalysisRuleRela;
import com.gem.loganalysis.model.vo.AssetAnalysisRuleVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

/**
 * 日志解析规则映射关系 Mapper 接口
 *
 * @author GuoChao
 * @since 2023-05-10
 */
@Mapper
public interface LogAnalysisRuleRelaMapper extends BaseMapper<LogAnalysisRuleRela> {

    /**
     * 根据资产ID查询日志解析规则
     *
     * @param dto 查询参数
     * @return 日志解析规则列表
     */
    List<AssetAnalysisRuleVO> getAnalysisRules(AnalysisRuleQueryDTO dto);

    /**
     * 根据资产ID查询日志记录列表
     *
     * @param assetId 资产ID
     * @return 日志记录列表
     */
    List<MergeLog> getLogRecordsByAsset(String assetId);

    /**
     * 根据解析规则ID查询日志列表
     *
     * @param ruleRelaId 日志解析规则ID
     * @return 日志记录列表
     */
    List<MergeLog> getLogRecordsByRuleRela(String ruleRelaId);

    /**
     * 查询指定IP、日志子系统、优先级的的设备解析及阻塞规则
     *
     * @param ip       IP
     * @param facility 子系统
     * @param severity 优先级
     * @return 设备解析及阻塞规则
     */
    HashMap<String, Object> getEquipAnalysisAndBlockRule(@Param("ip") String ip, @Param("facility") String facility, @Param("severity") String severity);

}
