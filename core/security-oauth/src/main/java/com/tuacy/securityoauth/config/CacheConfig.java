package com.tuacy.securityoauth.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.SimpleCacheErrorHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;

/**
 * Spring cache的一些配置，建议组件相关配置都放在相应的configuration类中
 */

/***
 @Service public class CacheDemoService {

 @Cacheable(key = "'key'", cacheManager = CacheManagerConfig.CacheManagerNames.EHCACHE_CACHE_MAANGER, cacheNames = CacheManagerConfig.EhCacheNames.CACHE_10MINS)
 public String demo(String key) {
 return "abc" + key;
 }

 //@Cacheable(key = "'key'", cacheNames = CacheManagerConfig.CacheNames.CACHE_15MINS)
 @Cacheable(key = "'key'",cacheManager = CacheManagerConfig.CacheManagerNames.REDIS_CACHE_MANAGER,  cacheNames = CacheManagerConfig.CacheNames.CACHE_15MINS)
 public String demo2(String key) {
 return "abcdemo2" + key;
 }
 }
 ***/
@Configuration
@ConditionalOnBean(RedisCacheManager.class)
public class CacheConfig extends CachingConfigurerSupport {

    @Autowired
    private RedisCacheManager redisCacheManager;

    /**
     * 重写这个方法，目的是用以提供默认的cacheManager
     */
    @Override
    public CacheManager cacheManager() {
        return redisCacheManager;
    }

    /**
     * 如果cache出错， 我们会记录在日志里，方便排查，比如反序列化异常
     */
    @Override
    public CacheErrorHandler errorHandler() {
        return new LoggingCacheErrorHandler();
    }


    /* non-public */ static class LoggingCacheErrorHandler extends SimpleCacheErrorHandler {
        private final Logger logger = LoggerFactory.getLogger(this.getClass());

        @Override
        public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
            logger.error(String.format("cacheName:%s,cacheKey:%s",
                    cache == null ? "unknown" : cache.getName(), key), exception);
            super.handleCacheGetError(exception, cache, key);
        }

        @Override
        public void handleCachePutError(RuntimeException exception, Cache cache, Object key,
                                        Object value) {
            logger.error(String.format("cacheName:%s,cacheKey:%s",
                    cache == null ? "unknown" : cache.getName(), key), exception);
            super.handleCachePutError(exception, cache, key, value);
        }

        @Override
        public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {
            logger.error(String.format("cacheName:%s,cacheKey:%s",
                    cache == null ? "unknown" : cache.getName(), key), exception);
            super.handleCacheEvictError(exception, cache, key);
        }

        @Override
        public void handleCacheClearError(RuntimeException exception, Cache cache) {
            logger.error(String.format("cacheName:%s", cache == null ? "unknown" : cache.getName()),
                    exception);
            super.handleCacheClearError(exception, cache);
        }
    }

}
