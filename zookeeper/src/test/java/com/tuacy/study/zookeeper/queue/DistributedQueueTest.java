package com.tuacy.study.zookeeper.queue;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.Uninterruptibles;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.queue.DistributedQueue;
import org.apache.curator.framework.recipes.queue.QueueBuilder;
import org.apache.curator.framework.recipes.queue.QueueConsumer;
import org.apache.curator.framework.recipes.queue.QueueSerializer;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @name: DistributedQueueTest
 * @author: tuacy.
 * @date: 2019/8/16.
 * @version: 1.0
 * @Description:
 */
public class DistributedQueueTest {

    private static final String DISTRIBUTED_QUEUE_PATH = "/queue/distributedQueue";

    class QueueActionThread extends Thread {

        private final DistributedQueue<String> queue;
        private final CountDownLatch countDownLatch;
        private final int queueIndex;

        QueueActionThread(DistributedQueue<String> queue, int index, CountDownLatch countDownLatch) {
            this.queue = queue;
            this.queueIndex = index;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            try {
                this.queue.start();
                for (int index = 0; index < 5; index++) {
                    queue.put("队列 " + queueIndex + " 发来的消息：" + index);
                    Uninterruptibles.sleepUninterruptibly(5, TimeUnit.SECONDS);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                countDownLatch.countDown();
            }

        }
    }



    @Test
    public void distributedQueue() throws Exception {

        CountDownLatch countDownLatch = new CountDownLatch(10);
        List<CuratorFramework> zookeeperClientList = Lists.newArrayList();

        // 启动10个zookeeper客户端
        for (int index = 0; index < 10; index++) {
            RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
            CuratorFramework client = CuratorFrameworkFactory.builder()
                    .connectString("127.0.0.1:2181")
                    .retryPolicy(retryPolicy)
                    .sessionTimeoutMs(6000)
                    .connectionTimeoutMs(6000)
                    .build();
            // 启动客户端
            client.start();
            zookeeperClientList.add(client);
        }

        for (int index = 0; index < zookeeperClientList.size(); index++) {
            QueueBuilder<String> queueBuild = QueueBuilder.builder(zookeeperClientList.get(index), index % 2 == 0 ? new ConsumerImp(index + "") : null, createQueueSerializer(), DISTRIBUTED_QUEUE_PATH);
            DistributedQueue<String> queue = queueBuild.buildQueue();
            new QueueActionThread(queue, index, countDownLatch).start();
        }

        countDownLatch.await();
        // 关闭客户端
        zookeeperClientList.forEach(CuratorFramework::close);

    }

    /**
     * 队列消息序列化实现类
     */
    private static QueueSerializer<String> createQueueSerializer() {
        return new QueueSerializer<String>() {
            @Override
            public byte[] serialize(String item) {
                return item.getBytes();
            }

            @Override
            public String deserialize(byte[] bytes) {
                return new String(bytes);
            }
        };
    }

    private class ConsumerImp implements QueueConsumer<String>{

        private String consumerName;

        public ConsumerImp(String consumerName) {
            this.consumerName = consumerName;
        }

        @Override
        public void consumeMessage(String message) throws Exception {
            System.out.println(consumerName + " 收到消息： " + message);
        }

        @Override
        public void stateChanged(CuratorFramework client, ConnectionState newState) {

        }
    }

}
