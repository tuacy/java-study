package com.tuacy.study.distributelock.distributedlock.db;

import com.google.common.util.concurrent.Uninterruptibles;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @name: SqlExclusiveLockTest
 * @author: tuacy.
 * @date: 2019/8/6.
 * @version: 1.0
 * @Description:
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class SqlExclusiveLockTest {

    @Test
    public void testLock() {

        final String sourceName = "uuid";
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
            SqlExclusiveLock keyLock = new SqlExclusiveLock(resourceName);
            System.out.println("线程(" + index + ")" + "开始获取锁");
            keyLock.lock();
            System.out.println("线程(" + index + ")" + "获取锁成功");
            Uninterruptibles.sleepUninterruptibly(10, TimeUnit.SECONDS);
            keyLock.unlock();
            System.out.println("线程(" + index + ")" + "释放锁");
            countDownLatch.countDown();
        }
    }

}
