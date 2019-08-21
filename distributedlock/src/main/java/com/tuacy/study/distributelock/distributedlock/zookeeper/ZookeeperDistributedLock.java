package com.tuacy.study.distributelock.distributedlock.zookeeper;

import java.lang.annotation.*;

/**
 * @name: ZookeeperDistributedLock
 * @author: tuacy.
 * @date: 2019/8/20.
 * @version: 1.0
 * @Description:
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ZookeeperDistributedLock {

    /**
     * 锁的资源
     */
    String key() default "default";

    /**
     * 加锁的超时时间
     */
    long timeout() default 0;

}
