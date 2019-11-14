package com.tuacy.security.provider;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * @name: CustomerAuthenticationProvider
 * @author: tuacy.
 * @date: 2019/11/14.
 * @version: 1.0
 * @Description:
 */
//@Component
public class CustomAuthenticationProvider /*implements AuthenticationProvider */{
//    @Override
//    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//
//        String userName = authentication.getName();// 这个获取表单输入中返回的用户名;
//        String password = (String) authentication.getCredentials();// 这个是表单中输入的密码；
//        // 这里构建来判断用户是否存在和密码是否正确
//        Collection<? extends GrantedAuthority> authorities = userInfo.getAuthorities();
//        // 构建返回的用户登录成功的token
//        return new UsernamePasswordAuthenticationToken(userInfo, password, authorities);
//    }
//
//    @Override
//    public boolean supports(Class<?> aClass) {
//        return true;
//    }
}
