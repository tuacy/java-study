package com.tuacy.study.springboot.aware.BeanNameAware;

import org.springframework.beans.factory.BeanNameAware;
import org.springframework.stereotype.Component;

@Component
public class ShowNameBean implements BeanNameAware {


    @Override
    public void setBeanName(String s) {

        System.out.println("****************");
        System.out.println("****************");
        System.out.println(s);
        System.out.println("****************");
        System.out.println("****************");

    }
}
