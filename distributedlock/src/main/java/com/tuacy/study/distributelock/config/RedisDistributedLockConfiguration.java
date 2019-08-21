package com.tuacy.study.distributelock.config;

import com.tuacy.study.distributelock.distributedlock.redis.IRedisDistributedLock;
import com.tuacy.study.distributelock.distributedlock.redis.RedisDistributedLockImpl;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class RedisDistributedLockConfiguration {

    @Bean
    @ConditionalOnBean(RedisTemplate.class)
    public IRedisDistributedLock redisDistributedLock(RedisTemplate<Object, Object> redisTemplate) {
        return new RedisDistributedLockImpl(redisTemplate);
    }
}
