package com.gem.loganalysis.kafka;

import com.fasterxml.jackson.databind.deser.std.StringDeserializer;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author GuoChao
 * @version 1.0
 * @date 2023/4/19 0:15
 */
@Configuration
public class KafkaTopicFactory {

    public static Map<String, KafkaConsumerRunnable> KAFKA_CONSUMER_RUNNABLE_POOL = new ConcurrentHashMap<>();

    @Value("${business-config.kafkaMainTopic}")
    private String mainTopic;
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public NewTopic initialTopic() {
        return new NewTopic(mainTopic, 1, (short) 1);
    }

    public Map<String, Object> consumerConfigs() {
        Map<String, Object> propsMap = new HashMap<>();

        propsMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

        propsMap.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);

        propsMap.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 1000);

        propsMap.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 100000);

        propsMap.put(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, 110000);

        propsMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        propsMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        propsMap.put(ConsumerConfig.GROUP_ID_CONFIG, "kafka2");

        propsMap.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        propsMap.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 1000);
        propsMap.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, 5000);
        // 每个批次获取数
        return propsMap;
    }

    @Bean
    public AdminClient getAdminClient() {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return AdminClient.create(properties);
    }

}
