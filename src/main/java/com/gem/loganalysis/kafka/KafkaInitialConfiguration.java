package com.gem.loganalysis.kafka;

import org.springframework.context.annotation.Configuration;

/**
 * @author GuoChao
 * @version 1.0
 * @date 2023/4/19 0:15
 */
@Configuration
public class KafkaInitialConfiguration {

    // 创建一个名为testtopic的Topic并设置分区数为4，分区副本数为2
    /*@Bean
    public NewTopic initialTopic() {
        return new NewTopic("logrepo4", 2, (short) 2);
    }*/

    // 如果要修改分区数，只需修改配置值重启项目即可
    // 修改分区数并不会导致数据的丢失，但是分区数只能增大不能减小
    /*@Bean
    public NewTopic updateTopic() {
        return new NewTopic("logrepo", 4, (short) 2);
    }*/

}
