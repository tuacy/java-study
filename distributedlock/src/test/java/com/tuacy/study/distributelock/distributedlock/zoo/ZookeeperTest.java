package com.tuacy.study.distributelock.distributedlock.zoo;

import com.tuacy.study.distributelock.config.ZkClient;
import org.apache.zookeeper.CreateMode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @name: ZookeeperTest
 * @author: tuacy.
 * @date: 2019/8/7.
 * @version: 1.0
 * @Description:
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ZookeeperTest {

    private ZkClient zkClient;

    @Autowired
    public void setZkClient(ZkClient zkClient) {
        this.zkClient = zkClient;
    }

    @Test
    public void zookeeperRunTest() throws Exception {
        System.out.println(zkClient.getChildren("/zookeeper"));
        try {
            zkClient.getClient().create().withMode(CreateMode.PERSISTENT).forPath("/tuacy2", "".getBytes());
            System.out.println(zkClient.getChildren("/"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
