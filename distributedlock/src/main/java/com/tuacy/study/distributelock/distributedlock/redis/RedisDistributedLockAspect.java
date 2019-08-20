package com.tuacy.study.distributelock.distributedlock.redis;

import com.tuacy.study.distributelock.config.RedisDistributedLockConfiguration;
import com.tuacy.study.distributelock.distributedlock.LockFailAction;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @name: DistributedLockAspectConfiguration
 * @author: tuacy.
 * @date: 2019/8/6.
 * @version: 1.0
 * @Description:
 */
@Aspect
@Configuration
@ConditionalOnClass(IRedisDistributedLock.class)
@AutoConfigureAfter(RedisDistributedLockConfiguration.class)
public class RedisDistributedLockAspect {

    private final Logger logger = LoggerFactory.getLogger(RedisDistributedLockAspect.class);

    @Autowired
    private IRedisDistributedLock distributedLock;

    @Pointcut("@annotation(com.tuacy.study.distributelock.distributedlock.redis.RedisLock)")
    private void lockPoint() {

    }

    @Around("lockPoint()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        RedisLock redisLock = method.getAnnotation(RedisLock.class);
        String key = redisLock.key();
        if (StringUtils.isEmpty(key)) {
            Object[] args = pjp.getArgs();
            key = Arrays.toString(args);
        }
        int retryTimes = redisLock.action().equals(LockFailAction.CONTINUE) ? redisLock.retryTimes() : 0;
        boolean lock = distributedLock.lock(key, redisLock.keepMills(), retryTimes, redisLock.sleepMills());
        if (!lock) {
            logger.debug("get lock failed : " + key);
            return null;
        }

        //得到锁,执行方法，释放锁
        logger.debug("get lock success : " + key);
        try {
            return pjp.proceed();
        } catch (Exception e) {
            logger.error("execute locked method occured an exception", e);
        } finally {
            boolean releaseResult = distributedLock.unlock(key);
            logger.debug("release lock : " + key + (releaseResult ? " success" : " failed"));
        }
        return null;
    }
}
