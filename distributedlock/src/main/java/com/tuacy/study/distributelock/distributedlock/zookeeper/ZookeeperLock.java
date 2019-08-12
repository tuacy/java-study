package com.tuacy.study.distributelock.distributedlock.zookeeper;

import com.tuacy.study.distributelock.config.ZkClient;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @name: ZookeeperLock
 * @author: tuacy.
 * @date: 2019/8/8.
 * @version: 1.0
 * @Description:
 */
@Component
public class ZookeeperLock {

    private static final String LOCK_ROOT = "lock";

    private String lockName;

    private ZkClient zkClient;

    @Autowired
    public void setZkClient(ZkClient zkClient) {
        this.zkClient = zkClient;
    }

//    private boolean lock(String lockName) {
//        this.lockName = lockName;
//
//
//        return true;
//
//    }
//
//    private boolean waitOtherLock(String waitLock, int sessionTimeout) {
//        String waitLockNode = LOCK_ROOT + "/" + waitLock;
//
//        //分布式锁
//        InterProcessMutex lock = new InterProcessMutex(zkClient.getClient(), "/lock");
//        lock.acquire();
//    }

}
