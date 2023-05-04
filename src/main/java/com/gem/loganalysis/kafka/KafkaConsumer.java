package com.gem.loganalysis.kafka;

import cn.hutool.json.JSONUtil;
import com.gem.loganalysis.facility.Facility;
import com.gem.loganalysis.facility.Honeypot;
import com.gem.loganalysis.facility.PoisonProofWall;
import com.gem.loganalysis.facility.Probe;
import com.gem.loganalysis.model.bo.RsyslogNormalMessage;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Description: 消息队列消费者
 *
 * @author GuoChao
 * @version 1.0
 * @date 2023/4/19 0:28
 */
@Component
public class KafkaConsumer {

    //@KafkaListener(topics = {"remotelog04"})
    void onMessage1(String record) {
        List<RsyslogNormalMessage> messageList = JSONUtil.toList("[" + record + "]", RsyslogNormalMessage.class);
        // 消费的哪个topic、partition的消息,打印出消息内容
        for (RsyslogNormalMessage rsyslogNormalMessage : messageList) {
            shunt(rsyslogNormalMessage);
        }
    }

    /**
     * 根据日志的IP、发生系统 (和优先级) 对日志进行分流
     *
     * @param message 日志消息标准对象
     */
    private void shunt(RsyslogNormalMessage message) {
        Facility facility;
        switch (message.getHost()) {
            case "172.16.200.73":
                facility = Probe.getInstance();
                break;
            case "172.16.200.77":
                facility = PoisonProofWall.getInstance();
                break;
            case "222.95.84.169":
                facility = Honeypot.getInstance();
                break;
            default:
                facility = null;
                break;
        }
        if (facility != null) {
            facility.insertInCache(message);
        }
    }


}
