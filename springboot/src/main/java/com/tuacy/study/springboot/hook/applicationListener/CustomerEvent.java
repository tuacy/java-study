package com.tuacy.study.springboot.hook.applicationListener;

import org.springframework.context.ApplicationEvent;

/**
 * @name: CustomerEvent
 * @author: tuacy.
 * @date: 2019/8/27.
 * @version: 1.0
 * @Description:
 */
public class CustomerEvent extends ApplicationEvent {

    public CustomerEvent(Object source) {
        super(source);
    }

}
