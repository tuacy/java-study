package com.tuacy.study.distributelock.distributedlock.zookeeper;

import com.tuacy.study.distributelock.config.ZkClient;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;

import java.util.concurrent.TimeUnit;

/**
 * @name: ZookeeperDistributedLockImpl
 * @author: tuacy.
 * @date: 2019/8/8.
 * @version: 1.0
 * @Description: zookeeper分布式锁的实现
 */
public class ZookeeperDistributedLockImpl implements IZookeeperDistributedLock {

    /**
     * curator给封住好的一个分布式锁对象和ReentrantLock类似
     */
    private CuratorFramework curatorFramework;

    public ZookeeperDistributedLockImpl(ZkClient zkClient) {
        curatorFramework = zkClient.getClient();
    }

    @Override
    public boolean lock(String key) {
        try {
            InterProcessMutex  interProcessMutex = new InterProcessMutex(curatorFramework, key);
            interProcessMutex.acquire();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean lock(String key, long time, TimeUnit unit) {
        try {
            InterProcessMutex  interProcessMutex = new InterProcessMutex(curatorFramework, key);
            interProcessMutex.acquire(time, unit);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void unlock(String key) {
        try {
            InterProcessMutex  interProcessMutex = new InterProcessMutex(curatorFramework, key);
            interProcessMutex.release();
        } catch (Exception e) {
            e.printStackTrace();
            // ignore
        }
    }

}
