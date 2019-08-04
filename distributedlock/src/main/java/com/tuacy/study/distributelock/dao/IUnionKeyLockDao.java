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
     * 插入
     */
    int insertLockInfo(String resourceName, String nodeInfo);

    /**
     * 插入
     */
    int insertLockInfo(UnionKeyLock info);

}
