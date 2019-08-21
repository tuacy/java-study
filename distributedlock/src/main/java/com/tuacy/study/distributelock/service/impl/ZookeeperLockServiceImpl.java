package com.tuacy.study.distributelock.service.impl;

import com.google.common.util.concurrent.Uninterruptibles;
import com.tuacy.study.distributelock.distributedlock.zookeeper.IZookeeperDistributedLock;
import com.tuacy.study.distributelock.distributedlock.zookeeper.ZookeeperDistributedLock;
import com.tuacy.study.distributelock.service.api.IZookeeperLockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @name: ZookeeperLockServiceImpl
 * @author: tuacy.
 * @date: 2019/8/21.
 * @version: 1.0
 * @Description:
 */
@Service
public class ZookeeperLockServiceImpl implements IZookeeperLockService {

    private IZookeeperDistributedLock zookeeperLock;

    @Autowired
    public void setZookeeperLock(IZookeeperDistributedLock zookeeperLock) {
        this.zookeeperLock = zookeeperLock;
    }

    @Override
    public boolean lockAndUnlock(String key) {

        if (zookeeperLock.lock(key)) {
            Uninterruptibles.sleepUninterruptibly(5000, TimeUnit.MILLISECONDS);
            zookeeperLock.unlock(key);
            return true;
        }
        return false;
    }

    @Override
    @ZookeeperDistributedLock(key = "/aopZookeeperLock")
    public boolean aopLockAndUnlock(String key) {
        return false;
    }
}
