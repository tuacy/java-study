package com.tuacy.study.lock;

import com.google.common.util.concurrent.Uninterruptibles;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @name: CountDownLatchTest
 * @author: tuacy.
 * @date: 2019/7/31.
 * @version: 1.0
 * @Description:
 */
public class CountDownLatchTest {

    @Test
    public void countDownLatch() {
        // CountDownLatch 初始值给10
        CountDownLatch countDownLatch = new CountDownLatch(10);
        // 启动10个线程，每个线程countDown
        for(int index = 0; index < 10; index++) {
            new Thread(() -> {
                try {
                    Uninterruptibles.sleepUninterruptibly(10, TimeUnit.SECONDS);
                } finally {
                    // countDownLatch减1
                    countDownLatch.countDown();
                }
            }).start();
        }
        try {
            // 等待countDownLatch减为0
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
