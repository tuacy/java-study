package com.tuacy.study.zookeeper.barrier;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.Uninterruptibles;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.barriers.DistributedDoubleBarrier;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @name: DistributedDoubleBarrierTest
 * @author: tuacy.
 * @date: 2019/8/17.
 * @version: 1.0
 * @Description:
 */
public class DistributedDoubleBarrierTest {

    private static final String BARRIER_PATH_COUNTER = "/barrier";

    class LogicThread extends Thread {

        private final CountDownLatch countDownLatch;
        private final int threadIndex;
        private final DistributedDoubleBarrier barrier;

        LogicThread(DistributedDoubleBarrier barrier, int index, CountDownLatch countDownLatch) {
            this.barrier = barrier;
            this.threadIndex = index;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            try {
                Uninterruptibles.sleepUninterruptibly(5 * threadIndex, TimeUnit.SECONDS);
                System.out.println("线程:" + threadIndex + " 请求进入");
                barrier.enter();
                System.out.println("线程:" + threadIndex + " 成功进入");

                System.out.println("线程:" + threadIndex + " 请求离开");
                barrier.leave();
                System.out.println("线程:" + threadIndex + " 成功离开");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                countDownLatch.countDown();
            }

        }
    }

    @Test
    public void distributedDoubleBarrier() throws Exception {
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
        if (zookeeperClientList.get(0).checkExists().forPath(BARRIER_PATH_COUNTER) != null) {
            zookeeperClientList.get(0).delete().forPath(BARRIER_PATH_COUNTER);
        }

        for (int index = 0; index < zookeeperClientList.size(); index++) {
            DistributedDoubleBarrier barrier = new DistributedDoubleBarrier(zookeeperClientList.get(index), BARRIER_PATH_COUNTER, 5);
            new LogicThread(barrier, index, countDownLatch).start();
        }

        countDownLatch.await();
        zookeeperClientList.forEach(curatorFramework -> {
            // 关闭客户端
            curatorFramework.close();
        });
    }

}
