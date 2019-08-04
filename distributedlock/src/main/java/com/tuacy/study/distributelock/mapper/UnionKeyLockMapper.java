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

}
