package com.tuacy.study.distributelock.distributedlock.db.exclusive;

import com.google.common.util.concurrent.Uninterruptibles;
import com.tuacy.study.distributelock.ResourceApplicationContext;
import com.tuacy.study.distributelock.distributedlock.IDistributedLock;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

/**
 * @name: SqlExclusiveLock
 * @author: tuacy.
 * @date: 2019/8/6.
 * @version: 1.0
 * @Description: 排他锁 实现分布式锁
 */
public class SqlExclusiveLock implements IDistributedLock {

    /**
     * 因为数据库里面限制了最大长度了
     */
    private static final int LOCK_LOOP_TIME_WAIT = 200;

    private JdbcTemplate jdbcTemplate;
    /**
     * lock对应的资源名字
     */
    private final String resourceName;

    private Connection sqlConnection;

    /**
     * 用于注入无法自动装配的Bean,处理@Autowired无效的问题
     */
    private void inject() {
        if (null == this.jdbcTemplate) {
            this.jdbcTemplate = ResourceApplicationContext.getApplicationContext().getBean(JdbcTemplate.class);
        }
    }

    /**
     * 构造函数
     *
     * @param resourceName lock对应资源名字
     */
    public SqlExclusiveLock(String resourceName) {
        this.resourceName = resourceName;
        inject();
    }

    @Override
    public void lock() {
        if (jdbcTemplate.getDataSource() == null) {
            throw new IllegalArgumentException("数据库配置失败!");
        }
        boolean success = false;
        while (!success) {
            PreparedStatement preparedStatement = null;
            try {
                sqlConnection = jdbcTemplate.getDataSource().getConnection();
                sqlConnection.setAutoCommit(false);//设置手动提交
                String prepareSql = "select * from exclusivelock where resource_name = ? for update";
                preparedStatement = sqlConnection.prepareStatement(prepareSql);
                preparedStatement.setString(1, this.resourceName);
                success = preparedStatement.executeQuery() != null;
                if (!success) {
                    Uninterruptibles.sleepUninterruptibly(LOCK_LOOP_TIME_WAIT, TimeUnit.MILLISECONDS);
                }
            } catch (Exception e) {
                e.printStackTrace();
                success = false;
                Uninterruptibles.sleepUninterruptibly(LOCK_LOOP_TIME_WAIT, TimeUnit.MILLISECONDS);
            } finally {
                if (preparedStatement != null) {
                    try {
                        preparedStatement.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public void unlock() {
        if (sqlConnection != null) {
            try {
                sqlConnection.commit();
            } catch (Exception e) {
                // ignore
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean tryLock() {
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) {
        return false;
    }
}
