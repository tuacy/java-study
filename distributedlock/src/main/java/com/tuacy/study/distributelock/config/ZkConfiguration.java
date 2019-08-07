package com.tuacy.study.distributelock.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @name: ZkConfiguration
 * @author: tuacy.
 * @date: 2019/8/7.
 * @version: 1.0
 * @Description:
 */
@Configuration
public class ZkConfiguration {

    @Value("${zookeeper.server}")
    private String zookeeperServer;
    @Value(("${zookeeper.sessionTimeoutMs}"))
    private int sessionTimeoutMs;
    @Value("${zookeeper.connectionTimeoutMs}")
    private int connectionTimeoutMs;
    @Value("${zookeeper.maxRetries}")
    private int maxRetries;
    @Value("${zookeeper.baseSleepTimeMs}")
    private int baseSleepTimeMs;

    @Bean(initMethod = "init", destroyMethod = "stop")
    public ZkClient zkClient() {
        ZkClient zkClient = new ZkClient();
        zkClient.setZookeeperServer(zookeeperServer);
        zkClient.setSessionTimeoutMs(sessionTimeoutMs);
        zkClient.setConnectionTimeoutMs(connectionTimeoutMs);
        zkClient.setMaxRetries(maxRetries);
        zkClient.setBaseSleepTimeMs(baseSleepTimeMs);
        return zkClient;
    }

}
