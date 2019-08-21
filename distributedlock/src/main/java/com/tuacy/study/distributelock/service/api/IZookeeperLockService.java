package com.tuacy.study.distributelock.service.api;

/**
 * @name: IZookeeperLockService
 * @author: tuacy.
 * @date: 2019/8/21.
 * @version: 1.0
 * @Description:
 */
public interface IZookeeperLockService {

    boolean lockAndUnlock(String key);

    boolean aopLockAndUnlock(String key);

}
