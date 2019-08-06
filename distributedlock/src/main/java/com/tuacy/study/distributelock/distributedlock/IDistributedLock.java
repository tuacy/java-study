package com.tuacy.study.distributelock.distributedlock;

import java.util.concurrent.TimeUnit;

public interface IDistributedLock {

    /**
     * 加锁 一直到加锁成功
     */
    void lock();

    /**
     * 释放锁
     */
    void unlock();

    /**
     * 尝试加锁
     */
    boolean tryLock();

    /**
     * 尝试加锁
     */
    boolean tryLock(long time, TimeUnit unit);

}
