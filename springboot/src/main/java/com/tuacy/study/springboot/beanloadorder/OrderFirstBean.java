package com.tuacy.study.springboot.beanloadorder;

/**
 * @name: OrderFirstBean
 * @author: tuacy.
 * @date: 2019/8/27.
 * @version: 1.0
 * @Description:
 */
public class OrderFirstBean {

    public void init() {
        System.out.println("first bean load");
    }

}
