package com.tuacy.security.mapper;

import com.tuacy.security.entity.model.UserInfoPo;
import org.apache.ibatis.annotations.Param;

/**
 * @name: UserManageMapper
 * @author: tuacy.
 * @date: 2019/11/28.
 * @version: 1.0
 * @Description:
 */
public interface UserManageMapper {

    /**
     * 根据用户名获取用户信息
     */
    UserInfoPo loadUserByUsername(@Param("userName") String userName);

}
