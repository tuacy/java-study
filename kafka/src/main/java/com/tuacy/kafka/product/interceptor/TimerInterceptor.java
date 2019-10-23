package com.tuacy.kafka.product.interceptor;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Map;

/**
 * @name: TimerInterceptor
 * @author: tuacy.
 * @date: 2019/10/23.
 * @version: 1.0
 * @Description:
 */
public class TimerInterceptor implements ProducerInterceptor {
    /**
     * 该方法封装进 KafkaProducer.send 方法中，即它运行在用户主线程中。Producer 确保在
     * 消息被序列化以及计算分区前调用该方法
     * 用户可以在该方法中对消息做任何操作，但最好保证不要修改消息所属的 topic 和分区，否则会影响目标分区的计算
     */
    @Override
    public ProducerRecord onSend(ProducerRecord record) {
        // 创建一个新的record，将时间戳写入记录的最前面,一个record就是一个记录
        return new ProducerRecord<>(
                record.topic(),
                record.partition(),
                record.timestamp(),
                record.key(),
                System.currentTimeMillis() + " - " + record.value().toString());
    }

    /**
     * 该方法会在消息被应答或消息发送失败时调用，并且通常都是在 producer 回调逻辑触
     * 发之前。onAcknowledgement 运行在 producer 的 IO 线程中，因此不要在该方法中放入很重的逻辑，
     * 否则会拖慢 producer 的消息发送效率
     */
    @Override
    public void onAcknowledgement(RecordMetadata metadata, Exception exception) {

    }

    /**
     * 关闭 interceptor，主要用于执行一些资源清理工作
     */
    @Override
    public void close() {

    }

    /**
     * 获取配置信息和初始化数据时调用
     */
    @Override
    public void configure(Map<String, ?> configs) {

    }
}
