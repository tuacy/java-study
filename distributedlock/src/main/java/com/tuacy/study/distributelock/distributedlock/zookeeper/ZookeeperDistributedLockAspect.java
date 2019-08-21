package com.tuacy.study.distributelock.distributedlock.zookeeper;

import com.tuacy.study.distributelock.config.ZookeeperLockConfiguration;
import com.tuacy.study.distributelock.distributedlock.db.IDbDistributedLock;
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
import java.util.concurrent.TimeUnit;

/**
 * @name: DistributedLockAspectConfiguration
 * @author: tuacy.
 * @date: 2019/8/6.
 * @version: 1.0
 * @Description:
 */
@Aspect
@Configuration
@ConditionalOnClass(IDbDistributedLock.class)
@AutoConfigureAfter(ZookeeperLockConfiguration.class)
public class ZookeeperDistributedLockAspect {

    private final Logger logger = LoggerFactory.getLogger(ZookeeperDistributedLockAspect.class);

    private IZookeeperDistributedLock zookeeperLock;

    @Autowired
    public void setZookeeperLock(IZookeeperDistributedLock zookeeperLock) {
        this.zookeeperLock = zookeeperLock;
    }

    @Pointcut("@annotation(com.tuacy.study.distributelock.distributedlock.zookeeper.ZookeeperDistributedLock)")
    private void lockPoint() {

    }

    @Around("lockPoint()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        ZookeeperDistributedLock lockAnnotation = method.getAnnotation(ZookeeperDistributedLock.class);
        String key = lockAnnotation.key();
        if (StringUtils.isEmpty(key)) {
            Object[] args = pjp.getArgs();
            key = Arrays.toString(args);
        }
        long timeOut = lockAnnotation.timeout();
        boolean lockFlag;
        if (timeOut <= 0) {
            lockFlag = zookeeperLock.lock(key);
        } else {
            lockFlag = zookeeperLock.lock(key, timeOut, TimeUnit.MILLISECONDS);
        }
        if (!lockFlag) {
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
            zookeeperLock.unlock(key);
            logger.debug("release lock");
        }
        return null;
    }
}
