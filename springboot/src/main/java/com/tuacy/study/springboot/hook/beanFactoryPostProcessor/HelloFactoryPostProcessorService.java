package com.tuacy.study.springboot.hook.beanFactoryPostProcessor;

import org.springframework.stereotype.Service;

@Service()
public class HelloFactoryPostProcessorService {

    private String desc = "hello";

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void sayHello() {
        System.out.println(desc);
    }

}
