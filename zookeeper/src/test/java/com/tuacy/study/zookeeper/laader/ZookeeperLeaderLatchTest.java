package com.tuacy.study.zookeeper.laader;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.Uninterruptibles;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.framework.recipes.leader.LeaderLatchListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;
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

    @Test
    public void leaderLatch() throws Exception {

        List<CuratorFramework> zookeeperClientList = Lists.newArrayList();
        List<LeaderLatch> leaderLatchList = Lists.newArrayList();

        // 启动10个zookeeper客户端
        for (int index = 0; index < 10; index++) {
            RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
            CuratorFramework client = CuratorFrameworkFactory.builder()
                    .connectString("127.0.0.1:2181")
                    .retryPolicy(retryPolicy)
                    .sessionTimeoutMs(6000)
                    .connectionTimeoutMs(6000)
                    .build();
            // 启动客户端
            client.start();
            zookeeperClientList.add(client);
        }

        // 这里我们所有的客户端都参与leader选举
        for (int index = 0; index < zookeeperClientList.size(); index++) {
            // 所有的客户端都参与leader选举
            final LeaderLatch latch = new LeaderLatch(zookeeperClientList.get(index), LEADER_PATH, index + "");
            latch.addListener(new LeaderLatchListener() {
                @Override
                public void isLeader() {
                    System.out.println("我是leader: " + latch.getId());
                }

                @Override
                public void notLeader() {
                    System.out.println("我不是leader: " + latch.getId());
                }
            });
            latch.start();
            leaderLatchList.add(latch);
        }

        // 30S之后
        Uninterruptibles.sleepUninterruptibly(30, TimeUnit.SECONDS);
        // 我们找到谁是leader
        String leaderId = leaderLatchList.get(0).getLeader().getId();
        System.out.println("当前leader id : " + leaderId);
        leaderLatchList.forEach(item -> {
            // 这里我们吧leader退出选举,让剩下的重新选举
            if (item.getId().equals(leaderId)) {
                try {
                    item.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


        Uninterruptibles.sleepUninterruptibly(1, TimeUnit.MINUTES);
        leaderLatchList.forEach(curatorFramework -> {
            // 退出选举
            try {
                curatorFramework.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        zookeeperClientList.forEach(curatorFramework -> {
            // 关闭客户端
            curatorFramework.close();
        });


    }

}
