package com.tuacy.study.springboot.hook.importSelector.abc;

import com.tuacy.study.springboot.hook.importSelector.HelloService;

public class HelloServiceC implements HelloService {
    @Override
    public void doSomething() {
        System.out.println("Hello A");
    }
}
