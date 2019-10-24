package com.tuacy.kafka.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @name: KafkaTopicConfiguration
 * @author: tuacy.
 * @date: 2019/10/24.
 * @version: 1.0
 * @Description:
 */
@Configuration
public class KafkaTopicConfiguration {

    private final KafkaTopicProperties properties;

    public KafkaTopicConfiguration(KafkaTopicProperties properties) {
        this.properties = properties;
    }

    @Bean
    public String[] kafkaTopicName() {
        return properties.getTopicName();
    }

    @Bean
    public String topicGroupId() {
        return properties.getGroupId();
    }

}
