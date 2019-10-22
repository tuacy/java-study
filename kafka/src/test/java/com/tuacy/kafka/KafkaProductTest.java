package com.tuacy.kafka;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @name: KafkaProductTest
 * @author: tuacy.
 * @date: 2019/10/22.
 * @version: 1.0
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest()
public class KafkaProductTest {

    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public void setKafkaTemplate(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Test
    public void sendKafkaMessage() {
        // 发送Kafka消息
        kafkaTemplate.send("test1", "hello kafka");

        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
