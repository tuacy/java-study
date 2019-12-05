package com.tuacy.security.config;

import com.tuacy.security.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.DigestUtils;

import java.util.Objects;

/**
 * @name: WebSecurityConfig
 * @author: tuacy.
 * @date: 2019/11/28.
 * @version: 1.0
 * @Description:
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private UserDetailsServiceImpl userService;

    @Autowired
    public void setUserService(UserDetailsServiceImpl userService) {
        this.userService = userService;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

        //校验用户 userDetailsService验证用户
        auth.userDetailsService(userService)
                .passwordEncoder(new PasswordEncoder() {
                    //对密码进行加密
                    @Override
                    public String encode(CharSequence charSequence) {
                        System.out.println(charSequence.toString());
                        return DigestUtils.md5DigestAsHex(charSequence.toString().getBytes());
                    }

                    //对密码进行判断匹配
                    @Override
                    public boolean matches(CharSequence charSequence, String s) {
                        String encode = DigestUtils.md5DigestAsHex(charSequence.toString().getBytes());
                        return s.equals(encode);
                    }
                });

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.requestMatchers()
                .antMatchers("/oauth/**", "/login", "/login-error")
                .and()
                .authorizeRequests()
                .antMatchers("/oauth/**").authenticated()
                .and()
                .formLogin().loginPage("/login").failureUrl("/login-error");
    }


    /**
     * 注入AuthenticationManager接口，启用OAuth2密码模式
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence charSequence) {
                return charSequence.toString();
            }

            @Override
            public boolean matches(CharSequence charSequence, String s) {
                return Objects.equals(charSequence.toString(), s);
            }
        };
    }

}
