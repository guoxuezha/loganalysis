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
     * 每分钟扫描一次缓存是否需要持久化
     */
    @Scheduled(cron = "0 0/1 * * * ? ")
    public void cacheFlushScan() {
        if (CollUtil.isNotEmpty(logAnalysisRulePool.getLogAnalysisRuleBoMap())) {
            for (LogAnalysisRuleBo analysisRule : logAnalysisRulePool.getLogAnalysisRuleBoMap().values()) {
                if (analysisRule.isEnable()) {
                    analysisRule.flushCache();
                }
            }
        }
    }

    /**
     * 每30秒扫描一次是否有事件结束
     */
    @Scheduled(cron = "0/30 * * * * ? ")
    public void logEventEndScan() {
        if (CollUtil.isNotEmpty(logAnalysisRulePool.getLogAnalysisRuleBoMap())) {
            for (LogAnalysisRuleBo analysisRule : logAnalysisRulePool.getLogAnalysisRuleBoMap().values()) {
                if (analysisRule.isEnable()) {
                    analysisRule.scanAndStopEvent();
                    // analysisRule.scanAndStartEvent();
                }
            }
        }
    }

}
