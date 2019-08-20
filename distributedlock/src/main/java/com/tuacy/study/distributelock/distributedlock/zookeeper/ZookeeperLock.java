package com.tuacy.study.distributelock.distributedlock.zookeeper;

import com.tuacy.study.distributelock.config.ZkClient;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;

import java.util.concurrent.TimeUnit;

/**
 * @name: ZookeeperLock
 * @author: tuacy.
 * @date: 2019/8/8.
 * @version: 1.0
 * @Description:
 */
public class ZookeeperLock implements IZookeeperLock {

    private static final String LOCK_ROOT = "/lock";
    private InterProcessMutex interProcessMutex;

    public ZookeeperLock(ZkClient zkClient) {
        interProcessMutex = new InterProcessMutex(zkClient.getClient(), LOCK_ROOT);
    }

    @Override
    public boolean lock() {
        try {
            interProcessMutex.acquire();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean lock(long time, TimeUnit unit) {
        try {
            interProcessMutex.acquire(time, unit);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void unlock() {
        try {
            interProcessMutex.release();
        } catch (Exception e) {
            e.printStackTrace();
            // ignore
        }
    }

}
