package com.tuacy.study.springboot.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @name: UserInfo
 * @author: tuacy.
 * @date: 2019/8/29.
 * @version: 1.0
 * @Description:
 */
@ConfigurationProperties(prefix = "user.info")
public class UserInfo {

    private int age;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
