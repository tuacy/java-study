package com.tuacy.study.springboot.hook;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.stereotype.Component;

/**
 * @name: CustomBeanDefinitionRegistryPostProcessor
 * @author: tuacy.
 * @date: 2019/8/16.
 * @version: 1.0
 * @Description:
 */
@Component
public class CustomBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {

    /**
     * 这个方法被调用的时候, 所有的BeanDefinition已经被加载了, 但是所有的Bean还没被创建
     * 定义 --> 创建 --> 初始化
     * 在Bean被定义但还没被创建的时候执行。
     */
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {

    }

    /**
     * 方法, 这个方法被调用的时候, 所有的Bean已经被创建, 但是还没有被初始化. 也就是说, 通过它我们可以在初始化任何Bean之前, 做各种操作, 甚至读取并修改BeanDefinition(bean定义的元数据)
     * Bean被创建但还没被初始化的时候执行
     */
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }
}
