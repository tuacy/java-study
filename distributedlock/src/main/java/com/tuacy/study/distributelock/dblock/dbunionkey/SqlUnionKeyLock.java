package com.tuacy.study.distributelock.dblock.dbunionkey;

import com.tuacy.study.distributelock.ResourceApplicationContext;
import com.tuacy.study.distributelock.dao.IUnionKeyLockDao;
import com.tuacy.study.distributelock.dao.impl.UnionKeyLockDaoImpl;
import com.tuacy.study.distributelock.model.UnionKeyLock;

public class SqlUnionKeyLock {

    private static final String COMPUTER_UUID = "abc";

    /**
     * 操作数据库的dao
     */
    private IUnionKeyLockDao unionKeyLockDao;
    /**
     * lock资源名
     */
    private String resourceName;
    /**
     * 是否可重入
     */
    private boolean reentrant;


    /**
     * 用于注入无法自动装配的Bean,处理@Autowired无效的问题
     */
    private void inject() {
        if (null == this.unionKeyLockDao) {
            this.unionKeyLockDao = ResourceApplicationContext.getApplicationContext().getBean(UnionKeyLockDaoImpl.class);
        }
    }


    public SqlUnionKeyLock(String resourceName) {
        this(resourceName, true);
    }

    public SqlUnionKeyLock(String resourceName, boolean reentrant) {
        this.resourceName = resourceName;
        this.reentrant = reentrant;
        inject();
    }

    /**
     * 对资源进行加锁
     */
    public boolean lock() {

        boolean isLock = false;
        UnionKeyLock lockInfo = unionKeyLockDao.getLockInfoByResourceName(this.resourceName);
        if (lockInfo == null) {
            /**
             * 当前资源没有被加锁
             */
            int insertNum = unionKeyLockDao.insertLockInfo(resourceName, COMPUTER_UUID);
            if (insertNum == 1) {
                // 插入成功
                isLock = true;
            }
            return isLock;
        } else {
            /**
             * 当前资源已经被加锁
             */
        }
        return true;
    }

    /**
     * 是否锁
     */
    public void unlock() {

    }

}
