package com.tuacy.study.distributelock.service.api;

/**
 * @name: IRedisLockService
 * @author: tuacy.
 * @date: 2019/8/21.
 * @version: 1.0
 * @Description:
 */
public interface IDbLockService {

    boolean lockAndUnlock(String key);

    boolean aopLockAndUnlock(String key);

}
