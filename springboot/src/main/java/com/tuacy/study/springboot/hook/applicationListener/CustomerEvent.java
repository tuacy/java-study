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

    /**
     * 事件内容
     */
    private String content;

    public CustomerEvent(Object source, String content) {
        super(source);
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}
