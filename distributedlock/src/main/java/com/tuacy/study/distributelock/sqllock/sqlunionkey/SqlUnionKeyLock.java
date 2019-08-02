package com.tuacy.study.distributelock.sqllock.sqlunionkey;

import com.tuacy.study.distributelock.dao.IUnionKeyLockDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SqlUnionKeyLock {

    private IUnionKeyLockDao unionKeyLockDao;

    @Autowired
    public void setUnionKeyLockDao(IUnionKeyLockDao unionKeyLockDao) {
        this.unionKeyLockDao = unionKeyLockDao;
    }

    public void lock() {

    }

    public void unlock() {

    }

}
