package com.tuacy.study.distributelock.mapper;

import com.tuacy.study.distributelock.model.UnionKeyLock;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UnionKeyLockMapper {

    /**
     * 根据资源名字获取对应的信息
     *
     * @param resourceName 数据库记录信息
     */
    UnionKeyLock getLockInfoByResourceName(@Param("resourceName") String resourceName);

    /**
     * 插入
     *
     * @param info 信息
     * @return 插入的条数
     */
    Integer insertLockInfo(@Param("info") UnionKeyLock info);

    /**
     * 可重入锁，加锁count+1
     *
     * @param resourceName 锁对应的资源信息
     * @param nodeInfo     节点信息
     * @param count        锁的次数
     * @return 更新条数
     */
    Integer reentrantLock(@Param("resourceName") String resourceName,
                          @Param("nodeInfo") String nodeInfo,
                          @Param("count") int count);

    /**
     * 可重入锁，释放count-1
     *
     * @param resourceName 锁对应的资源信息
     * @param nodeInfo     节点信息
     * @param count        锁的次数
     * @return 更新条数
     */
    Integer reentrantUnLock(@Param("resourceName") String resourceName,
                          @Param("nodeInfo") String nodeInfo,
                          @Param("count") int count);

    /**
     * 插入
     *
     * @param resourceName 锁对应的资源信息
     * @param nodeInfo     节点信息
     */
    Integer deleteLockInfo(@Param("resourceName") String resourceName,
                           @Param("nodeInfo") String nodeInfo);

}
