package com.tuacy.study.zookeeper.lock;

import com.google.common.util.concurrent.Uninterruptibles;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @name: InterProcessMutexTest
 * @author: tuacy.
 * @date: 2019/8/14.
 * @version: 1.0
 * @Description:
 */
public class InterProcessMutexTest {

    private static final String LOCK_PATH = "/tuacy/interProcessMutexTest";

    class LockThread extends Thread {

        private final CuratorFramework client;
        private final CountDownLatch countDownLatch;
        private final int threadIndex;
        private final String lockPath;

        LockThread(String lockPath, int index, CountDownLatch countDownLatch) {
            this.lockPath = lockPath;
            this.threadIndex = index;
            this.countDownLatch = countDownLatch;
            RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
            client = CuratorFrameworkFactory.builder()
                    .connectString("127.0.0.1:2181")
                    .retryPolicy(retryPolicy)
                    .sessionTimeoutMs(6000)
                    .connectionTimeoutMs(6000)
                    .build();
            client.start();
        }

        @Override
        public void run() {
            try {
                InterProcessMutex interProcessMutex = new InterProcessMutex(client, lockPath);
                System.out.println(threadIndex + " 线程开始获取锁");
                // 获取锁
                interProcessMutex.acquire();
                System.out.println(threadIndex + " 线程获取到锁");
                // 等10s
                Uninterruptibles.sleepUninterruptibly(10, TimeUnit.SECONDS);
                System.out.println(threadIndex + " 线程释放锁");
                // 释放锁
                interProcessMutex.release();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                client.close();
                countDownLatch.countDown();
            }

        }
    }

    @Test
    public void leaderLatch() throws Exception {

        CountDownLatch countDownLatch = new CountDownLatch(10);

        for (int index = 0; index < 10; index++) {
            new LockThread(LOCK_PATH, index, countDownLatch).start();
        }

        countDownLatch.await();

    }

}
