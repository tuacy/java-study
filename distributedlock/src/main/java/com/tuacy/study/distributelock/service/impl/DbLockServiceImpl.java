package com.tuacy.study.distributelock.service.impl;

import com.google.common.util.concurrent.Uninterruptibles;
import com.tuacy.study.distributelock.distributedlock.db.DbDistributedLock;
import com.tuacy.study.distributelock.distributedlock.db.IDbDistributedLock;
import com.tuacy.study.distributelock.service.api.IDbLockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @name: RedisLockServiceImpl
 * @author: tuacy.
 * @date: 2019/8/21.
 * @version: 1.0
 * @Description:
 */
@Service
public class DbLockServiceImpl implements IDbLockService {

    private IDbDistributedLock dbDistributedLock;

    @Autowired
    public void setDbDistributedLock(IDbDistributedLock dbDistributedLock) {
        this.dbDistributedLock = dbDistributedLock;
    }

    @Override
    public boolean lockAndUnlock(String key) {
        if (dbDistributedLock.lock(key)) {
            Uninterruptibles.sleepUninterruptibly(5000, TimeUnit.MILLISECONDS);
            dbDistributedLock.unlock(key);
            return true;
        }
        return false;
    }

    @Override
    @DbDistributedLock(key = "orderId")
    public boolean aopLockAndUnlock(String key) {
        return false;
    }
}
