package com.tuacy.study.distributelock.distributedlock.zookeeper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @name: ZookeeperLock
 * @author: tuacy.
 * @date: 2019/8/20.
 * @version: 1.0
 * @Description:
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ZookeeperLock {

    private IZookeeperLock zookeeperLock;

    @Autowired
    public void setZookeeperLock(IZookeeperLock zookeeperLock) {
        this.zookeeperLock = zookeeperLock;
    }

    @Test
    public void zookeeperLockTest() {
        zookeeperLock.lock();
        zookeeperLock.unlock();
    }
}
