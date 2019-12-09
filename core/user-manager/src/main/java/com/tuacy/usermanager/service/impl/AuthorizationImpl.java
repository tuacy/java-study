package com.tuacy.usermanager.service.impl;

import com.tuacy.usermanager.entity.AuthorizationResult;
import com.tuacy.usermanager.entity.param.PasswordAuthorizationParam;
import com.tuacy.usermanager.exception.BusinessException;
import com.tuacy.usermanager.exception.ErrorCode;
import com.tuacy.usermanager.service.Authorization;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * @name: AuthorizationImpl
 * @author: tuacy.
 * @date: 2019/12/9.
 * @version: 1.0
 * @Description:
 */
@Service
public class AuthorizationImpl implements Authorization {

    private static final String OAUTH_TOKEN_API = "/oauth/token";

    private static final String REQUEST_USER_NAME = "username";
    private static final String REQUEST_PASSWORD = "password";
    private static final String REQUEST_GRANT_TYPE = "grant_type";
    private static final String REQUEST_CLIENT_ID = "client_id";
    private static final String REQUEST_CLIENT_SECRET = "client_secret";

    private RestTemplate restTemplate;
    @Value("${auth-service-url}")
    private String authServiceUrl;

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * 授权码模式(authorization code)
     */
    @Override
    public void authorizationCode() {

    }

    /**
     * 简化模式(implicit)
     */
    @Override
    public void implicit() {

    }

    /**
     * 密码模式(recource ovner password credentials)
     */
    @Override
    public AuthorizationResult passwordCredentials(PasswordAuthorizationParam param) throws BusinessException {
        if (param == null) {
            throw new BusinessException(ErrorCode.PARAM_NULL_ERROR, "参数为null");
        }
        MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<>();
        requestEntity.add(REQUEST_USER_NAME, param.getUsername());
        requestEntity.add(REQUEST_PASSWORD, param.getPassword());
        requestEntity.add(REQUEST_GRANT_TYPE, param.getGrant_type());
        requestEntity.add(REQUEST_CLIENT_ID, param.getClient_id());
        requestEntity.add(REQUEST_CLIENT_SECRET, param.getClient_secret());
        AuthorizationResult authorizationResult;
        try {
            authorizationResult = restTemplate.getForObject(authServiceUrl + OAUTH_TOKEN_API, AuthorizationResult.class, requestEntity);
        } catch (RestClientException e) {
            BusinessException distException = new BusinessException(ErrorCode.REST_TEMPLATE_ERRO);
            BeanUtils.initCause(distException, e);
            throw distException;
        }

        return authorizationResult;
    }

    /**
     * 客户端模式(client credentials)
     */
    @Override
    public void clientCredentials() {

    }

}
