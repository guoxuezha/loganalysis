package com.gem.loganalysis.kafka;

import com.gem.loganalysis.sshserver.KafkaTempConsumerWebSocket;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.io.IOException;
import java.time.Duration;

/**
 * @author GuoChao
 * @version 1.0
 * @date 2023/6/15 18:18
 */
@Log4j2
@AllArgsConstructor
public class TempConsumerRunnable extends Thread {

    private KafkaTempConsumerWebSocket socket;

    private KafkaConsumer<String, String> consumer;

    @Override
    public void run() {
        while (true) {
            // 从服务器开始拉取数据
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1500));
            try {
                if (records.isEmpty()) {
                    Thread.sleep(5000);
                    continue;
                }
                for (ConsumerRecord<String, String> record : records) {
                    socket.sendMessage(record.value());
                }
            } catch (InterruptedException | IOException e) {
                throw new RuntimeException(e);
            }
            consumer.commitSync();
        }
    }

}
