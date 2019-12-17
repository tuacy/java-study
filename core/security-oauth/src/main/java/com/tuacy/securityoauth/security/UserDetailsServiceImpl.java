package com.tuacy.securityoauth.security;

import com.tuacy.securityoauth.entity.AuthUserDetailBo;
import com.tuacy.securityoauth.entity.model.RoleDetailBo;
import com.tuacy.securityoauth.entity.model.UserInfoPo;
import com.tuacy.securityoauth.mapper.RoleMapper;
import com.tuacy.securityoauth.service.UserManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @name: UserDetailsServiceImpl
 * @author: tuacy.
 * @date: 2019/11/28.
 * @version: 1.0
 * @Description: 密码模式的用户及权限存到了数据库
 * 实现 UserDetailsService 接口，该接口是根据用户名获取该用户的所有信息， 包括用户信息和权限点。
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserManageService userManageService;
    private RoleMapper roleMapper;

    @Autowired
    public void setUserManageService(UserManageService userManageService) {
        this.userManageService = userManageService;
    }

    @Autowired
    public void setRoleMapper(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
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
        List<RoleDetailBo> roles = roleMapper.getRolesByUserId(userInfo.getUserId());
        return new AuthUserDetailBo(userInfo.getUserName(), userInfo.getPassword(), roles);
    }

}
