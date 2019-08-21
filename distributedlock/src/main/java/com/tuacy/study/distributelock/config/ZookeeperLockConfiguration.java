package com.tuacy.study.distributelock.config;

import com.tuacy.study.distributelock.distributedlock.zookeeper.IZookeeperDistributedLock;
import com.tuacy.study.distributelock.distributedlock.zookeeper.ZookeeperDistributedLockImpl;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
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
    @ConditionalOnBean(ZkClient.class)
    public IZookeeperDistributedLock zookeeperLock(ZkClient zkClient) {
        return new ZookeeperDistributedLockImpl(zkClient);
    }

}
