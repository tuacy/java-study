package com.tuacy.study.zookeeper.counter;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.Uninterruptibles;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.shared.SharedCount;
import org.apache.curator.framework.recipes.shared.SharedCountListener;
import org.apache.curator.framework.recipes.shared.SharedCountReader;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @name: SharedCountTest
 * @author: tuacy.
 * @date: 2019/8/16.
 * @version: 1.0
 * @Description:
 */
public class SharedCountTest {

    private static final String PATH_COUNTER = "/int/counter";

    class CounterThread extends Thread {

        private final CountDownLatch countDownLatch;
        private final int threadIndex;
        private final SharedCount counter;

        CounterThread(SharedCount counter, int index, CountDownLatch countDownLatch) {
            this.counter = counter;
            this.threadIndex = index;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            try {
                for (int index = 0; index < 5; index++) {
                    while (true) {
                        Uninterruptibles.sleepUninterruptibly(5, TimeUnit.SECONDS);
                        boolean success = counter.trySetCount(counter.getVersionedValue(), counter.getCount() + 1);
                        if (success) {
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    System.out.println("当前值为：" + counter.getCount());
                    counter.close();
                } catch (Exception e) {
                    //ignore
                }
                countDownLatch.countDown();
            }

        }
    }

    @Test
    public void sharedCount() throws Exception {
        CountDownLatch countDownLatch = new CountDownLatch(10);
        List<CuratorFramework> zookeeperClientList = Lists.newArrayList();

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

        // 如果节点存在，我们就删除节点
        zookeeperClientList.get(0).delete().forPath(PATH_COUNTER);

        for (int index = 0; index < zookeeperClientList.size(); index++) {
            SharedCount sharedCount = new SharedCount(zookeeperClientList.get(index), PATH_COUNTER, 0);
            sharedCount.addListener(new SharedCountListener() {
                @Override
                public void countHasChanged(SharedCountReader sharedCount, int newCount) throws Exception {
                    System.out.println("计数器值改变，现在的值为：" + newCount);
                }

                @Override
                public void stateChanged(CuratorFramework client, ConnectionState newState) {
                    // 连接状态改变
                }
            });
            sharedCount.start();
            new CounterThread(sharedCount, index, countDownLatch).start();
        }

        countDownLatch.await();
        zookeeperClientList.forEach(curatorFramework -> {
            // 关闭客户端
            curatorFramework.close();
        });
    }

}
