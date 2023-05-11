package com.gem.loganalysis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gem.loganalysis.model.entity.LogAnalysisRuleRela;
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
     * 查询指定IP、日志子系统、优先级的的设备解析及阻塞规则
     *
     * @param ip       IP
     * @param facility 子系统
     * @param severity 优先级
     * @return 设备解析及阻塞规则
     */
    HashMap<String, Object> getEquipAnalysisAndBlockRule(@Param("ip") String ip,
                                                         @Param("facility") String facility,
                                                         @Param("severity") String severity);

    /**
     * 查询日志解析规则对象列表
     *
     * @return list
     */
    List<HashMap<String, String>> getEquipAnalysisAndBlockRuleList();

}
