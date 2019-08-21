package com.tuacy.study.distributelock.service.impl;

import com.google.common.util.concurrent.Uninterruptibles;
import com.tuacy.study.distributelock.distributedlock.redis.IRedisDistributedLock;
import com.tuacy.study.distributelock.distributedlock.redis.RedisDistributedLock;
import com.tuacy.study.distributelock.service.api.IRedisLockService;
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
public class RedisLockServiceImpl implements IRedisLockService {

    private IRedisDistributedLock redisDistributedLock;

    @Autowired
    public void setRedisDistributedLock(IRedisDistributedLock redisDistributedLock) {
        this.redisDistributedLock = redisDistributedLock;
    }

    @Override
    public boolean lockAndUnlock(String key) {
        if (redisDistributedLock.lock(key)) {
            Uninterruptibles.sleepUninterruptibly(5000, TimeUnit.MILLISECONDS);
            redisDistributedLock.unlock(key);
            return true;
        }
        return false;
    }

    @Override
    @RedisDistributedLock(key = "orderId")
    public boolean aopLockAndUnlock(String key) {
        return false;
    }
}
