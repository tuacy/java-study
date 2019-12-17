package com.tuacy.securityoauth.mapper;


import com.tuacy.securityoauth.entity.model.RolePermisson;

import java.util.List;

/**
 * @name: UserManageMapper
 * @author: tuacy.
 * @date: 2019/11/28.
 * @version: 1.0
 * @Description:
 */
public interface PermissionMapper {


    List<RolePermisson> getRolePermissions();

}
