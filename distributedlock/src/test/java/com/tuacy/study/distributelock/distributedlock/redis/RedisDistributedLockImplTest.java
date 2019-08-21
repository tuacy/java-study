package com.tuacy.study.distributelock.distributedlock.redis;

import com.google.common.util.concurrent.Uninterruptibles;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

/**
 * @name: RedisDistributedLockImplTest
 * @author: tuacy.
 * @date: 2019/8/6.
 * @version: 1.0
 * @Description:
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class RedisDistributedLockImplTest {

    @Autowired
    private IRedisDistributedLock redisDistributedLock;

    @Test
    public void redisLock() {
        System.out.println("加锁：" + redisDistributedLock.lock("orderNum"));
        Uninterruptibles.sleepUninterruptibly(60, TimeUnit.SECONDS);
        System.out.println("释放锁：" + redisDistributedLock.unlock("orderNum"));

    }

}
