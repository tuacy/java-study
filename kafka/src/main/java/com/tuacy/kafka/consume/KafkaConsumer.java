package com.tuacy.kafka.consume;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @name: KafkaConsumer
 * @author: tuacy.
 * @date: 2019/10/22.
 * @version: 1.0
 * @Description: Kafka接收消息
 */
@Component
@Slf4j
public class KafkaConsumer {

    @KafkaListener(topics = {"test1"})
    public void listen(ConsumerRecord<String, String> record) {

        Optional<String> kafkaMessage = Optional.ofNullable(record.value());
        if (kafkaMessage.isPresent()) {
            String message = kafkaMessage.get();
            System.out.println("----------------- record = " + record);
            System.out.println("------------------ message = " + message);
        }
    }

}
