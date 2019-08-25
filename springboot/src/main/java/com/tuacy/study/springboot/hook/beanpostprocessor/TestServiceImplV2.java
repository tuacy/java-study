package com.tuacy.study.springboot.hook.beanpostprocessor;

import org.springframework.stereotype.Component;

@Component
public class TestServiceImplV2 implements TestService {
    @Override
    public void doYouWant() {
        System.out.println("V22222222222222");
    }
}
