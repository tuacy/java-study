package com.tuacy.study.distributelock.distributedlock.zoo;

import com.google.common.util.concurrent.Uninterruptibles;
import com.tuacy.study.distributelock.config.LeaderSelectorAdapter;
import com.tuacy.study.distributelock.config.ZkClient;
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
public class ZookeeperLeaderSelectorTest {

    private static final String LEADER_PATH = "/tuacy/leaderSelector";

    private ZkClient zkClient;

    @Autowired
    public void setZkClient(ZkClient zkClient) {
        this.zkClient = zkClient;
    }

    @Test
    public void leaderSelector() throws Exception {
        LeaderSelectorAdapter selectorAdapter = new LeaderSelectorAdapter(zkClient.getClient(), LEADER_PATH, "leaderSelector");
        selectorAdapter.start();
        Uninterruptibles.sleepUninterruptibly(30, TimeUnit.SECONDS);
        selectorAdapter.close();
    }

}
