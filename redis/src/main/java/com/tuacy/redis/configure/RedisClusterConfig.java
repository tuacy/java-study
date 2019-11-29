package com.tuacy.redis.configure;

import com.tuacy.redis.condition.ConditionalOnPropertyExist;
import com.tuacy.redis.condition.ConditionalOnSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.JedisCluster;

import java.lang.reflect.Field;

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

    private JedisConnectionFactory jedisConnectionFactory;

    @Autowired
    public void setJedisConnectionFactory(JedisConnectionFactory jedisConnectionFactory) {
        this.jedisConnectionFactory = jedisConnectionFactory;
    }

    /**
     * 通过反射获取spring管理的JedisCluster对象
     */
    @Bean
    public JedisCluster jedisCluster() {
        return (JedisCluster) getValue(jedisConnectionFactory, getField(JedisConnectionFactory.class, "cluster"));
    }

    private static Field getField(Class<?> cls, String fieldName) {
        try {
            Field field = cls.getDeclaredField(fieldName);
            field.setAccessible(true);

            return field;
        } catch (NoSuchFieldException | SecurityException e) {
            throw new RuntimeException("cannot find or access field '" + fieldName + "' from " + cls.getName(), e);
        }
    }

    @SuppressWarnings({"unchecked"})
    private static <T> T getValue(Object obj, Field field) {
        try {
            return (T) field.get(obj);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
