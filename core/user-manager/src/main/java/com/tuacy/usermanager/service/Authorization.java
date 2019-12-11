package com.tuacy.usermanager.service;

import com.tuacy.usermanager.entity.AuthorizationResult;
import com.tuacy.usermanager.entity.param.ClientAuthorizationParam;
import com.tuacy.usermanager.entity.param.PasswordAuthorizationParam;
import com.tuacy.usermanager.exception.BusinessException;

/**
 * @name: Authorization
 * @author: tuacy.
 * @date: 2019/12/9.
 * @version: 1.0
 * @Description:
 */
public interface Authorization {

    /**
     * 授权码模式(authorization code)
     */
    void authorizationCode() throws BusinessException;

    /**
     * 简化模式(implicit)
     */
    void implicit() throws BusinessException;

    /**
     * 密码模式(recource ovner password credentials)
     */
    AuthorizationResult passwordCredentials(PasswordAuthorizationParam param) throws BusinessException;

    /**
     * 客户端模式(client credentials)
     */
    AuthorizationResult clientCredentials(ClientAuthorizationParam param) throws BusinessException;

}
