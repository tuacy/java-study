package com.tuacy.study.distributelock.config;

import com.tuacy.study.distributelock.dao.IUnionKeyLockDao;
import com.tuacy.study.distributelock.distributedlock.db.IDbDistributedLock;
import com.tuacy.study.distributelock.distributedlock.db.unionkey.SqlUnionKeyLock;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfigureAfter(MybatisAutoConfiguration.class)
public class DbDistributedLockConfiguration {

    @Bean
    @ConditionalOnBean(IUnionKeyLockDao.class)
    public IDbDistributedLock dbDistributedLock(IUnionKeyLockDao unionKeyLockDao) {
        return new SqlUnionKeyLock(unionKeyLockDao);
    }
}
