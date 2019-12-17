package com.tuacy.securityoauth.config;

import com.tuacy.securityoauth.extend.CustomTokenEnhancer;
import com.tuacy.securityoauth.extend.SMSCodeTokenGranter;
import com.tuacy.securityoauth.service.SMSRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenGranter;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeTokenGranter;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.implicit.ImplicitTokenGranter;
import org.springframework.security.oauth2.provider.password.ResourceOwnerPasswordTokenGranter;
import org.springframework.security.oauth2.provider.refresh.RefreshTokenGranter;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.*;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @name: AuthorizationServerConfiguration
 * @author: tuacy.
 * @date: 2019/11/28.
 * @version: 1.0
 * @Description: OAuth2认证授权服务配置
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    /**
     * 注入权限验证控制器 来支持 password grant type
     */
    private AuthenticationManager authenticationManager;
    /**
     * 数据源
     */
    private DataSource dataSource;
    private TokenStore tokenStore;
    private AccessTokenConverter accessTokenConverter;
    /**
     * 注入userDetailsService，开启refresh_token需要用到
     */
    private UserDetailsService userDetailsService;
    private SMSRecordService smsRecordService;
//    private WebResponseExceptionTranslator webResponseExceptionTranslator;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Autowired
    public void setTokenStore(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    @Autowired
    public void setAccessTokenConverter(AccessTokenConverter accessTokenConverter) {
        this.accessTokenConverter = accessTokenConverter;
    }

    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Autowired
    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Autowired
    public void setSmsRecordService(SMSRecordService smsRecordService) {
        this.smsRecordService = smsRecordService;
    }

//    @Autowired
//    public void setWebResponseExceptionTranslator(WebResponseExceptionTranslator webResponseExceptionTranslator) {
//        this.webResponseExceptionTranslator = webResponseExceptionTranslator;
//    }

    /**
     * 声明 ClientDetails实现 -- JdbcClientDetailsService实现
     */
    @Bean
    public ClientDetailsService clientDetails() {
        return new JdbcClientDetailsService(dataSource);
    }

    @Bean
    public TokenEnhancer tokenEnhancer() {
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        if (accessTokenConverter instanceof TokenEnhancer) {
            tokenEnhancerChain.setTokenEnhancers(Arrays.asList(new CustomTokenEnhancer(), (TokenEnhancer) accessTokenConverter));
        } else {
            tokenEnhancerChain.setTokenEnhancers(Collections.singletonList(new CustomTokenEnhancer()));
        }
        return tokenEnhancerChain;
    }

    @Primary
    @Bean
    public AuthorizationServerTokenServices tokenServices() {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setTokenStore(tokenStore);
        // 支持使用refresh_token
        tokenServices.setSupportRefreshToken(true);
        //不重复使用refresh_token,每交刷新完后，更新这个值
        tokenServices.setReuseRefreshToken(false);
        tokenServices.setTokenEnhancer(tokenEnhancer());   // 如果没有设置它,JWT就失效了.
        // token有效期, 30分钟
        tokenServices.setAccessTokenValiditySeconds((int) TimeUnit.MINUTES.toSeconds(30));
        // 默认30天，60分钟
        tokenServices.setRefreshTokenValiditySeconds((int) TimeUnit.MINUTES.toSeconds(60));
        return tokenServices;
    }

    /**
     * 用来配置令牌端点(Token Endpoint)的安全约束
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        super.configure(security);

        /**
         * 配置oauth2服务跨域
         */
        CorsConfigurationSource source = request -> {
            CorsConfiguration corsConfiguration = new CorsConfiguration();
            corsConfiguration.addAllowedHeader("*");
            corsConfiguration.addAllowedOrigin(request.getHeader(HttpHeaders.ORIGIN));
            corsConfiguration.addAllowedMethod("*");
            corsConfiguration.setAllowCredentials(true);
            corsConfiguration.setMaxAge(3600L);
            return corsConfiguration;
        };

        security.tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()")
                .allowFormAuthenticationForClients()
                .addTokenEndpointAuthenticationFilter(new CorsFilter(source));
    }

    /**
     * 用来配置客户端详情服务（ClientDetailsService），
     * 客户端详情信息在这里进行初始化，你能够把客户端详情信息写死在这里或者是通过数据库来存储调取详情信息。
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        super.configure(clients);
        clients.withClientDetails(clientDetails());
    }

    /**
     * 用来配置授权（authorization）以及令牌（token）的访问端点和令牌服务(token services)
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        super.configure(endpoints);
        endpoints
                //要使用refresh_token的话，需要额外配置userDetailsService
                .userDetailsService(userDetailsService)
                //token存到redis
                .tokenStore(tokenStore)
                .tokenGranter(tokenGranter())
                //开启密码授权类型
                .authenticationManager(authenticationManager)
                .authorizationCodeServices(new JdbcAuthorizationCodeServices(dataSource))
                // 告诉spring security token的生成方式
                .accessTokenConverter(accessTokenConverter)
                //接收GET和POST
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);
        //自定义登录或者鉴权失败时的返回信息
//                .exceptionTranslator(webResponseExceptionTranslator);


        endpoints.tokenServices(tokenServices());
    }

    private OAuth2RequestFactory requestFactory() {
        return new DefaultOAuth2RequestFactory(clientDetails());
    }

    private AuthorizationCodeServices authorizationCodeServices() {
        return new InMemoryAuthorizationCodeServices();
    }


    /**
     * 这是从spring 的代码中 copy出来的,默认的几个 TokenGranter, 我们自定义的就加到这里就行了,目前我还没有加
     */
    private List<TokenGranter> getDefaultTokenGranters() {
        ClientDetailsService clientDetails = clientDetails();
        AuthorizationServerTokenServices tokenServices = tokenServices();
        AuthorizationCodeServices authorizationCodeServices = authorizationCodeServices();
        OAuth2RequestFactory requestFactory = requestFactory();

        List<TokenGranter> tokenGranters = new ArrayList<TokenGranter>();
        tokenGranters.add(new AuthorizationCodeTokenGranter(tokenServices, authorizationCodeServices, clientDetails, requestFactory));
        tokenGranters.add(new RefreshTokenGranter(tokenServices, clientDetails, requestFactory));
        ImplicitTokenGranter implicit = new ImplicitTokenGranter(tokenServices, clientDetails, requestFactory);
        tokenGranters.add(implicit);
        tokenGranters.add(new ClientCredentialsTokenGranter(tokenServices, clientDetails, requestFactory));
        if (authenticationManager != null) {
            tokenGranters.add(new ResourceOwnerPasswordTokenGranter(authenticationManager, tokenServices, clientDetails, requestFactory));
        }
        // 增加一种验证码的认证模式
        tokenGranters.add(new SMSCodeTokenGranter(tokenServices, clientDetails, requestFactory, userDetailsService, smsRecordService));
        return tokenGranters;
    }

    /**
     * 多增加一种授权模式（oath2之前是有四种验证模式:授权码模式,简化模式,密码模式,客户端模式），比如我们添加一个短信验证码的认知模式
     */
    private TokenGranter tokenGranter() {
        return new TokenGranter() {
            private CompositeTokenGranter delegate;

            @Override
            public OAuth2AccessToken grant(String grantType, TokenRequest tokenRequest) {
                if (delegate == null) {
                    delegate = new CompositeTokenGranter(getDefaultTokenGranters());
                }
                return delegate.grant(grantType, tokenRequest);
            }
        };
    }
}
