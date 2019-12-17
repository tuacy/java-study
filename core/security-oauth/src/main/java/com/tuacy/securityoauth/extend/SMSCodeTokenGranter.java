package com.tuacy.securityoauth.extend;

import com.tuacy.securityoauth.service.SMSRecordService;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @name: SMSCodeTokenGranter
 * @author: tuacy.
 * @date: 2019/11/28.
 * @version: 1.0
 * @Description: 验证码认证模式 (oath2之前是有四种验证模式:授权码模式,简化模式,密码模式,客户端模式)
 */
public class SMSCodeTokenGranter extends AbstractTokenGranter {

    private static final String GRANT_TYPE = "sms_code";

    private SMSRecordService smsRecordService;
    private UserDetailsService userManageService;

    public SMSCodeTokenGranter(AuthorizationServerTokenServices tokenServices,
                               ClientDetailsService clientDetailsService,
                               OAuth2RequestFactory requestFactory,
                               UserDetailsService userDetailsService,
                               SMSRecordService smsRecordService) {
        super(tokenServices, clientDetailsService, requestFactory, GRANT_TYPE);
        this.smsRecordService = smsRecordService;
        this.userManageService = userDetailsService;
    }

    @Override
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client,
                                                           TokenRequest tokenRequest) {

        Map<String, String> parameters = new LinkedHashMap<String, String>(tokenRequest.getRequestParameters());
        String userName = parameters.get("username");  //客户端提交的用户名
        String smsCode = parameters.get("smscode");  //客户端提交的验证码

        // 从库里查用户
        UserDetails user = userManageService.loadUserByUsername(userName);
        if (user == null) {
            throw new InvalidGrantException("用户不存在");
        }

        // 验证用户状态(是否警用等), 代码略

        // 验证验证码,从缓存中查找验证码
        String smsCodeCached = smsRecordService.getSMSCode(userName);
        if (smsCodeCached == null || smsCodeCached.isEmpty()) {
            throw new InvalidGrantException("用户没有发送验证码");
        }
        if (!smsCode.equals(smsCodeCached)) {
            throw new InvalidGrantException("验证码不正确");
        } else {
            // 验证通过后从缓存中移除验证码, 代码略
            smsRecordService.clearSMSCode(userName);
        }


        Authentication userAuth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        // 关于user.getAuthorities(): 我们的自定义用户实体是实现了
        // org.springframework.security.core.userdetails.UserDetails 接口的, 所以有 user.getAuthorities()
        // 当然该参数传null也行
        ((AbstractAuthenticationToken) userAuth).setDetails(parameters);

        OAuth2Request storedOAuth2Request = getRequestFactory().createOAuth2Request(client, tokenRequest);
        return new OAuth2Authentication(storedOAuth2Request, userAuth);
    }
}
