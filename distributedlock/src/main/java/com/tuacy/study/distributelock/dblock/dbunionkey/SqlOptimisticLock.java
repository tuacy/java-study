package com.tuacy.study.distributelock.dblock.dbunionkey;

import com.tuacy.study.distributelock.ResourceApplicationContext;
import com.tuacy.study.distributelock.dao.IOptimisticLockDao;
import com.tuacy.study.distributelock.dao.impl.OptimisticLockDaoImpl;
import com.tuacy.study.distributelock.model.OptimisticLock;

/**
 * @name: SqlOptimisticLock
 * @author: tuacy.
 * @date: 2019/8/6.
 * @version: 1.0
 * @Description: 数据库乐观锁 -- 这里我们简单的模拟一个存钱的场景
 */
public class SqlOptimisticLock {

    private final String resourceName;
    /**
     * 操作数据库的dao
     */
    private IOptimisticLockDao optimisticLockDao;

    /**
     * 用于注入无法自动装配的Bean,处理@Autowired无效的问题
     */
    private void inject() {
        if (null == this.optimisticLockDao) {
            this.optimisticLockDao = ResourceApplicationContext.getApplicationContext().getBean(OptimisticLockDaoImpl.class);
        }
    }

    /**
     * 构造函数
     *
     * @param resourceName lock对应资源名字
     */
    public SqlOptimisticLock(String resourceName) {
        this.resourceName = resourceName;
        inject();
    }

    /**
     * 我们简单的模拟一个存钱的操作
     *
     * @param money 存入金额
     */
    public void depositMoney(int money) {
        // 第一步：版本号信息很重要
        OptimisticLock dbItemInfo = optimisticLockDao.selectLockResourceInfo(resourceName);
        // 第二步：
        // 第三步：
    }

}
