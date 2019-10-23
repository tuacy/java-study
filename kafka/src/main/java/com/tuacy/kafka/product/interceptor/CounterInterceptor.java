package com.tuacy.kafka.product.interceptor;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Map;

/**
 * @name: CounterInterceptor
 * @author: tuacy.
 * @date: 2019/10/23.
 * @version: 1.0
 * @Description: 统计发送消息成功和发送失败消息数，并在 producer 关闭时打印这两个计数器
 */
public class CounterInterceptor implements ProducerInterceptor {

    private int successCount = 0;
    private int errorCount = 0;

    @Override
    public ProducerRecord onSend(ProducerRecord record) {
        return record;
    }

    @Override
    public void onAcknowledgement(RecordMetadata metadata, Exception exception) {
        // 统计失败和成功的次数
        if (exception == null) {
            successCount++;
        } else {
            errorCount++;
        }
    }

    @Override
    public void close() {
        System.out.println("success sent: " + successCount);
        System.out.println("error sent: " + errorCount);
    }

    @Override
    public void configure(Map<String, ?> configs) {

    }
}
