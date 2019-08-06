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

    /**
     * 删除
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteLockInfo(String resourceName, String nodeInfo) {
        unionKeyLockMapper.deleteLockInfo(resourceName, nodeInfo);
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean insertLockInfo(String resourceName, String nodeInfo) {
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
    public boolean insertLockInfo(UnionKeyLock info) {
        return unionKeyLockMapper.insertLockInfo(info) == 1;
    }

    /**
     * 可重入锁 -- 加锁
     *
     * @param resourceName 锁对应的资源
     * @param nodeInfo     节点信息（计算机+线程信息）
     * @param count        之前加锁次数
     * @return 是否修改成功
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean reentrantLock(String resourceName, String nodeInfo, int count) {
        int updateCount = unionKeyLockMapper.reentrantLock(resourceName, nodeInfo, count);
        return updateCount == 1;
    }

    /**
     * 可重入锁 -- 释放
     *
     * @param resourceName 锁对应的资源
     * @param nodeInfo     节点信息（计算机+线程信息）
     * @param count        之前加锁次数
     * @return 是否修改成功
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean reentrantUnLock(String resourceName, String nodeInfo, int count) {
        int updateCount = unionKeyLockMapper.reentrantUnLock(resourceName, nodeInfo, count);
        return updateCount == 1;
    }


}
