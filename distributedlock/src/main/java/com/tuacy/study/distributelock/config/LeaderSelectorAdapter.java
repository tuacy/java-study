package com.tuacy.study.distributelock.config;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @name: LeaderSelectorAdapter
 * @author: tuacy.
 * @date: 2019/8/12.
 * @version: 1.0
 * @Description:
 */
public class LeaderSelectorAdapter extends LeaderSelectorListenerAdapter implements Closeable {
    private final String name;
    private final LeaderSelector leaderSelector;
    private final AtomicInteger leaderCount = new AtomicInteger();

    public LeaderSelectorAdapter(CuratorFramework client, String path, String name) {
        this.name = name;
        leaderSelector = new LeaderSelector(client, path, this);
        // 保证在此实例释放领导权之后还可能获得领导权
        leaderSelector.autoRequeue();
    }

    public void start() throws IOException {
        leaderSelector.start();
    }

    @Override
    public void close() throws IOException {
        leaderSelector.close();
    }

    /**
     * 当获得leader的时候，这个方法会被调用。如果还想继续当leader,这个方法不能返回。如果你想要要此实例一直是leader的话可以加一个死循环
     */
    @Override
    public void takeLeadership(CuratorFramework client) throws Exception {
        final int waitSeconds = (int) (5 * Math.random()) + 1;
        System.out.println(name + " is now the leader. Waiting " + waitSeconds + " seconds...");
        System.out.println(name + " has been leader " + leaderCount.getAndIncrement() + " time(s) before.");
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(waitSeconds));
        } catch (InterruptedException e) {
            System.err.println(name + " was interrupted.");
            Thread.currentThread().interrupt();
        } finally {
            System.out.println(name + " relinquishing leadership.\n");
        }
    }
}
