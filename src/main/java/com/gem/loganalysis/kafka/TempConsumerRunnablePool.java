package com.gem.loganalysis.kafka;

import com.gem.loganalysis.sshserver.KafkaTempConsumerWebSocket;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author GuoChao
 * @version 1.0
 * @date 2023/6/15 18:26
 */
@Component
public class TempConsumerRunnablePool {

    private static final ConcurrentHashMap<String, TempConsumerRunnable> consumerPool = new ConcurrentHashMap<>();

    private static final String tempConsumerGroupId = "tempConsumer";

    @Resource
    private KafkaTopicFactory kafkaTopicFactory;

    public TempConsumerRunnable get(String topicName, KafkaTempConsumerWebSocket socket) {
        if (consumerPool.get(topicName) == null) {
            append(topicName, socket);
        }
        return consumerPool.get(topicName);
    }

    private void append(String topicName, KafkaTempConsumerWebSocket socket) {
        // 判断是否存在目标Topic
        if (kafkaTopicFactory.exitConsumer(topicName)) {
            // 创建基于目标Topic的消费者,groupId为 "tempConsumer"
            Map<String, Object> consumerConfigs = kafkaTopicFactory.consumerConfigs();
            consumerConfigs.put(ConsumerConfig.GROUP_ID_CONFIG, tempConsumerGroupId);
            KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerConfigs);
            consumer.subscribe(Collections.singletonList(topicName));

            // 创建线程执行消息消费和sendMessage
            TempConsumerRunnable runnable = new TempConsumerRunnable(socket, consumer);
            consumerPool.put(topicName, runnable);
        }
    }

}
