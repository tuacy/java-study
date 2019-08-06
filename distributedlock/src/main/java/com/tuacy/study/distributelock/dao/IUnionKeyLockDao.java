package com.tuacy.study.distributelock.dao;

import com.tuacy.study.distributelock.model.UnionKeyLock;

public interface IUnionKeyLockDao {

    /**
     * 根据资源名字去获取数据库里面对应的信息
     *
     * @param resourceName 资源名字
     * @return 数据库记录信息
     */
    UnionKeyLock getLockInfoByResourceName(String resourceName);

    /**
     * 删除
     */
    boolean deleteLockInfo(String resourceName, String nodeInfo);

    /**
     * 插入
     */
    boolean insertLockInfo(String resourceName, String nodeInfo);

    /**
     * 插入
     */
    boolean insertLockInfo(UnionKeyLock info);

    /**
     * 针对可重入锁做相应的
     *
     * @param resourceName 锁对应的资源
     * @param nodeInfo     节点信息（计算机+线程信息）
     * @param count        之前加锁次数
     * @return 是否修改成功
     */
    boolean reentrantLock(String resourceName, String nodeInfo, int count);

    /**
     * 针对可重入锁做相应的
     *
     * @param resourceName 锁对应的资源
     * @param nodeInfo     节点信息（计算机+线程信息）
     * @param count        之前加锁次数
     * @return 是否修改成功
     */
    boolean reentrantUnLock(String resourceName, String nodeInfo, int count);

}
