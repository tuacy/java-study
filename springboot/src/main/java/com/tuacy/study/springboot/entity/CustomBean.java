package com.tuacy.study.springboot.entity;

/**
 * @name: CustomBean
 * @author: tuacy.
 * @date: 2019/8/22.
 * @version: 1.0
 * @Description:
 */
public class CustomBean {

    private String name;
    private int age;

    public CustomBean(String name, int age) {
        this.name = name;
        this.age = age;
    }

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
