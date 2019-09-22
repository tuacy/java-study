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
        // 获取所有的属性
        Field[] fields = clazz.getDeclaredFields();
        for (Field f : fields) {
            // 找到对象里面添加了RoutingInjected注解的属性
            if (f.isAnnotationPresent(RoutingInjected.class)) {
                // 一定要是接口
                if (!f.getType().isInterface()) {
                    throw new BeanCreationException("RoutingInjected field must be declared as an interface: " + f.getName() + " @Class" + clazz.getName());
                }
                try {
                    handleRoutingInjected(f, bean, f.getType());
                } catch (IllegalAccessException e) {
                    throw new BeanCreationException("Exception thrown when handleAutowireRouting", e);
                }
            }
        }
        return bean;
    }


    @SuppressWarnings("unchecked")
    private void handleRoutingInjected(Field field, Object bean, Class type) throws IllegalAccessException {
        // 获取type的所有的bean,key就是bean对应的名字,value就是bean对象
        Map<String, Object> candidates = this.applicationContext.getBeansOfType(type);
        if (candidates.isEmpty()) {
            throw new IllegalAccessException(type.getSimpleName() + " 没有实现类");
        }
        field.setAccessible(true);
        if (candidates.size() == 1) {
            // 如果type只有一个bean,那么直接设置就好了
            field.set(bean, candidates.values().iterator().next());
        } else {
            // 如果有多个实现类,我们就需要看用那个来代理执行了.(用代理类来替换)
            Object proxy = RoutingBeanProxyFactory.crateProxy(type, candidates);
            field.set(bean, proxy);
        }
    }
}
