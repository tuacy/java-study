package com.tuacy.securityoauth.entity.model;

import org.springframework.security.core.GrantedAuthority;

/**
 * @name: RoleDetailBo
 * @author: tuacy.
 * @date: 2019/12/2.
 * @version: 1.0
 * @Description:
 */
public class RoleDetailBo implements GrantedAuthority {
    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getAuthority() {
        return name;
    }
}
