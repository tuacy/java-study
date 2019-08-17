package com.tuacy.study.zookeeper.queue;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.Uninterruptibles;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.queue.SimpleDistributedQueue;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @name: SimpleDistributedQueueTest
 * @author: tuacy.
 * @date: 2019/8/16.
 * @version: 1.0
 * @Description:
 */
public class SimpleDistributedQueueTest {

    private static final String SIMPLE_DISTRIBUTED_QUEUE_PATH = "/SimpleDistributedQueue";

    class QueueActionThread extends Thread {

        private final SimpleDistributedQueue queue;
        private final CountDownLatch countDownLatch;
        private final int queueIndex;

        QueueActionThread(SimpleDistributedQueue queue, int index, CountDownLatch countDownLatch) {
            this.queue = queue;
            this.queueIndex = index;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            try {
                for (int index = 0; index < 5; index++) {
                    String message = "我是队列：" + queueIndex + " 的第-" + index + "-条消息";
                    this.queue.offer(message.getBytes());
                }
                Uninterruptibles.sleepUninterruptibly(5, TimeUnit.SECONDS);
                for (int index = 0; index < 5; index++) {
                    byte[] queueItem = queue.take();
                    System.out.println("我是队列：" + queueIndex + " 我收到了：" + new String(queueItem));
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                countDownLatch.countDown();
            }

        }
    }

    @Test
    public void simpleDistributedQueue() throws Exception {
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
            SimpleDistributedQueue queue = new SimpleDistributedQueue(zookeeperClientList.get(index), SIMPLE_DISTRIBUTED_QUEUE_PATH);
            new QueueActionThread(queue, index, countDownLatch).start();
        }

        countDownLatch.await();
        // 关闭客户端
        zookeeperClientList.forEach(CuratorFramework::close);
    }

}
