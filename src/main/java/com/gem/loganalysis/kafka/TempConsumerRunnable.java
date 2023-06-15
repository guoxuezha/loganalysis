package com.gem.loganalysis.kafka;

import cn.hutool.core.collection.CollUtil;
import com.gem.loganalysis.sshserver.KafkaTempConsumerWebSocket;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.Map;

/**
 * @author GuoChao
 * @version 1.0
 * @date 2023/6/15 18:18
 */
@Log4j2
@AllArgsConstructor
public class TempConsumerRunnable extends Thread {

    private final KafkaTempConsumerWebSocket socket;
    private Map<String, Object> consumerConfigs;
    private String topicName;
    private String groupId;

    @Override
    public void run() {
        consumerConfigs.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerConfigs);
        consumer.subscribe(Collections.singletonList(topicName));
        while (true) {
            // 从服务器开始拉取数据
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1500));
            try {
                if (CollUtil.isEmpty(records)) {
                    sleep(5000);
                    continue;
                }
                for (ConsumerRecord<String, String> record : records) {
                    synchronized (socket) {
                        socket.getSession().getBasicRemote().sendText(record.value());
                    }
                }
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
            consumer.commitSync();
        }
    }

}
