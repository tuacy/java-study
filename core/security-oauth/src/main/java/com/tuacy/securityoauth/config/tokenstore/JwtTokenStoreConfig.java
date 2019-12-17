package com.tuacy.securityoauth.config.tokenstore;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * @name: JwtTokenStoreConfig
 * @author: tuacy.
 * @date: 2019/12/6.
 * @version: 1.0
 * @Description:
 */
@Configuration
@ConditionalOnProperty(prefix = "token-store", name = "type", havingValue = "jwt")
public class JwtTokenStoreConfig {

    /**
     * AccessToken转换器-定义token的生成方式，这里使用JWT生成token，对称加密只需要加入key等其他信息（自定义）
     */
    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey("123");
        return converter;
    }

    /**
     * 设置保存token的方式，一共有五种，Redis的方式
     */
    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

}
