package com.tuacy.study.springboot.hook.beanpostprocessor;

public interface TestService {

    @RoutingSwitch(switchClass = TestServiceImplV1.class)
    void doYouWant();

}
