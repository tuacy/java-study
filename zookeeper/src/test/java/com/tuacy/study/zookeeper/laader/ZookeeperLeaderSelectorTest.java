package com.tuacy.study.zookeeper.laader;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.Uninterruptibles;
import com.tuacy.study.zookeeper.leaderadapter.LeaderSelectorAdapter;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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
public class ZookeeperLeaderSelectorTest {

    private static final String LEADER_PATH = "/tuacy/leaderSelector";

    @Test
    public void leaderSelector() {
        List<CuratorFramework> zookeeperClientList = Lists.newArrayList();
        List<LeaderSelectorAdapter> leaderLatchList = Lists.newArrayList();

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
            final LeaderSelectorAdapter latch = new LeaderSelectorAdapter(zookeeperClientList.get(index), LEADER_PATH, index + "");
            latch.start();
            leaderLatchList.add(latch);
        }

        // 1分钟之后关掉程序
        Uninterruptibles.sleepUninterruptibly(1, TimeUnit.MINUTES);
        leaderLatchList.forEach(curatorFramework -> {
            // 退出选举
            curatorFramework.close();

        });
        zookeeperClientList.forEach(curatorFramework -> {
            // 关闭客户端
            curatorFramework.close();
        });
    }

}
