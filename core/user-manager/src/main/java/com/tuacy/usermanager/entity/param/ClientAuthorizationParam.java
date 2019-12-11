package com.tuacy.usermanager.entity.param;

import lombok.Data;

/**
 * @name: PasswordAuthorizationParam
 * @author: tuacy.
 * @date: 2019/12/9.
 * @version: 1.0
 * @Description: 客户端授权模式
 */
@Data
public class ClientAuthorizationParam {

    private String grant_type;
    private String client_id;
    private String client_secret;

}
