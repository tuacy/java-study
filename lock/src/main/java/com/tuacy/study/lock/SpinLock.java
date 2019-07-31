package com.tuacy.study.lock;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @name: SpinLock
 * @author: tuacy.
 * @date: 2019/7/31.
 * @version: 1.0
 * @Description: 自旋锁的简单实现
 */
public class SpinLock {

    private final AtomicBoolean spinLock = new AtomicBoolean(true);

    /**
     * 获取锁
     */
    public void lock() {
        boolean flag;
        // 一直去获取锁
        do {
            // 只有当spinLock的值为true的时候,我们才可以获取成功，并且把值设置为false。
            flag = spinLock.compareAndSet(true, false);
        } while (!flag);
    }

    /**
     * 释放锁
     */
    public void unlock() {
        spinLock.compareAndSet(false, true);
    }

}
