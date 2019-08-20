package com.tuacy.study.distributelock.distributedlock.zookeeper;

import java.util.concurrent.TimeUnit;

/**
 * @name: IZookeeperLock
 * @author: tuacy.
 * @date: 2019/8/20.
 * @version: 1.0
 * @Description:
 */
public interface IZookeeperLock {

    boolean lock();

    boolean lock(long time, TimeUnit unit);

    void unlock();

}
