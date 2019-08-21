package com.tuacy.study.distributelock.distributedlock.db.unionkey;

import com.google.common.util.concurrent.Uninterruptibles;
import com.tuacy.study.distributelock.dao.IUnionKeyLockDao;
import com.tuacy.study.distributelock.distributedlock.db.AbstractDbDistributedLock;
import com.tuacy.study.distributelock.model.UnionKeyLock;
import com.tuacy.study.distributelock.utils.ComputerIdentifierUtil;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 通过数据库的唯一主键实现数据库分布式锁。如果有多个请求同时提交到数据库的话，数据库会保证只有一个操作可以成功，那么我们可以认为操作成功的那个线程获得了该方法的锁
 * 1. 可重入锁
 * 2. 非公平锁
 */
public class DbDistributedUnionKeyLock extends AbstractDbDistributedLock {
    /**
     * 因为数据库里面限制了最大长度了
     */
    private static final int NODE_INFO_MAX_LENGTH = 120;

    /**
     * 计算机唯一标识 为可重入锁做准备(一定要确保每个机器的值不同)
     */
    private static final String COMPUTER_UUID = ComputerIdentifierUtil.getComputerIdentifier();
    /**
     * 线程变量
     */
    private ThreadLocal<String> threadFlag = new ThreadLocal<>();
    /**
     * 操作数据库的dao
     */
    private IUnionKeyLockDao unionKeyLockDao;

    public DbDistributedUnionKeyLock(IUnionKeyLockDao unionKeyLockDao) {
        this.unionKeyLockDao = unionKeyLockDao;
    }

    /**
     * 加锁
     */
    @Override
    public boolean lock(String key, int retryTimes, long sleepMillis) {
        boolean lockSuccess = false;
        // 机器码+线程uuid -- 唯一标识（保证同一台电脑的同一个线程是一样的）
        if (threadFlag.get() == null || threadFlag.get().isEmpty()) {
            String nodeTemp = COMPUTER_UUID + "#" + String.format("%08x", UUID.randomUUID().hashCode()) + "#" + Thread.currentThread().getId();
            if (nodeTemp.length() > NODE_INFO_MAX_LENGTH) {
                nodeTemp = nodeTemp.substring(0, NODE_INFO_MAX_LENGTH);
            }
            threadFlag.set(nodeTemp);
        }
        int retry = 0;
        while (!lockSuccess && retry < retryTimes) {
            try {
                UnionKeyLock lockInfo = unionKeyLockDao.getLockInfoByResourceName(key);
                if (lockInfo == null) {
                    // 当前资源没有被加锁
                    lockSuccess = unionKeyLockDao.insertLockInfo(key, threadFlag.get());
                } else {
                    // 当前资源已经被加锁,这个时候需要考虑是否可重入
                    // 可重入锁
                    if (lockInfo.getNodeInfo() != null && lockInfo.getNodeInfo().equals(threadFlag.get())) {
                        // 重入
                        lockSuccess = unionKeyLockDao.reentrantLock(lockInfo.getResourceName(), threadFlag.get(), lockInfo.getCount());
                    } else {
                        // 如果出现这种情况代表数据已经有问题了
                        lockSuccess = false;
                    }
                }
                if (!lockSuccess) {
                    // 等待200毫秒
                    Uninterruptibles.sleepUninterruptibly(sleepMillis, TimeUnit.MILLISECONDS);
                }
            } catch (Exception e) {
                // 等待指定时间
                Uninterruptibles.sleepUninterruptibly(sleepMillis, TimeUnit.MILLISECONDS);
            } finally {
                retry++;
            }
        }
        return lockSuccess;
    }


    /**
     * 释放
     */
    @Override
    public void unlock(String key) {
        if (threadFlag.get() == null || threadFlag.get().isEmpty()) {
            return;
        }
        boolean unlockSuccess = false;
        while (!unlockSuccess) {
            try {
                UnionKeyLock lockInfo = unionKeyLockDao.getLockInfoByResourceName(key);
                if (lockInfo == null) {
                    return;
                }
                if (lockInfo.getNodeInfo() == null) {
                    unionKeyLockDao.deleteLockInfo(lockInfo.getResourceName(), lockInfo.getNodeInfo());
                    return;
                }
                if (!lockInfo.getNodeInfo().equals(threadFlag.get())) {
                    return;
                }
                // 可重入锁
                if (lockInfo.getCount() == 1) {
                    unionKeyLockDao.deleteLockInfo(lockInfo.getResourceName(), threadFlag.get());
                    unlockSuccess = true;
                } else {
                    if (lockInfo.getNodeInfo() != null && lockInfo.getNodeInfo().equals(threadFlag.get())) {
                        // 重入
                        unlockSuccess = unionKeyLockDao.reentrantUnLock(lockInfo.getResourceName(), threadFlag.get(), lockInfo.getCount());
                    } else {
                        // 如果出现这种情况，代表这时候数据已经有问题了
                        unionKeyLockDao.deleteLockInfo(lockInfo.getResourceName(), lockInfo.getNodeInfo());
                        unlockSuccess = true;
                    }
                }
                if (!unlockSuccess) {
                    // 等待200毫秒
                    Uninterruptibles.sleepUninterruptibly(100, TimeUnit.MILLISECONDS);
                }
            } catch (Exception e) {
//                e.printStackTrace();
                // 等待200毫秒
                Uninterruptibles.sleepUninterruptibly(100, TimeUnit.MILLISECONDS);
            }

        }

    }

}
