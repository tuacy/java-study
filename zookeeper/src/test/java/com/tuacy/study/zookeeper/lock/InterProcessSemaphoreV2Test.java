package com.tuacy.study.zookeeper.lock;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.Uninterruptibles;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessSemaphoreV2;
import org.apache.curator.framework.recipes.locks.Lease;
import org.apache.curator.framework.recipes.shared.SharedCount;
import org.apache.curator.framework.recipes.shared.SharedCountReader;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @name: InterProcessSemaphoreV2Test
 * @author: tuacy.
 * @date: 2019/8/15.
 * @version: 1.0
 * @Description:
 */
public class InterProcessSemaphoreV2Test {

    private static final String SEMAPHORE_PATH = "/tuacy/semaphore";
    private static final String SHARED_COUNT = "/tuacy/sharedCount";

    class SemaphoreThread extends Thread {

        private final InterProcessSemaphoreV2 interProcessSemaphoreV2;
        private final CountDownLatch countDownLatch;
        private final int threadIndex;

        SemaphoreThread(InterProcessSemaphoreV2 lockPath, int index, CountDownLatch countDownLatch) {
            this.interProcessSemaphoreV2 = lockPath;
            this.threadIndex = index;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            Lease lease = null;
            try {
                System.out.println(threadIndex + " 线程开始开始获取租约");
                // 获取租约
                lease = interProcessSemaphoreV2.acquire();
                System.out.println(threadIndex + " 线程获取到租约");
                // 等10s
                Uninterruptibles.sleepUninterruptibly(60, TimeUnit.SECONDS);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (lease != null) {
                    System.out.println(threadIndex + " 线程释放租约");
                    interProcessSemaphoreV2.returnLease(lease);
                }
                countDownLatch.countDown();
            }

        }
    }

    @Test
    public void interProcessSemaphoreV2() throws Exception {

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

        // 这里我们所有的客户端都参与leader选举
        for (int index = 0; index < zookeeperClientList.size(); index++) {
            SharedCountReader sharedCountReader = new SharedCount(zookeeperClientList.get(1), SHARED_COUNT, 5);
            InterProcessSemaphoreV2 interProcessSemaphoreV2 = new InterProcessSemaphoreV2(zookeeperClientList.get(1), SEMAPHORE_PATH, sharedCountReader);
            new SemaphoreThread(interProcessSemaphoreV2, index, countDownLatch).start();
        }

        countDownLatch.await();
        zookeeperClientList.forEach(curatorFramework -> {
            // 关闭客户端
            curatorFramework.close();
        });

    }

}
