package com.tuacy.security.service.impl;

import com.tuacy.security.entity.model.UserInfoPo;
import com.tuacy.security.mapper.UserManageMapper;
import com.tuacy.security.service.UserManageService;
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
        return userManageMapper.findUserInfoByName(userName);
    }
}
