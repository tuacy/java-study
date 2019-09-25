package com.tuacy.study.springboot.hook.instantiationAwareBeanPostProcessor;

import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.stereotype.Component;

/**
 * @name: CustomerInstantiationAwareBeanPostProcessor
 * @author: tuacy.
 * @date: 2019/9/25.
 * @version: 1.0
 * @Description:
 */
@Component
public class CustomerInstantiationAwareBeanPostProcessor implements InstantiationAwareBeanPostProcessor {

    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        if (beanClass == TestInstantiationAwareBeanPostProcessor.class) {
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(beanClass);
            enhancer.setCallback(new BeanMethodInterceptor());
            TestInstantiationAwareBeanPostProcessor bean = (TestInstantiationAwareBeanPostProcessor) enhancer.create();
            System.out.print("返回动态代理\n");
            return bean;
        }
        return null;
    }

    /**
     * 会影响postProcessProperties是否执行，返回false不执行
     */
    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        if (bean instanceof TestInstantiationAwareBeanPostProcessor) {
            return false;
        }
        return true;
    }

    @Override
    public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName) throws BeansException {
        if (bean instanceof TestInstantiationAwareBeanPostProcessor) {
            MutablePropertyValues pvsList = new MutablePropertyValues();
            pvsList.addPropertyValue(new PropertyValue("name", "tuacy"));
            //修改bean中name 的属性值
            return pvsList;
        }
        return pvs;
    }
}
