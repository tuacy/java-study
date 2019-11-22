package com.tuacy.security.service;

import com.google.common.collect.Lists;
import com.tuacy.security.entity.RoleInfo;
import com.tuacy.security.entity.UserInfo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @name: CustomUserDetailsService
 * @author: tuacy.
 * @date: 2019/11/15.
 * @version: 1.0
 * @Description:
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {


    /**
     * 这个方法将根据用户名去查找用户，如果用户不存在，则抛出UsernameNotFoundException异常,
     * 否则返回查询到的数据
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(username.equals("root")) {
            UserInfo userInfo = new UserInfo();
            userInfo.setUserName("root");
            userInfo.setPassword("root");
            List<RoleInfo> roleList = Lists.newArrayList();
            RoleInfo roleItem = new RoleInfo();
            roleItem.setName("USER");
            roleList.add(roleItem);
            userInfo.setRoleList(roleList);
            return userInfo;
        } else {
            throw new UsernameNotFoundException("用户名不对");
        }
    }
}
