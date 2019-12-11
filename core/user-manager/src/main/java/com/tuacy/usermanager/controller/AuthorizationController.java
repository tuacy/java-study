package com.tuacy.usermanager.controller;

import com.tuacy.usermanager.entity.AuthorizationResult;
import com.tuacy.usermanager.entity.param.ClientAuthorizationParam;
import com.tuacy.usermanager.entity.param.PasswordAuthorizationParam;
import com.tuacy.usermanager.exception.BusinessException;
import com.tuacy.usermanager.service.impl.AuthorizationImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @name: AuthorizationModelController
 * @author: tuacy.
 * @date: 2019/12/9.
 * @version: 1.0
 * @Description: 提供四种授权模式
 */
@RestController
@RequestMapping(value = "/user/authorization")
public class AuthorizationController {

    private AuthorizationImpl authorization;

    @Autowired
    public void setAuthorization(AuthorizationImpl authorization) {
        this.authorization = authorization;
    }

    /**
     * 授权码模式(authorization code)
     */
    public void authorizationCode() {

    }

    /**
     * 简化模式(implicit)
     */
    public void implicit() {

    }

    /**
     * 密码模式(recource ovner password credentials)
     */
    @RequestMapping(value = "/password", method = RequestMethod.POST)
    public AuthorizationResult passwordCredentials(@RequestBody PasswordAuthorizationParam param) throws BusinessException {
        return authorization.passwordCredentials(param);
    }

    /**
     * 客户端模式(client credentials)
     */
    @RequestMapping(value = "/client", method = RequestMethod.POST)
    public AuthorizationResult clientCredentials(@RequestBody ClientAuthorizationParam param) throws BusinessException {
        return authorization.clientCredentials(param);
    }
}
