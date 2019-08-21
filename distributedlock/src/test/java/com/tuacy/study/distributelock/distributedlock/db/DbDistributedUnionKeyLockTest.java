package com.tuacy.study.distributelock.distributedlock.db;

import com.google.common.util.concurrent.Uninterruptibles;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@RunWith(SpringRunner.class)
public class DbDistributedUnionKeyLockTest {

    private IDbDistributedLock dbDistributedLock;

    @Autowired
    public void setDbDistributedLock(IDbDistributedLock dbDistributedLock) {
        this.dbDistributedLock = dbDistributedLock;
    }

    /**
     * 可重入锁测试
     */
    @Test
    public void unionKeyReentrantLockOneThread() {
        String sourceName = "onThread";
        // 加锁
        for (int index = 0; index < 10; index++) {
            dbDistributedLock.lock(sourceName);
        }
        // 释放锁
        for (int index = 0; index < 10; index++) {
            dbDistributedLock.unlock(sourceName);
        }

    }

    /**
     * 开多个线程去获取锁
     */
    @Test
    public void unionKeyReentrantLockMultiThread() {
        final String sourceName = "multiThread";
        CountDownLatch countDownLatch = new CountDownLatch(10);
        for (int index = 0; index < countDownLatch.getCount(); index++) {
            new LockThread(countDownLatch, index, sourceName).start();
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private class LockThread extends Thread {
        private final int index;
        private final String resourceName;
        private final CountDownLatch countDownLatch;

        LockThread(CountDownLatch countDownLatch, int index, String resourceName) {
            this.index = index;
            this.resourceName = resourceName;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            System.out.println("线程(" + index + ")" + "开始获取锁");
            dbDistributedLock.lock(resourceName);
            System.out.println("线程(" + index + ")" + "获取锁成功");
            Uninterruptibles.sleepUninterruptibly(300, TimeUnit.MILLISECONDS);
            dbDistributedLock.unlock(resourceName);
            System.out.println("线程(" + index + ")" + "释放锁");
            countDownLatch.countDown();
        }
    }


}
