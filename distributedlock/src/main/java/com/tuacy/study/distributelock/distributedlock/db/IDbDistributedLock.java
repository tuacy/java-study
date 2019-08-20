package com.tuacy.study.distributelock.distributedlock.db;

/**
 * @name: IDbDistributedLock
 * @author: tuacy.
 * @date: 2019/8/20.
 * @version: 1.0
 * @Description: 数据库实现分布式锁 接口
 */
public interface IDbDistributedLock {

    /**
     * 默认重试多少次，如果加锁失败
     */
    int RETRY_TIMES = Integer.MAX_VALUE;

    /**
     * 加锁失败的情况下，重试的时候，每次加锁的间隔时间
     */
    long RETRY_INTERVAL_MILLIS = 500;

    boolean lock(String key);

    boolean lock(String key, int retryTimes);

    boolean lock(String key, int retryTimes, long sleepMillis);

    void unlock(String key);
}
