package com.tuacy.securityoauth.service;


import com.tuacy.securityoauth.entity.model.UserInfoPo;

/**
 * @name: UserManageService
 * @author: tuacy.
 * @date: 2019/11/28.
 * @version: 1.0
 * @Description:
 */
public interface UserManageService {

    /**
     * 根据用户名获取用户信息
     */
    UserInfoPo getUserInfo(String userName);
}
