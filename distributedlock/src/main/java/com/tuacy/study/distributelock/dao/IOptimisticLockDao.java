package com.tuacy.study.distributelock.dao;

import com.tuacy.study.distributelock.model.OptimisticLock;

public interface IOptimisticLockDao {

    /**
     * 查询
     *
     * @param resourceName 锁定的资源
     * @return 数据库对应的信息
     */
    OptimisticLock selectLockResourceInfo(String resourceName);

    /**
     * 插入
     *
     * @param resourceName 锁定的资源
     * @param value        锁定的资源对应的值
     * @return 是否成功
     */
    boolean insertLockResourceValue(String resourceName, int value);

    /**
     * 更新
     *
     * @param resourceName 锁定的资源
     * @param flagVersion  版本号
     * @param value        锁定的资源对应的值
     * @return 是否成功
     */
    boolean updateLockResourceValue(String resourceName, int flagVersion, int value);

}
