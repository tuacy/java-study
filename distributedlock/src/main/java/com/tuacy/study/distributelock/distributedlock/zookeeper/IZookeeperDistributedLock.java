package com.tuacy.study.distributelock.distributedlock.zookeeper;

import java.util.concurrent.TimeUnit;

/**
 * @name: IZookeeperDistributedLock
 * @author: tuacy.
 * @date: 2019/8/20.
 * @version: 1.0
 * @Description:
 */
public interface IZookeeperDistributedLock {

    /**
     * 加锁 (key “/”开头 比如key: /lock)
     */
    boolean lock(String key);

    /**
     * 加锁 (key “/”开头 比如key: /lock)
     */
    boolean lock(String key, long time, TimeUnit unit);

    /**
     * 释放锁 (key “/”开头 比如key: /lock)
     */
    void unlock(String key);

}
