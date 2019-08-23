package com.tuacy.study.springboot.hook.beanpostprocessor;

/**
 * @name: RountingVersion
 * @author: tuacy.
 * @date: 2019/8/23.
 * @version: 1.0
 * @Description:
 */
public enum RountingVersion {

    A("a"),
    B("b");

    String value;

    RountingVersion(String version) {
        value = version;
    }
}
