package com.tuacy.study.distributelock.mapper;

import com.tuacy.study.distributelock.model.OptimisticLock;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OptimisticLockMapper {

    /**
     * 查询
     *
     * @param resourceName 锁定的资源
     * @return 对应记录信息
     */
    OptimisticLock selectLockResourceInfo(@Param("resourceName") String resourceName);

    /**
     * 插入
     *
     * @param resourceName 锁定的资源
     * @param value        锁定的资源对应的值
     * @return 插入条数
     */
    Integer insertLockResourceValue(@Param("resourceName") String resourceName, @Param("value") int value);


    /**
     * 更新
     *
     * @param resourceName 锁定的资源
     * @param version      版本
     * @param value        锁定的资源对应的值
     * @return 更新条数
     */
    Integer updateLockResourceValue(@Param("resourceName") String resourceName, @Param("version") int version, @Param("value") int value);
}
