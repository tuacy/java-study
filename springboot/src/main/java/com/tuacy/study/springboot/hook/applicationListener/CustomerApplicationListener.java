package com.tuacy.study.springboot.hook.applicationListener;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @name: CustomerApplicationListener
 * @author: tuacy.
 * @date: 2019/8/27.
 * @version: 1.0
 * @Description:
 */
@Component
public class CustomerApplicationListener implements ApplicationListener<CustomerEvent> {

    @Override
    public void onApplicationEvent(CustomerEvent event) {
        System.out.println(event.getSource());
    }
}
