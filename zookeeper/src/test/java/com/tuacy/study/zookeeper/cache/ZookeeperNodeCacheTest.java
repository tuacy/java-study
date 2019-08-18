package com.tuacy.study.zookeeper.cache;

import com.google.common.util.concurrent.Uninterruptibles;
import com.tuacy.study.zookeeper.config.ZkClient;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

/**
 * @name: ZookeeperPathCacheTest
 * @author: tuacy.
 * @date: 2019/8/12.
 * @version: 1.0
 * @Description:
 */
public class ZookeeperNodeCacheTest {



    @Test
    public void nodeCache() throws Exception {

        // 创建zookeeper客户端
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString("127.0.0.1:2181")
                .retryPolicy(retryPolicy)
                .sessionTimeoutMs(6000)
                .connectionTimeoutMs(6000)
                .build();
        // 启动客户端
        client.start();

        final NodeCache cache = new NodeCache(client, "/tuacy/nodeCache");
        cache.start();
        // 添加监听
        cache.getListenable().addListener(new NodeCacheListener() {

            @Override
            public void nodeChanged() throws Exception {
                ChildData data = cache.getCurrentData();
                if (null != data) {
                    System.out.println("节点数据：" + new String(cache.getCurrentData().getData()));
                } else {
                    System.out.println("节点被删除!");
                }
            }
        });
        // 添加节点
        client.create().creatingParentsIfNeeded().forPath("/tuacy/nodeCache");
        Uninterruptibles.sleepUninterruptibly(10, TimeUnit.SECONDS);
        client.setData().forPath("/tuacy/nodeCache", "abc".getBytes());
        Uninterruptibles.sleepUninterruptibly(10, TimeUnit.SECONDS);
        client.delete().forPath("/tuacy/nodeCache");
        Uninterruptibles.sleepUninterruptibly(30, TimeUnit.SECONDS);
        cache.close();
    }
}
