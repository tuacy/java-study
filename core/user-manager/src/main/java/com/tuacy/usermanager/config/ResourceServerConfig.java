package com.tuacy.usermanager.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;


@Configuration()
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {

        http.
                csrf().disable()
                .authorizeRequests() // 对请求做授权配置
                .antMatchers("/user/authorization/**").permitAll() //允许所有人访问 /user/authorization/**"
                .anyRequest().authenticated() // 任何请求。需要身份认证
                .and()
                .httpBasic();
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resource) throws Exception {
        //这里把我们自定义的异常加进去，这样有异常的时候可以控制返回统一的格式信息
        resource.authenticationEntryPoint(new AuthExceptionEntryPoint());
    }
}
