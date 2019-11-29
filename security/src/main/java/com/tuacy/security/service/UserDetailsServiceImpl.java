package com.tuacy.security.service;

import com.google.common.collect.Lists;
import com.tuacy.security.entity.AuthUserDetailBo;
import com.tuacy.security.entity.model.UserInfoPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * @name: UserDetailsServiceImpl
 * @author: tuacy.
 * @date: 2019/11/28.
 * @version: 1.0
 * @Description: 密码模式的用户及权限存到了数据库
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserManageService userManageService;

    @Autowired
    public void setUserManageService(UserManageService userManageService) {
        this.userManageService = userManageService;
    }

    /**
     * 实现UserDetailsService中的loadUserByUsername方法，加载用户数据
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserInfoPo userInfo = userManageService.getUserInfo(username);
        if (userInfo == null) {
            throw new UsernameNotFoundException("用户不存在");
        }

        //用户权限列表
        Collection<? extends GrantedAuthority> authorities = Lists.newArrayList();

        return new AuthUserDetailBo(userInfo.getUserName(), userInfo.getPassword(), authorities);
    }

}
