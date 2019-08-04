package com.tuacy.study.distributelock.dao.impl;

import com.tuacy.study.distributelock.dao.IUnionKeyLockDao;
import com.tuacy.study.distributelock.mapper.UnionKeyLockMapper;
import com.tuacy.study.distributelock.model.UnionKeyLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Repository
public class UnionKeyLockDaoImpl implements IUnionKeyLockDao {

    private UnionKeyLockMapper unionKeyLockMapper;

    @Autowired
    public void setUnionKeyLockMapper(UnionKeyLockMapper unionKeyLockMapper) {
        this.unionKeyLockMapper = unionKeyLockMapper;
    }

    /**
     * 查询操作就不需要事务处理了
     */
    @Override
    public UnionKeyLock getLockInfoByResourceName(String resourceName) {
        return unionKeyLockMapper.getLockInfoByResourceName(resourceName);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int insertLockInfo(String resourceName, String nodeInfo) {
        UnionKeyLock lockInfo = new UnionKeyLock();
        lockInfo.setResourceName(resourceName);
        lockInfo.setNodeInfo(nodeInfo);
        lockInfo.setCount(1);
        lockInfo.setDes(resourceName);
        lockInfo.setCreateTime(new Date());
        lockInfo.setUpdateTime(new Date());
        return insertLockInfo(lockInfo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int insertLockInfo(UnionKeyLock info) {
        return unionKeyLockMapper.insertLockInfo(info);
    }


}
