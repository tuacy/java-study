package com.tuacy.study.distributelock.distributedlock.redis;

public interface IRedisDistributedLock {

    /**
     * 默认锁的过期时间
     */
    long TIMEOUT_MILLIS = 30000;

    /**
     * 默认重试多少次，如果加锁失败
     */
    int RETRY_TIMES = Integer.MAX_VALUE;

    /**
     * 加锁失败的情况下，每次加锁的间隔时间
     */
    long SLEEP_MILLIS = 500;

    boolean lock(String key);

    boolean lock(String key, int retryTimes);

    boolean lock(String key, int retryTimes, long sleepMillis);

    boolean lock(String key, long expire);

    boolean lock(String key, long expire, int retryTimes);

    boolean lock(String key, long expire, int retryTimes, long sleepMillis);

    boolean unlock(String key);

}
