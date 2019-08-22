package com.tuacy.study.springboot.hook;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * @name: CustomBeanPostProcessor
 * @author: tuacy.
 * @date: 2019/8/22.
 * @version: 1.0
 * @Description:
 */
@Component
public class CustomBeanPostProcessor implements BeanPostProcessor {

    /**
     * 在每一个bean对象的初始化方法调用之前回调
     * 实例化、依赖注入完毕，在调用显示的初始化之前完成一些定制的初始化任务
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    /**
     * 会在每个bean对象的初始化方法调用之后被回调
     * 实例化、依赖注入、初始化完毕时执行
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
