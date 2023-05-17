package com.gem.loganalysis.kafka;

import cn.hutool.core.thread.ExecutorBuilder;
import cn.hutool.json.JSONUtil;
import com.gem.loganalysis.model.bo.LogAnalysisRuleBo;
import com.gem.loganalysis.model.bo.LogAnalysisRulePool;
import com.gem.loganalysis.model.bo.MergeLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Description: 消息队列消费者
 *
 * @author GuoChao
 * @version 1.0
 * @date 2023/4/19 0:28
 */
@Component
@Slf4j
public class KafkaConsumer {

    private static final ExecutorService logAnalysisThreadPool = ExecutorBuilder.create()
            .setCorePoolSize(5)
            .setMaxPoolSize(10)
            .setWorkQueue(new LinkedBlockingQueue<>(100))
            .build();
    @Resource
    private LogAnalysisRulePool logAnalysisRulePool;

//    @KafkaListener(topics = {"logrepo4"})
    void onMessage1(String record) {
        List<MergeLog> messageList = convertLogFormat(record);
        // 消费的哪个topic、partition的消息,打印出消息内容
        //log.info("1.监听到的 messageList.size = {}", messageList.size());
        for (MergeLog mergelog : messageList) {
            LogAnalysisRuleBo logAnalysisRuleBoObject = logAnalysisRulePool.getLogAnalysisRuleObject(mergelog.getHost(), mergelog.getFacility(), mergelog.getSeverity());
            if (logAnalysisRuleBoObject != null && logAnalysisRuleBoObject.isEnable()) {
                //logAnalysisRuleObject.printOverview();
                logAnalysisRuleBoObject.insertInCache(mergelog);
            }
        }
        //logAnalysisThreadPool.submit(new LogAnalysisThread(record));
    }

    /**
     * 对象化日志内容
     *
     * @param record 日志记录字符串
     * @return 格式化对象列表
     */
    private List<MergeLog> convertLogFormat(String record) {
        List<MergeLog> result = new ArrayList<>();
        List<HashMap> hashMaps = JSONUtil.toList("[" + record + "]", HashMap.class);
        for (HashMap map : hashMaps) {
            MergeLog mergeLog = new MergeLog();
            mergeLog.setTimestamp((String) map.get("timestamp"));
            mergeLog.setHost((String) map.get("host"));
            mergeLog.setSeverity((String) map.get("severity"));
            mergeLog.setFacility((String) map.get("facility"));
            mergeLog.setMessage((String) map.get("message"));
            mergeLog.setTag((String) map.get("tag"));
            result.add(mergeLog);
        }
        return result;
    }

}
