package com.tuacy.study.springboot.hook.destructionAwareBeanPostProcessor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * @name: CustomerDestructionAwareBeanPostProcessor
 * @author: tuacy.
 * @date: 2019/9/25.
 * @version: 1.0
 * @Description:
 */
@Component
public class CustomerDestructionAwareBeanPostProcessor implements DestructionAwareBeanPostProcessor {
    @Override
    public void postProcessBeforeDestruction(Object bean, String beanName) throws BeansException {
    }
}
