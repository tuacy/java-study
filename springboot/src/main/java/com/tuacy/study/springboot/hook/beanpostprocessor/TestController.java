package com.tuacy.study.springboot.hook.beanpostprocessor;

import org.springframework.stereotype.Controller;

@Controller
public class TestController {

    @RoutingInjected
    private TestService testService;

    public void test() {
        testService.doYouWant();
    }

}
