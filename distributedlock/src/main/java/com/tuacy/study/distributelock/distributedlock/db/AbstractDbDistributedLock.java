package com.tuacy.study.distributelock.distributedlock.db;

/**
 * @name: AbstractDbDistributedLock
 * @author: tuacy.
 * @date: 2019/8/20.
 * @version: 1.0
 * @Description:
 */
public abstract class AbstractDbDistributedLock implements IDbDistributedLock {
    @Override
    public boolean lock(String key) {
        return lock(key, RETRY_TIMES, RETRY_INTERVAL_MILLIS);
    }

    @Override
    public boolean lock(String key, int retryTimes) {
        return lock(key, retryTimes, RETRY_INTERVAL_MILLIS);
    }
}
