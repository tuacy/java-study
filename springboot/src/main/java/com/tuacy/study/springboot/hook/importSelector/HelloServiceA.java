package com.tuacy.study.springboot.hook.importSelector;

public class HelloServiceA implements HelloService {
    @Override
    public void doSomething() {
        System.out.println("Hello A");
    }
}
