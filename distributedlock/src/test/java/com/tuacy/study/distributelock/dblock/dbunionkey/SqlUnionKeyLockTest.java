package com.tuacy.study.distributelock.dblock.dbunionkey;

import com.google.common.util.concurrent.Uninterruptibles;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SqlUnionKeyLockTest {

    /**
     * 可重入锁测试
     */
    @Test
    public void unionKeyReentrantLockOneThread() {
        String sourceName = "onThread";
        SqlUnionKeyLock[] keyLocks = new SqlUnionKeyLock[10];
        for (int index = 0; index < keyLocks.length; index++) {
            keyLocks[index] = new SqlUnionKeyLock(sourceName);
        }
        // 加锁
        for (SqlUnionKeyLock keyLock : keyLocks) {
            keyLock.lock();
        }
        // 释放锁
        for (SqlUnionKeyLock keyLock : keyLocks) {
            keyLock.unlock();
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
            SqlUnionKeyLock keyLock = new SqlUnionKeyLock(resourceName);
            System.out.println("线程(" + index + ")" + "开始获取锁");
            keyLock.lock();
            System.out.println("线程(" + index + ")" + "获取锁成功");
            Uninterruptibles.sleepUninterruptibly(300, TimeUnit.MILLISECONDS);
            keyLock.unlock();
            System.out.println("线程(" + index + ")" + "释放锁");
            countDownLatch.countDown();
        }
    }


}
