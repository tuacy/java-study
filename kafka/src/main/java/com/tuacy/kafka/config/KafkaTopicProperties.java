package com.tuacy.kafka.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @name: KafkaTopicProperties
 * @author: tuacy.
 * @date: 2019/10/24.
 * @version: 1.0
 * @Description:
 */
@ConfigurationProperties(prefix = "kafka.topic")
public class KafkaTopicProperties {

    private String groupId;
    private String[] topicName;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String[] getTopicName() {
        return topicName;
    }

    public void setTopicName(String[] topicName) {
        this.topicName = topicName;
    }

}
