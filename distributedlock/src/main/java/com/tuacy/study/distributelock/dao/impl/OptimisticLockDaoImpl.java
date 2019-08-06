package com.tuacy.study.distributelock.dao.impl;

import com.tuacy.study.distributelock.dao.IOptimisticLockDao;
import com.tuacy.study.distributelock.mapper.OptimisticLockMapper;
import com.tuacy.study.distributelock.model.OptimisticLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @name: OptimisticLockDaoImpl
 * @author: tuacy.
 * @date: 2019/8/6.
 * @version: 1.0
 * @Description:
 */
@Repository
public class OptimisticLockDaoImpl implements IOptimisticLockDao {

    private OptimisticLockMapper optimisticLockMapper;

    @Autowired
    public void setOptimisticLockMapper(OptimisticLockMapper optimisticLockMapper) {
        this.optimisticLockMapper = optimisticLockMapper;
    }

    @Override
    public OptimisticLock selectLockResourceInfo(String resourceName) {
        return optimisticLockMapper.selectLockResourceInfo(resourceName);
    }

    @Override
    public boolean insertLockResourceValue(String resourceName, int value) {
        return optimisticLockMapper.insertLockResourceValue(resourceName, value) == 1;
    }

    @Override
    public boolean updateLockResourceValue(String resourceName, int flagVersion, int value) {
        return optimisticLockMapper.updateLockResourceValue(resourceName, flagVersion, value) == 1;
    }
}
