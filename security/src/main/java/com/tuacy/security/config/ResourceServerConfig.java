package com.tuacy.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

/**
 * @name: ResourceServerConfig
 * @author: tuacy.
 * @date: 2019/11/28.
 * @version: 1.0
 * @Description:
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    /**
     * 这里设置需要token验证的url
     * 这些url需要在WebSecurityConfigurerAdapter中排掉
     * 否则进入WebSecurityConfigurerAdapter,进行的是basic auth或表单认证,而不是token认证
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.requestMatchers().antMatchers("/hi")
                .and()
                .authorizeRequests()
                .antMatchers("/hi").authenticated();
    }

}
