package com.tuacy.study.zookeeper.leaderadapter;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;

import java.util.concurrent.TimeUnit;

/**
 * @name: LeaderSelectorAdapter
 * @author: tuacy.
 * @date: 2019/8/12.
 * @version: 1.0
 * @Description:
 */
public class LeaderSelectorAdapter extends LeaderSelectorListenerAdapter {

    private final LeaderSelector leaderSelector;

    public LeaderSelectorAdapter(CuratorFramework client, String path, String id) {
        // 创建一个LeaderSelector对象
        leaderSelector = new LeaderSelector(client, path, this);
        // 设置id
        leaderSelector.setId(id);
        // 保证在此实例释放领导权之后还可能获得领导权
        leaderSelector.autoRequeue();
    }

    /**
     * 参与选举
     */
    public void start() {
        // 参与选举
        leaderSelector.start();
    }

    /**
     * 退出选举
     */
    public void close() {
        // 退出选举
        leaderSelector.close();
    }

    /**
     * 当获得leader的时候，这个方法会被调用。如果还想继续当leader,这个方法不能返回。如果你想要要此实例一直是leader的话可以加一个死循环
     */
    @Override
    public void takeLeadership(CuratorFramework client) throws Exception {
        System.out.println(leaderSelector.getId() + " 是leader");
        try {
            // 当上leader 5s之后，释放leader权利
            Thread.sleep(TimeUnit.SECONDS.toMillis(10));
        } catch (InterruptedException e) {
            System.err.println(leaderSelector.getId() + " 被中断.");
            Thread.currentThread().interrupt();
        } finally {
            System.out.println(leaderSelector.getId() + " 释放leader的权力。");
        }
    }
}
