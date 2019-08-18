package com.tuacy.study.zookeeper.cache;

import com.google.common.util.concurrent.Uninterruptibles;
import com.tuacy.study.zookeeper.config.ZkClient;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.ThreadUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @name: ZookeeperPathCacheTest
 * @author: tuacy.
 * @date: 2019/8/12.
 * @version: 1.0
 * @Description:
 */
public class ZookeeperTreeCacheTest {


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
        final TreeCache cache = TreeCache.newBuilder(client, "/tuacy/treeCache")
                .setCacheData(true)
                .build();
        // 添加监听
        cache.getListenable().addListener(new TreeCacheListener() {

            @Override
            public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
                System.out.println("事件类型：" + event.getType() + " | 路径：" + (null != event.getData() ? event.getData().getPath() : null));
            }
        });
        cache.start();
        // 添加节点
        client.create().creatingParentsIfNeeded().forPath("/tuacy/treeCache");
        Uninterruptibles.sleepUninterruptibly(10, TimeUnit.SECONDS);
        // 给节点设置数据
        client.setData().forPath("/tuacy/treeCache", "abc".getBytes());
        // 创建子节点
        client.create().creatingParentsIfNeeded().forPath("/tuacy/treeCache/001");
        Uninterruptibles.sleepUninterruptibly(10, TimeUnit.SECONDS);
        // 修改子节点的数据
        client.setData().forPath("/tuacy/treeCache/001", "abc".getBytes());
        Uninterruptibles.sleepUninterruptibly(10, TimeUnit.SECONDS);
        // 删除子节点
        client.delete().forPath("/tuacy/treeCache/001");
        Uninterruptibles.sleepUninterruptibly(10, TimeUnit.SECONDS);
        // 删除节点
        client.delete().forPath("/tuacy/treeCache");
        Uninterruptibles.sleepUninterruptibly(30, TimeUnit.SECONDS);
        cache.close();
        client.close();
    }
}
