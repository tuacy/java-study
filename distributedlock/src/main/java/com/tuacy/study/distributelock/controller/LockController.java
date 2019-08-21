package com.tuacy.study.distributelock.controller;

import com.tuacy.study.distributelock.entity.LockParam;
import com.tuacy.study.distributelock.service.api.IDbLockService;
import com.tuacy.study.distributelock.service.api.IRedisLockService;
import com.tuacy.study.distributelock.service.api.IZookeeperLockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @name: LockController
 * @author: tuacy.
 * @date: 2019/8/21.
 * @version: 1.0
 * @Description:
 */
@RestController
public class LockController {

    private IZookeeperLockService zookeeperLockService;
    private IDbLockService dbLockService;
    private IRedisLockService redisLockService;

    @Autowired
    public void setZookeeperLockService(IZookeeperLockService zookeeperLockService) {
        this.zookeeperLockService = zookeeperLockService;
    }

    @Autowired
    public void setDbLockService(IDbLockService dbLockService) {
        this.dbLockService = dbLockService;
    }

    @Autowired
    public void setRedisLockService(IRedisLockService redisLockService) {
        this.redisLockService = redisLockService;
    }

    /**
     * zookeeper 加锁
     */
    @RequestMapping(value = "/zookeeperLock", method = RequestMethod.POST)
    public String zookeeperLock(@RequestBody LockParam param) {
        zookeeperLockService.lockAndUnlock(param.getKey());
        return "zookeeperLock";
    }

    /**
     * zookeeper aop 加锁
     */
    @RequestMapping(value = "/aopZookeeperLock", method = RequestMethod.POST)
    public String aopZookeeperLock(@RequestBody LockParam param) {
        zookeeperLockService.aopLockAndUnlock(param.getKey());
        return "aopZookeeperLock";
    }

    /**
     * redis 加锁
     */
    @RequestMapping(value = "/redisLock", method = RequestMethod.POST)
    public String redisLock(@RequestBody LockParam param) {
        redisLockService.lockAndUnlock(param.getKey());
        return "redisLock";
    }

    /**
     * redis aop 加锁
     */
    @RequestMapping(value = "/aopRedisLock", method = RequestMethod.POST)
    public String aopRedisLock(@RequestBody LockParam param) {
        redisLockService.aopLockAndUnlock(param.getKey());
        return "aopRedisLock";
    }

    /**
     * db 加锁
     */
    @RequestMapping(value = "/dbLock", method = RequestMethod.POST)
    public String dbLock(@RequestBody LockParam param) {
        dbLockService.lockAndUnlock(param.getKey());
        return "dbLock";
    }

    /**
     * db aop 加锁
     */
    @RequestMapping(value = "/aopDbLock", method = RequestMethod.POST)
    public String aopDbLock(@RequestBody LockParam param) {
        dbLockService.aopLockAndUnlock(param.getKey());
        return "aopDbLock";
    }

}
