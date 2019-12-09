package com.tuacy.usermanager.entity;

/**
 * @name: AuthorizationResult
 * @author: tuacy.
 * @date: 2019/12/9.
 * @version: 1.0
 * @Description:
 */
public class AuthorizationResult {


    /**
     * access_token : d94ec0aa-47ee-4578-b4a0-8cf47f0e8639
     * token_type : bearer
     * refresh_token : 23503bc7-4494-4795-a047-98db75053374
     * expires_in : 3475
     * scope : app
     */

    private String access_token;
    private String token_type;
    private String refresh_token;
    private long expires_in;
    private String scope;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public long getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(long expires_in) {
        this.expires_in = expires_in;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
