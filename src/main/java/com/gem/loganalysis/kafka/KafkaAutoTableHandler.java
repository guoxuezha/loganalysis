package com.gem.loganalysis.kafka;

import com.gem.loganalysis.model.BaseConstant;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.KafkaFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

/**
 * @author GuoChao
 * @version 1.0
 * @date 2023/6/14 18:45
 */
@Component
public class KafkaAutoTableHandler {

    public static Map<String, NewTopic> TOPIC_MAP = new ConcurrentHashMap<>();

    @Autowired
    private AdminClient adminClient;

    @Autowired
    private KafkaTopicFactory kafkaTopicFactory;

    @Autowired
    private ChildKafkaConsumer childKafkaConsumer;

    /**
     * 初始化Topic监听线程和newTopic对象缓存
     */
    public void initTopicListener() {
        ListTopicsResult result = adminClient.listTopics();
        KafkaFuture<Set<String>> names = result.names();
        try {
            for (String topic : names.get()) {
                if (topic.startsWith(BaseConstant.CHILD_TOPIC_PREFIX)) {
                    // 判断子主题监听线程是否创建
                    if (!kafkaTopicFactory.exitConsumer(topic)) {
                        KafkaConsumerRunnable consumerRunnable = new KafkaConsumerRunnable();
                        consumerRunnable.setConsumerConfigs(kafkaTopicFactory.consumerConfigs());
                        consumerRunnable.setTopicName(topic);
                        consumerRunnable.setChildKafkaConsumer(childKafkaConsumer);
                        kafkaTopicFactory.appendConsumer(topic, consumerRunnable);
                        consumerRunnable.start();
                    }
                    // 判断newTopic对象是否维护
                    String host = topic.split(BaseConstant.CHILD_TOPIC_PREFIX)[1];
                    if (TOPIC_MAP.get(host) == null) {
                        String topicName = BaseConstant.CHILD_TOPIC_PREFIX + host;
                        NewTopic newTopic = new NewTopic(topicName, 2, (short) 1);
                        TOPIC_MAP.put(host, newTopic);
                    }
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据IP查询newTopic对象,若目标Topic不存在,则执行创建,并初始化监听线程
     *
     * @param host IP
     * @return newTopic对象
     */
    public NewTopic getTopic(String host) {
        if (TOPIC_MAP.get(host) == null) {
            String topicName = BaseConstant.CHILD_TOPIC_PREFIX + host;
            NewTopic newTopic = new NewTopic(topicName, 2, (short) 1);
            TOPIC_MAP.put(host, newTopic);
            initTopicListener();
        }
        return TOPIC_MAP.get(host);
    }

}
