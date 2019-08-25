package com.tuacy.study.springboot.hook.beanpostprocessor;

import org.springframework.stereotype.Component;

@Component
public class TestServiceImplV1 implements TestService {
    @Override
    public void doYouWant() {
        System.out.println("V11111111111111111");
    }
}
