package com.tuacy.study.distributelock.config;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * @name: ZkClient
 * @author: tuacy.
 * @date: 2019/8/7.
 * @version: 1.0
 * @Description:
 */
public class ZkClient {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private CuratorFramework client;
    private String zookeeperServer;
    private int sessionTimeoutMs;
    private int connectionTimeoutMs;
    private int baseSleepTimeMs;
    private int maxRetries;

    public void setZookeeperServer(String zookeeperServer) {
        this.zookeeperServer = zookeeperServer;
    }

    public String getZookeeperServer() {
        return zookeeperServer;
    }

    public void setSessionTimeoutMs(int sessionTimeoutMs) {
        this.sessionTimeoutMs = sessionTimeoutMs;
    }

    public int getSessionTimeoutMs() {
        return sessionTimeoutMs;
    }

    public void setConnectionTimeoutMs(int connectionTimeoutMs) {
        this.connectionTimeoutMs = connectionTimeoutMs;
    }

    public int getConnectionTimeoutMs() {
        return connectionTimeoutMs;
    }

    public void setBaseSleepTimeMs(int baseSleepTimeMs) {
        this.baseSleepTimeMs = baseSleepTimeMs;
    }

    public int getBaseSleepTimeMs() {
        return baseSleepTimeMs;
    }

    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    public int getMaxRetries() {
        return maxRetries;
    }

    public void init() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(baseSleepTimeMs, maxRetries);
        client = CuratorFrameworkFactory.builder()
                .connectString(zookeeperServer)
                .retryPolicy(retryPolicy)
                .sessionTimeoutMs(sessionTimeoutMs)
                .connectionTimeoutMs(connectionTimeoutMs)
                .build();
        client.start();
    }

    public void stop() {
        client.close();
    }

    public CuratorFramework getClient() {
        return client;
    }

    public void register() {
        try {
            String rootPath = "/" + "services";
            String hostAddress = InetAddress.getLocalHost().getHostAddress();
            String serviceInstance = "prometheus" + "-" + hostAddress + "-";
            client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(rootPath + "/" + serviceInstance);
        } catch (Exception e) {
            logger.error("注册出错", e);
        }
    }

    /**
     * 查看指定目录下所有的children
     *
     * @param path 目录
     * @return children
     */
    public List<String> getChildren(String path) {
        List<String> childrenList = new ArrayList<>();
        try {
            childrenList = client.getChildren().forPath(path);
        } catch (Exception e) {
            logger.error("获取子节点出错", e);
        }
        return childrenList;
    }

    /**
     * 查看指定目录下所有的children的count
     *
     * @param path 目录
     * @return children个数
     */
    public int getChildrenCount(String path) {
        return getChildren(path).size();
    }

    public List<String> getInstances() {
        return getChildren("/services");
    }

    public int getInstancesCount() {
        return getInstances().size();
    }

    public void crateNode() {

    }

}
