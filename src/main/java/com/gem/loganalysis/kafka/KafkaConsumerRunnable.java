package com.gem.loganalysis.kafka;

/**
 * @author GuoChao
 * @version 1.0
 * @date 2023/6/14 18:41
 */

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;

/***
 *
 * @author wangxiaobo
 *
 */
@Log4j2
@Data
public class KafkaConsumerRunnable extends Thread {

    private Map<String, Object> consumerConfigs;

    private String topicName;

    private String groupId;

    private ChildKafkaConsumer childKafkaConsumer;

    @Override
    public void run() {
        log.info("注册开始KafkaConfigRunnable {} ", topicName);
        if (StringUtils.isNotBlank(groupId)) {
            consumerConfigs.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        }
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerConfigs);

        consumer.subscribe(Collections.singletonList(topicName));
        try {
            while (true) {
                // 从服务器开始拉取数据
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1500));
                if (records.isEmpty()) {
                    Thread.sleep(5000);
                    continue;
                }

                for (ConsumerRecord<String, String> record : records) {
                    childKafkaConsumer.executeLogAnalysis(record.value());
                }
                consumer.commitSync();
            }
        } catch (Exception e) {
            log.error("KafkaConfigRunnable 消费异常:{} ", topicName, e);
        } finally {
            // 异常先关闭
            consumer.close();
            KafkaTopicFactory.removeConsumer(topicName);
        }
    }
}

