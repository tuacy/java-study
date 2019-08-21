package com.tuacy.study.zookeeper.config;

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

    /**
     * 服务器列表，格式host1:port1,host2:port2,...
     */
    @Value("${zookeeper.server}")
    private String zookeeperServer;
    /**
     * 会话超时时间，单位毫秒，默认60000ms
     */
    @Value(("${zookeeper.sessionTimeoutMs}"))
    private int sessionTimeoutMs;
    /**
     * 连接创建超时时间，单位毫秒，默认60000ms
     */
    @Value("${zookeeper.connectionTimeoutMs}")
    private int connectionTimeoutMs;
    /**
     * 当连接异常时的重试次数
     */
    @Value("${zookeeper.maxRetries}")
    private int maxRetries;
    /**
     * 重试之间等待的初始时间
     */
    @Value("${zookeeper.baseSleepTimeMs}")
    private int baseSleepTimeMs;
    /**
     * 为了实现不同的Zookeeper业务之间的隔离，有的时候需要为每个业务分配一个独立的命名空间
     */
    @Value("${zookeeper.namespace}")
    private String namespace;

//    @Bean(initMethod = "init", destroyMethod = "stop")
//    public ZkClient zkClient() {
//        ZkClient zkClient = new ZkClient();
//        zkClient.setZookeeperServer(zookeeperServer);
//        zkClient.setSessionTimeoutMs(sessionTimeoutMs);
//        zkClient.setConnectionTimeoutMs(connectionTimeoutMs);
//        zkClient.setMaxRetries(maxRetries);
//        zkClient.setBaseSleepTimeMs(baseSleepTimeMs);
//        zkClient.setNamespace(namespace);
//        return zkClient;
//    }

}
