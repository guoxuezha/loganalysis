package com.gem.loganalysis.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gem.loganalysis.model.bo.AssetPreviewLogHandler;
import com.gem.loganalysis.model.bo.LogAnalysisRuleBo;
import com.gem.loganalysis.model.bo.LogAnalysisRulePool;
import com.gem.loganalysis.model.bo.MergeLog;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author GuoChao
 * @version 1.0
 * @date 2023/6/14 18:57
 */
@Component
public class ChildKafkaConsumer {

    @Resource
    private ObjectMapper objectMapper;

    @Resource
    private LogAnalysisRulePool logAnalysisRulePool;

    public void executeLogAnalysis(String record) {
        try {
            MergeLog mergeLog = objectMapper.readValue(record, MergeLog.class);

            // 2023-06-09 新增:保存预览日志
            AssetPreviewLogHandler.getInstance().append(mergeLog);

            LogAnalysisRuleBo logAnalysisRuleBoObject = logAnalysisRulePool.getLogAnalysisRuleObject(mergeLog.getHost(), mergeLog.getFacility(), mergeLog.getSeverity());
            if (logAnalysisRuleBoObject != null && logAnalysisRuleBoObject.isEnable()) {
                logAnalysisRuleBoObject.analysisSourceLog(mergeLog);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
