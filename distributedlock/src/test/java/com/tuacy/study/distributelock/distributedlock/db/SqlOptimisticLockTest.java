package com.tuacy.study.distributelock.distributedlock.db;

import com.tuacy.study.distributelock.distributedlock.db.optimistic.SqlOptimisticLock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CountDownLatch;

/**
 * @name: SqlOptimisticLockTest
 * @author: tuacy.
 * @date: 2019/8/6.
 * @version: 1.0
 * @Description:
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class SqlOptimisticLockTest {

    /**
     * 开多个线程去存钱
     */
    @Test
    public void optimisticLockLockMultiThread() {
        final String sourceName = "multiThread";
        CountDownLatch countDownLatch = new CountDownLatch(10);
        for (int index = 0; index < countDownLatch.getCount(); index++) {

            new Thread(() -> {
                SqlOptimisticLock sqlOptimisticLock = new SqlOptimisticLock(sourceName);
                sqlOptimisticLock.depositMoney(10);
                countDownLatch.countDown();
            }).start();

        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
