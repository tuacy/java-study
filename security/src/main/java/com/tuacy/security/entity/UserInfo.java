package com.tuacy.security.entity;

import com.google.common.collect.Lists;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * @name: UserInfo
 * @author: tuacy.
 * @date: 2019/11/14.
 * @version: 1.0
 * @Description:
 */
public class UserInfo implements UserDetails {

    private String userName;
    private String password;

    /**
     * 返回授予用户的权限
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Lists.newArrayList();
    }

    @Override
    public String getPassword() {
        return userName;
    }

    @Override
    public String getUsername() {
        return password;
    }

    /**
     * 帐户是否过期
     */
    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    /**
     * 帐户是否被冻结
     */
    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    /**
     * 帐户密码是否过期，一般有的密码要求性高的系统会使用到，比较每隔一段时间就要求用户重置密码
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    /**
     * 帐号是否可用
     */
    @Override
    public boolean isEnabled() {
        return false;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
