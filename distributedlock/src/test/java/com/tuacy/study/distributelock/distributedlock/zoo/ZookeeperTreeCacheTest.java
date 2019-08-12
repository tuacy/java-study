package com.tuacy.study.distributelock.distributedlock.zoo;

import com.google.common.util.concurrent.Uninterruptibles;
import com.tuacy.study.distributelock.config.ZkClient;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.*;
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
@SpringBootTest
@RunWith(SpringRunner.class)
public class ZookeeperTreeCacheTest {

    private ZkClient zkClient;

    @Autowired
    public void setZkClient(ZkClient zkClient) {
        this.zkClient = zkClient;
    }


    @Test
    public void nodeCache() throws Exception {
        final TreeCache cache = new TreeCache(zkClient.getClient(), "/tuacy/treeCache");
        // 添加监听
        cache.getListenable().addListener(new TreeCacheListener() {


            @Override
            public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
                System.out.println("事件类型：" + event.getType() + " | 路径：" + (null != event.getData() ? event.getData().getPath() : null));
            }
        });
        cache.start();
        // 添加节点
        zkClient.createEphemeralNodeSync("/tuacy/nodeCache");
        zkClient.updateNodeDataSync("/tuacy/nodeCache", "abc".getBytes());
        zkClient.deleteNodeSync("/tuacy/nodeCache");
        Uninterruptibles.sleepUninterruptibly(30, TimeUnit.SECONDS);
        cache.close();
    }
}
