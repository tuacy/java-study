package com.tuacy.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @name: SecurityConfig
 * @author: tuacy.
 * @date: 2019/11/14.
 * @version: 1.0
 * @Description:
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationProvider provider;  //注入我们自己的AuthenticationProvider

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(provider);
//        super.configure(auth);
//        auth.inMemoryAuthentication()
//                .withUser("admin").password("123456").roles("USER")
//                .and()
//                .withUser("test").password("test123").roles("ADMIN");
    }


}
