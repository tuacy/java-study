package com.tuacy.study.springboot.aware.BeanFactoryAware;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Component;

@Component
public class ShowBeanFactory implements BeanFactoryAware {


    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        // 可以从这里获取到BeanFactory(负责生产和管理Bean的一个工厂)
    }

}
