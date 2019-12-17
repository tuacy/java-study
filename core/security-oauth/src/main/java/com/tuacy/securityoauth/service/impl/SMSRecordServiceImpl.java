package com.tuacy.securityoauth.service.impl;

import com.tuacy.securityoauth.config.CacheManagerConfig;
import com.tuacy.securityoauth.service.SMSRecordService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @name: SMSRecordServiceImpl
 * @author: tuacy.
 * @date: 2019/11/29.
 * @version: 1.0
 * @Description:
 */
@CacheConfig(cacheNames = CacheManagerConfig.EhCacheNames.CACHE_10MINS)
@Service
public class SMSRecordServiceImpl implements SMSRecordService {

    @CachePut(key = "#userName")
    @Override
    public String saveSMSCode(String userName, String smsCode) {
        return smsCode;
    }

    /**
     * Cacheable 注解会先查询是否已经有缓存，有会使用缓存，没有则会执行方法并缓存
     * 如果没有指定key则默认使用参数作为key
     */
    @Cacheable(key = "#userName")
    @Override
    public String getSMSCode(String userName) {
        return null;
    }

    @CacheEvict(key = "#userName", beforeInvocation = true)
    @Override
    public void clearSMSCode(String userName) {

    }
}
