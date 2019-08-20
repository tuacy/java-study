package com.tuacy.study.distributelock.distributedlock.db.optimistic;

import com.google.common.util.concurrent.Uninterruptibles;
import com.tuacy.study.distributelock.ResourceApplicationContext;
import com.tuacy.study.distributelock.dao.IOptimisticLockDao;
import com.tuacy.study.distributelock.dao.impl.OptimisticLockDaoImpl;
import com.tuacy.study.distributelock.model.OptimisticLock;

import java.util.concurrent.TimeUnit;

/**
 * @name: SqlOptimisticLock
 * @author: tuacy.
 * @date: 2019/8/6.
 * @version: 1.0
 * @Description: 数据库乐观锁 -- 这里我们简单的模拟一个存钱的场景
 */
public class SqlOptimisticLock {

    /**
     * 因为数据库里面限制了最大长度了
     */
    private static final int LOCK_LOOP_TIME_WAIT = 200;
    /**
     * lock对应的资源名字
     */
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
        boolean success = false;
        while (!success) {
            try {
                // 第一步：版本号信息很重要
                OptimisticLock dbItemInfo = optimisticLockDao.selectLockResourceInfo(resourceName);
                // 第二步：做相应的逻辑处理
                // 第三步：
                Uninterruptibles.sleepUninterruptibly(2, TimeUnit.SECONDS);
                if (dbItemInfo == null) {
                    success = optimisticLockDao.insertLockResourceValue(resourceName, money);
                } else {
                    // 更新的时候会去做下版本的判断，相同才更新，不相同不更新
                    success = optimisticLockDao.updateLockResourceValue(resourceName, dbItemInfo.getVersion(), dbItemInfo.getValue() + money);
                }
                if (!success) {
                    Uninterruptibles.sleepUninterruptibly(LOCK_LOOP_TIME_WAIT, TimeUnit.MILLISECONDS);
                }
            } catch (Exception e) {
//                e.printStackTrace();
                success = false;
                Uninterruptibles.sleepUninterruptibly(LOCK_LOOP_TIME_WAIT, TimeUnit.MILLISECONDS);

            }

        }
    }

}
