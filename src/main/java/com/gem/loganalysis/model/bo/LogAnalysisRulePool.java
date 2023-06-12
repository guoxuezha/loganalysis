package com.gem.loganalysis.model.bo;

import cn.hutool.core.collection.CollUtil;
import com.gem.loganalysis.mapper.LogAnalysisRuleRelaMapper;
import lombok.Getter;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * Description: 安全设备池
 * Date: 2023/5/5 21:14
 *
 * @author GuoChao
 **/
@Component
public class LogAnalysisRulePool {

    @Getter
    private final HashMap<String, LogAnalysisRuleBo> logAnalysisRuleBoMap = new HashMap<>();

    @Resource
    private LogAnalysisRuleRelaMapper logAnalysisRuleRelaMapper;

    /**
     * 根据三项信息查询
     *
     * @param ip       IP
     * @param facility 子系统
     * @param severity 日志级别
     * @return 已维护好的日志解析规则对象
     */
    public LogAnalysisRuleBo getLogAnalysisRuleObject(String ip, String facility, String severity) {
        String key = ip + "~" + facility + "~" + severity;
        if (logAnalysisRuleBoMap.get(key) == null) {
            loadAnalysisRuleBO(key);
        }
        return logAnalysisRuleBoMap.get(key);
    }

    /**
     * 加载安全设备信息
     *
     * @param key 业务唯一联合主键
     */
    private void loadAnalysisRuleBO(String key) {
        String[] split = key.split("~");
        HashMap<String, Object> ruleMap = logAnalysisRuleRelaMapper.getEquipAnalysisAndBlockRule(split[0], split[1], split[2]);
        if (CollUtil.isNotEmpty(ruleMap)) {
            LogAnalysisRuleBo equipmentBO = new LogAnalysisRuleBo(ruleMap);
            logAnalysisRuleBoMap.put(key, equipmentBO);
        }
    }


}
