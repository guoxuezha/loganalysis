package com.gem.loganalysis.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gem.loganalysis.mapper.LogAnalysisRuleMapper;
import com.gem.loganalysis.model.entity.LogAnalysisRule;
import com.gem.loganalysis.service.ILogAnalysisRuleService;
import org.springframework.stereotype.Service;

/**
 * 日志解析规则 服务实现类
 *
 * @author GuoChao
 * @since 2023-05-03
 */
@Service
public class LogAnalysisRuleServiceImpl extends ServiceImpl<LogAnalysisRuleMapper, LogAnalysisRule> implements ILogAnalysisRuleService {

}
