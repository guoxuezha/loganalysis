package com.gem.loganalysis.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author GuoChao
 * @version 1.0
 * @date 2023/4/19 0:27
 */
@RestController
@RequestMapping("/kafka")
public class KafkaProducer {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    // 发送消息
    @PostMapping("/send")
    public void sendMessage1(@RequestParam("topic") String topic, @RequestParam("message") String message) {
        kafkaTemplate.send(topic, message);
    }

}
