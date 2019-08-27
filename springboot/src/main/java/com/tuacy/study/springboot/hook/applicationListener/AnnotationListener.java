package com.tuacy.study.springboot.hook.applicationListener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @name: AnnotationListener
 * @author: tuacy.
 * @date: 2019/8/27.
 * @version: 1.0
 * @Description: 通过@EventListener监听事件
 */
@Component
@Slf4j
public class AnnotationListener {

    @EventListener
    public void customerEvent(CustomerEvent event) {
        System.out.println("收到是消息:" + event.getSource());
    }

}
