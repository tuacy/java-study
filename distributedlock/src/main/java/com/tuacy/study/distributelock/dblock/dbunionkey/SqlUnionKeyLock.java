package com.tuacy.study.distributelock.dblock.dbunionkey;

import com.google.common.util.concurrent.Uninterruptibles;
import com.tuacy.study.distributelock.ResourceApplicationContext;
import com.tuacy.study.distributelock.dao.IUnionKeyLockDao;
import com.tuacy.study.distributelock.dao.impl.UnionKeyLockDaoImpl;
import com.tuacy.study.distributelock.dblock.IDistributedLock;
import com.tuacy.study.distributelock.model.UnionKeyLock;
import com.tuacy.study.distributelock.utils.ComputerIdentifierUtil;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 1. 可重入锁
 * 2. 非公平锁
 */
public class SqlUnionKeyLock implements IDistributedLock {
    /**
     * 因为数据库里面限制了最大长度了
     */
    private static final int LOCK_LOOP_TIME_WAIT = 200;
    /**
     * 因为数据库里面限制了最大长度了
     */
    private static final int NODE_INFO_MAX_LENGTH = 120;

    /**
     * 计算机唯一标识 为可重入锁做准备
     */
    private static final String COMPUTER_UUID = ComputerIdentifierUtil.getComputerIdentifier();
    /**
     * 进程唯一编号
     */
    private static final String PROCESS_UUID = String.format("%08x", UUID.randomUUID().hashCode());
    /**
     * 可重入关键字
     */
    private final String nodeInfo;

    /**
     * 操作数据库的dao
     */
    private IUnionKeyLockDao unionKeyLockDao;
    /**
     * lock资源名
     */
    private String resourceName;


    /**
     * 用于注入无法自动装配的Bean,处理@Autowired无效的问题
     */
    private void inject() {
        if (null == this.unionKeyLockDao) {
            this.unionKeyLockDao = ResourceApplicationContext.getApplicationContext().getBean(UnionKeyLockDaoImpl.class);
        }
    }


    /**
     * 构造函数
     *
     * @param resourceName lock对应资源名字
     */
    public SqlUnionKeyLock(String resourceName) {
        this.resourceName = resourceName;
        String nodeTemp = COMPUTER_UUID + "#" + PROCESS_UUID + "#" + Thread.currentThread().getId();
        if (nodeTemp.length() > NODE_INFO_MAX_LENGTH) {
            nodeTemp = nodeTemp.substring(0, NODE_INFO_MAX_LENGTH);
        }
        this.nodeInfo = nodeTemp;
        inject();
    }

    /**
     * 对资源进行加锁
     */
    @Override
    public void lock() {
        boolean lockSuccess = false;
        while (!lockSuccess) {
            try {
                UnionKeyLock lockInfo = unionKeyLockDao.getLockInfoByResourceName(this.resourceName);
                if (lockInfo == null) {
                    // 当前资源没有被加锁
                    lockSuccess = unionKeyLockDao.insertLockInfo(resourceName, nodeInfo);
                } else {
                    // 当前资源已经被加锁,这个时候需要考虑是否可重入
                    // 可重入锁
                    if (lockInfo.getNodeInfo() != null && lockInfo.getNodeInfo().equals(nodeInfo)) {
                        // 重入
                        lockSuccess = unionKeyLockDao.reentrantLock(lockInfo.getResourceName(), nodeInfo, lockInfo.getCount());
                    } else {
                        // 如果出现这种情况代表数据已经有问题了
                        lockSuccess = false;
                    }
                }
                if (!lockSuccess) {
                    // 等待200毫秒
                    Uninterruptibles.sleepUninterruptibly(LOCK_LOOP_TIME_WAIT, TimeUnit.MILLISECONDS);
                }
            } catch (Exception e) {
//                e.printStackTrace();
                // 等待200毫秒
                Uninterruptibles.sleepUninterruptibly(LOCK_LOOP_TIME_WAIT, TimeUnit.MILLISECONDS);
            }
        }

    }

    /**
     * 释放
     */
    @Override
    public void unlock() {
        boolean unlockSuccess = false;
        while (!unlockSuccess) {
            try {
                UnionKeyLock lockInfo = unionKeyLockDao.getLockInfoByResourceName(this.resourceName);
                if (lockInfo == null) {
                    return;
                }
                if (lockInfo.getNodeInfo() == null) {
                    unionKeyLockDao.deleteLockInfo(lockInfo.getResourceName(), lockInfo.getNodeInfo());
                    return;
                }
                if (!lockInfo.getNodeInfo().equals(nodeInfo)) {
                    return;
                }
                // 可重入锁
                if (lockInfo.getCount() == 1) {
                    unionKeyLockDao.deleteLockInfo(lockInfo.getResourceName(), nodeInfo);
                    unlockSuccess = true;
                } else {
                    if (lockInfo.getNodeInfo() != null && lockInfo.getNodeInfo().equals(nodeInfo)) {
                        // 重入
                        unlockSuccess = unionKeyLockDao.reentrantUnLock(lockInfo.getResourceName(), nodeInfo, lockInfo.getCount());
                    } else {
                        // 如果出现这种情况，代表这时候数据已经有问题了
                        unionKeyLockDao.deleteLockInfo(lockInfo.getResourceName(), lockInfo.getNodeInfo());
                        unlockSuccess = true;
                    }
                }
                if (!unlockSuccess) {
                    // 等待200毫秒
                    Uninterruptibles.sleepUninterruptibly(LOCK_LOOP_TIME_WAIT, TimeUnit.MILLISECONDS);
                }
            } catch (Exception e) {
//                e.printStackTrace();
                // 等待200毫秒
                Uninterruptibles.sleepUninterruptibly(LOCK_LOOP_TIME_WAIT, TimeUnit.MILLISECONDS);
            }

        }

    }

    @Override
    public boolean tryLock() {
        boolean lockSuccess = false;
        try {
            UnionKeyLock lockInfo = unionKeyLockDao.getLockInfoByResourceName(this.resourceName);
            if (lockInfo == null) {
                // 当前资源没有被加锁
                lockSuccess = unionKeyLockDao.insertLockInfo(resourceName, nodeInfo);
            } else {
                // 当前资源已经被加锁,这个时候需要考虑是否可重入
                // 可重入锁
                if (lockInfo.getNodeInfo() != null && lockInfo.getNodeInfo().equals(nodeInfo)) {
                    // 重入
                    lockSuccess = unionKeyLockDao.reentrantLock(lockInfo.getResourceName(), nodeInfo, lockInfo.getCount());
                } else {
                    // 如果出现这种情况代表数据已经有问题了
                    lockSuccess = false;
                }
            }
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return lockSuccess;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) {
        boolean lockSuccess = false;
        // 截至时间
        long endTime = System.nanoTime() + unit.toNanos(time);
        while (!lockSuccess) {
            try {
                UnionKeyLock lockInfo = unionKeyLockDao.getLockInfoByResourceName(this.resourceName);
                if (lockInfo == null) {
                    // 当前资源没有被加锁
                    lockSuccess = unionKeyLockDao.insertLockInfo(resourceName, nodeInfo);
                } else {
                    // 当前资源已经被加锁,这个时候需要考虑是否可重入
                    // 可重入锁
                    if (lockInfo.getNodeInfo() != null && lockInfo.getNodeInfo().equals(nodeInfo)) {
                        // 重入
                        lockSuccess = unionKeyLockDao.reentrantLock(lockInfo.getResourceName(), nodeInfo, lockInfo.getCount());
                    } else {
                        // 如果出现这种情况代表数据已经有问题了
                        lockSuccess = false;
                    }
                }
                if (!lockSuccess) {
                    long leftTime = endTime - System.nanoTime();
                    if (leftTime <= 0) {
                        // 超过时间了
                        return false;
                    } else if (endTime - System.nanoTime() > LOCK_LOOP_TIME_WAIT) {
                        // 等待200毫秒
                        Uninterruptibles.sleepUninterruptibly(LOCK_LOOP_TIME_WAIT, TimeUnit.MILLISECONDS);
                    } else {
                        Uninterruptibles.sleepUninterruptibly(leftTime, TimeUnit.MILLISECONDS);
                    }
                }
            } catch (Exception e) {
//                e.printStackTrace();
                // 等待200毫秒
                Uninterruptibles.sleepUninterruptibly(LOCK_LOOP_TIME_WAIT, TimeUnit.MILLISECONDS);
            }
        }
        return true;
    }

}
