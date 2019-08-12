package com.tuacy.study.distributelock.distributedlock.zoo;

import com.google.common.util.concurrent.Uninterruptibles;
import com.tuacy.study.distributelock.config.ZkClient;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.zookeeper.data.Stat;
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
public class ZookeeperNodeCacheTest {

    private ZkClient zkClient;

    @Autowired
    public void setZkClient(ZkClient zkClient) {
        this.zkClient = zkClient;
    }


    @Test
    public void nodeCache() throws Exception {
        final NodeCache cache = new NodeCache(zkClient.getClient(), "/tuacy/nodeCache");
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
        zkClient.createEphemeralNodeSync("/tuacy/nodeCache");
        zkClient.updateNodeDataSync("/tuacy/nodeCache", "abc".getBytes());
        zkClient.deleteNodeSync("/tuacy/nodeCache");
        Uninterruptibles.sleepUninterruptibly(30, TimeUnit.SECONDS);
        cache.close();
    }
}
