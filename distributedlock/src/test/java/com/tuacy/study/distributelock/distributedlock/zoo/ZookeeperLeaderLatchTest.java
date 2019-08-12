package com.tuacy.study.distributelock.distributedlock.zoo;

import com.google.common.util.concurrent.Uninterruptibles;
import com.tuacy.study.distributelock.config.ZkClient;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.framework.recipes.leader.LeaderLatchListener;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

/**
 * @name: ZookeeperLeaderLatchTest
 * @author: tuacy.
 * @date: 2019/8/12.
 * @version: 1.0
 * @Description:
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ZookeeperLeaderLatchTest {

    private static final String LEADER_PATH = "/tuacy/leaderSelector";

    private ZkClient zkClient;

    @Autowired
    public void setZkClient(ZkClient zkClient) {
        this.zkClient = zkClient;
    }

    @Test
    public void leaderLatch() throws Exception {

        LeaderLatch latch = new LeaderLatch(zkClient.getClient(), LEADER_PATH);
        latch.addListener(new LeaderLatchListener(){
            @Override
            public void isLeader() {
                System.out.println("我是Leader");
            }

            @Override
            public void notLeader() {
                System.out.println("我不是Leader");
            }
        });
        latch.start();
        Uninterruptibles.sleepUninterruptibly(30, TimeUnit.SECONDS);
        latch.close();
    }

}
