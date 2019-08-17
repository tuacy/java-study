package com.tuacy.study.zookeeper.counter;

import com.google.common.collect.Lists;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.atomic.AtomicValue;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicLong;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryNTimes;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @name: DistributedAtomicLongTest
 * @author: tuacy.
 * @date: 2019/8/16.
 * @version: 1.0
 * @Description:
 */
public class DistributedAtomicLongTest {

    private static final String PATH_COUNTER = "/long/counter";

    class CounterThread extends Thread {

        private final CountDownLatch countDownLatch;
        private final int threadIndex;
        private final DistributedAtomicLong counter;

        CounterThread(DistributedAtomicLong counter, int index, CountDownLatch countDownLatch) {
            this.counter = counter;
            this.threadIndex = index;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            try {
                AtomicValue<Long> value = counter.increment();
                System.out.println("succeed: " + value.succeeded() + " value:" + value.postValue());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                countDownLatch.countDown();
            }

        }
    }

    @Test
    public void distributedAtomicLong() throws Exception {
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

        // 如果节点存在，我们就删除节点
        if (zookeeperClientList.get(0).checkExists().forPath(PATH_COUNTER) != null) {
            zookeeperClientList.get(0).delete().forPath(PATH_COUNTER);
        }

        for (int index = 0; index < zookeeperClientList.size(); index++) {
            // 乐观锁模式
            DistributedAtomicLong count = new DistributedAtomicLong(zookeeperClientList.get(index), PATH_COUNTER, new RetryNTimes(10, 10));
           boolean initializeSuccess =  count.initialize(0L);
            if(initializeSuccess) {
                System.out.println("初始化成功");
            } else {
                System.out.println("初始化失败");
            }
            new CounterThread(count, index, countDownLatch).start();
        }

        countDownLatch.await();
        zookeeperClientList.forEach(curatorFramework -> {
            // 关闭客户端
            curatorFramework.close();
        });
    }

}
