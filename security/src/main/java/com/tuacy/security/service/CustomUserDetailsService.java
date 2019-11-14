package com.tuacy.security.service;

import com.tuacy.security.entity.UserInfo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @name: CustomUserDetailsService
 * @author: tuacy.
 * @date: 2019/11/14.
 * @version: 1.0
 * @Description:
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //这里可以可以通过username（登录时输入的用户名）然后到数据库中找到对应的用户信息，并构建成我们自己的UserInfo来返回。
        //这里可以通过数据库来查找到实际的用户信息，这里我们先模拟下,后续我们用数据库来实现
        if (username.equals("admin")) {
            UserInfo userInfo = new UserInfo();
            userInfo.setUserName("admin");
            userInfo.setPassword("123456");
            return userInfo;
        }
        return null;
    }
}
