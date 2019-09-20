package com.tuacy.study.springboot.hook.importSelector;

public class HelloServiceB implements HelloService {
    @Override
    public void doSomething() {
        System.out.println("Hello B");
    }
}
