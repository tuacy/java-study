package com.tuacy.study.distributelock.config;

import com.tuacy.study.distributelock.distributedlock.zookeeper.IZookeeperLock;
import com.tuacy.study.distributelock.distributedlock.zookeeper.ZookeeperLock;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @name: ZookeeperLockConfiguration
 * @author: tuacy.
 * @date: 2019/8/20.
 * @version: 1.0
 * @Description:
 */
@Configuration
@AutoConfigureAfter(ZkConfiguration.class)
public class ZookeeperLockConfiguration {

    @Bean
    public IZookeeperLock zookeeperLock(ZkClient zkClient) {
        return new ZookeeperLock(zkClient);
    }

}
