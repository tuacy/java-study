package com.tuacy.study.distributelock.distributedlock.db;

import com.tuacy.study.distributelock.distributedlock.LockFailAction;

import java.lang.annotation.*;

/**
 * @name: DbLock
 * @author: tuacy.
 * @date: 2019/8/20.
 * @version: 1.0
 * @Description:
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface DbLock {

    /**
     * 锁的资源，redis的key
     */
    String key() default "default";

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
