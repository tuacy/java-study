package com.tuacy.securityoauth.entity.model;

import java.io.Serializable;

/**
 * @name: UserInfoPo
 * @author: tuacy.
 * @date: 2019/11/28.
 * @version: 1.0
 * @Description: 用户信息
 */
public class UserInfoPo implements Serializable {
    /**
     * 用户id
     */
    private long userId;
    /**
     * 用户密码
     */
    private String password;
    /**
     * 用户名
     */
    private String userName;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
