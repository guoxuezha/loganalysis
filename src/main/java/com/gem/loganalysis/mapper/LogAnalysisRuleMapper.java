package com.gem.loganalysis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gem.loganalysis.model.entity.LogAnalysisRule;
import org.apache.ibatis.annotations.Mapper;

/**
 * 日志解析规则 Mapper 接口
 *
 * @author GuoChao
 * @since 2023-05-03
 */
@Mapper
public interface LogAnalysisRuleMapper extends BaseMapper<LogAnalysisRule> {

}
