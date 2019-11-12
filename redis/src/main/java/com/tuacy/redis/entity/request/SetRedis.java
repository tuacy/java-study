package com.tuacy.redis.entity.request;

/**
 * @name: GetRedis
 * @author: tuacy.
 * @date: 2019/11/11.
 * @version: 1.0
 * @Description:
 */
public class SetRedis {

    private String key;
    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
