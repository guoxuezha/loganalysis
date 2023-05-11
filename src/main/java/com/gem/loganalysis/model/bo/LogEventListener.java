package com.gem.loganalysis.model.bo;

import cn.hutool.core.collection.CollUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 事件监听
 *
 * @author GuoChao
 * @version 1.0
 * @date 2023/5/11 12:00
 */
@Component
public class LogEventListener {

    @Resource
    private LogAnalysisRulePool logAnalysisRulePool;

    /**
     * 每5秒扫描一次事件
     */
    @Scheduled(cron = "0/5 * * * * ? ")
    public void loadInStorage() {
        if (CollUtil.isNotEmpty(logAnalysisRulePool.getSafetyEquipmentBoMap())) {
            for (LogAnalysisRule analysisRule : logAnalysisRulePool.getSafetyEquipmentBoMap().values()) {
                analysisRule.scanAndStopEvent();
                analysisRule.scanAndStartEvent();
            }
        }
    }

}
