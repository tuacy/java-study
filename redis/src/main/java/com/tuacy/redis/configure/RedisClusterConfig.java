package com.tuacy.redis.configure;

import com.tuacy.redis.condition.ConditionalOnPropertyExist;
import com.tuacy.redis.condition.ConditionalOnSystem;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @name: RedisClusterConfig
 * @author: tuacy.
 * @date: 2019/11/12.
 * @version: 1.0
 * @Description:
 */
@Configuration
@ConditionalOnSystem(type = ConditionalOnSystem.SystemType.WINDOWS)
@ConditionalOnPropertyExist(name = "spring.redis.sentinel.nodes", exist = false)
public class RedisClusterConfig {

    @Bean
    public StringRedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);
        template.setEnableTransactionSupport(true); //开启 @Transactional 支持
        return template;
    }

}
