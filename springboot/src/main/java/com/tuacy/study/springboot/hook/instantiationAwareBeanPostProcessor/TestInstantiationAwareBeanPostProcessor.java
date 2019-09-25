package com.tuacy.study.springboot.hook.instantiationAwareBeanPostProcessor;

import org.springframework.stereotype.Component;

/**
 * @name: TestInstantiationAwareBeanPostProcessor
 * @author: tuacy.
 * @date: 2019/9/25.
 * @version: 1.0
 * @Description:
 */
@Component
public class TestInstantiationAwareBeanPostProcessor {

    private String name = "test";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void dosomething() {
        System.out.print("执行了dosomething.......\n");
    }
}
