package com.tuacy.study.distributelock.distributedlock.zoo;

import com.google.common.util.concurrent.Uninterruptibles;
import com.tuacy.study.distributelock.config.ZkClient;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
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
public class ZookeeperPathCacheTest {

    private ZkClient zkClient;

    @Autowired
    public void setZkClient(ZkClient zkClient) {
        this.zkClient = zkClient;
    }


    @Test
    public void pathChildrenCache() throws Exception {
        PathChildrenCache cache = new PathChildrenCache(zkClient.getClient(), "/tuacy/pathCache", true);
        cache.start();
        // 添加监听
        cache.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                System.out.println("事件类型：" + event.getType());
                if (null != event.getData()) {
                    System.out.println("节点数据：" + event.getData().getPath() + " = " + new String(event.getData().getData()));
                }
            }
        });
        // 添加节点
        zkClient.createEphemeralNodeSync("/tuacy/pathCache/001");
        Stat stat = zkClient.getNodeStatSync("/tuacy/pathCache/001");
        System.out.println(stat.getVersion());
        Uninterruptibles.sleepUninterruptibly(30, TimeUnit.SECONDS);
        cache.close();
    }
}
