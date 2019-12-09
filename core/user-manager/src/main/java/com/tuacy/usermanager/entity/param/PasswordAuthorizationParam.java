package com.tuacy.usermanager.entity.param;

import lombok.Data;

/**
 * @name: PasswordAuthorizationParam
 * @author: tuacy.
 * @date: 2019/12/9.
 * @version: 1.0
 * @Description:
 */
@Data
public class PasswordAuthorizationParam {

    private String username;
    private String password;
    private String grant_type;
    private String client_id;
    private String client_secret;

}
