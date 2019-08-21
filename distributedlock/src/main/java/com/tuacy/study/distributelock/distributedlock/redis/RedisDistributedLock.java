package com.tuacy.study.distributelock.distributedlock.redis;

import com.tuacy.study.distributelock.distributedlock.LockFailAction;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface RedisDistributedLock {
    /**
     * 锁的资源，redis的key
     */
    String key() default "default";

    /**
     * 持锁时间,单位毫秒
     */
    long keepMills() default 30000;

    /**
     * 当获取失败时候动作
     */
    LockFailAction action() default LockFailAction.CONTINUE;

    /**
     * 重试的间隔时间,设置GIVEUP忽略此项
     */
    long sleepMills() default 200;

    /**
     * 重试次数
     */
    int retryTimes() default 5;
}
