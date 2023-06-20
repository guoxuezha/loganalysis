package com.gem.loganalysis.kafka;

import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gem.loganalysis.config.BusinessConfigInfo;
import com.gem.loganalysis.model.bo.MergeLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Description: 消息队列消费者
 *
 * @author GuoChao
 * @version 1.0
 * @date 2023/4/19 0:28
 */
@Component
@Slf4j
public class MainKafkaConsumer {

    @Resource
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Resource
    private BusinessConfigInfo businessConfigInfo;

    @Resource
    private KafkaAutoTableHandler kafkaAutoTableHandler;

    @Resource
    private ObjectMapper objectMapper;

    @KafkaListener(topics = {"${business-config.kafkaMainTopic}"})
    void onMessage(String record) {
        if (businessConfigInfo.getLogMonitorEnable()) {
            List<MergeLog> messageList = convertLogFormat(record);
            for (MergeLog mergelog : messageList) {
                // 根据IP分发到不同的Topic下
                String host = mergelog.getHost();
                NewTopic topic = kafkaAutoTableHandler.getTopic(host);
                try {
                    kafkaTemplate.send(topic.name(), objectMapper.writeValueAsString(mergelog));
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        }
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
