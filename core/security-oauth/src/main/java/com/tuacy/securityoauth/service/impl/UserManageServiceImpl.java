package com.tuacy.securityoauth.service.impl;

import com.tuacy.securityoauth.entity.model.UserInfoPo;
import com.tuacy.securityoauth.mapper.UserManageMapper;
import com.tuacy.securityoauth.service.UserManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @name: UserManageServiceImpl
 * @author: tuacy.
 * @date: 2019/11/28.
 * @version: 1.0
 * @Description:
 */
@Service
public class UserManageServiceImpl implements UserManageService {

    private UserManageMapper userManageMapper;

    @Autowired
    public void setUserManageMapper(UserManageMapper userManageMapper) {
        this.userManageMapper = userManageMapper;
    }

    @Override
    public UserInfoPo getUserInfo(String userName) {
        return userManageMapper.loadUserByUsername(userName);
    }
}
