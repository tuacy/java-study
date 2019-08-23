package com.tuacy.study.springboot.hook.beanpostprocessor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * @name: CustomBeanPostProcessor
 * @author: tuacy.
 * @date: 2019/8/22.
 * @version: 1.0
 * @Description:
 */
@Component
public class CustomBeanPostProcessor implements BeanPostProcessor {

    private ApplicationContext applicationContext;

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

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
        Class clazz = bean.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field f : fields) {
            if (f.isAnnotationPresent(RoutingInjected.class)) {
                if (!f.getType().isInterface()) {
                    throw new BeanCreationException("RoutingInjected field must be declared as an interface: " + f.getName() + " @Class" + clazz.getName());
                }
            }
        }
        return bean;
    }

    private void handleRoutingInjected(Field field, Object bean, Class type) throws IllegalAccessException {
        Map<String, Object> candidates = this.applicationContext.getBeansOfType(type);
        field.setAccessible(true);
        if (candidates.size() == 1) {
            field.set(bean, candidates.values().iterator().next());
        } else if (candidates.size() == 2) {
            Object proxy = RountingBeanProxyFactory.crateProxy(type, candidates);
            field.set(bean, proxy);
        } else {
            throw new IllegalAccessException("Find more than 2 beans for type:" + type);
        }
    }
}
