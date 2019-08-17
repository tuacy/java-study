package com.tuacy.study.zookeeper.barrier;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.Uninterruptibles;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.barriers.DistributedBarrier;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @name: DistributedBarrierTest
 * @author: tuacy.
 * @date: 2019/8/17.
 * @version: 1.0
 * @Description:
 */
public class DistributedBarrierTest {

    private static final String BARRIER_PATH_COUNTER = "/barrier";

    class LogicThread extends Thread {

        private final CountDownLatch countDownLatch;
        private final int threadIndex;
        private final DistributedBarrier barrier;

        LogicThread(DistributedBarrier barrier, int index, CountDownLatch countDownLatch) {
            this.barrier = barrier;
            this.threadIndex = index;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            try {
                System.out.println("线程: " + threadIndex + " 请求进入");
                // 阻塞等待
                barrier.waitOnBarrier();
                System.out.println("线程: " + threadIndex + " 成功进入");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                countDownLatch.countDown();
            }

        }
    }

    @Test
    public void distributedBarrier() throws Exception {

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
        DistributedBarrier controlBarrier = new DistributedBarrier(zookeeperClientList.get(0), BARRIER_PATH_COUNTER);
        controlBarrier.setBarrier();
        for (int index = 0; index < zookeeperClientList.size(); index++) {
            DistributedBarrier barrier = new DistributedBarrier(zookeeperClientList.get(index), BARRIER_PATH_COUNTER);
            new LogicThread(barrier, index, countDownLatch).start();
        }

        Uninterruptibles.sleepUninterruptibly(30, TimeUnit.SECONDS);
        controlBarrier.removeBarrier();
        countDownLatch.await();
        zookeeperClientList.forEach(curatorFramework -> {
            // 关闭客户端
            curatorFramework.close();
        });

    }

}
