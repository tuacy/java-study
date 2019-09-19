package com.tuacy.study.springboot.hook.importBeanDefinitionRegistrar.customercomponent;

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.util.Set;

/**
 * @name: CustomerComponentClassPathBeanDefinitionScanner
 * @author: tuacy.
 * @date: 2019/9/16.
 * @version: 1.0
 * @Description:
 */
public class CustomerComponentClassPathBeanDefinitionScanner extends ClassPathBeanDefinitionScanner {

    public CustomerComponentClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry, boolean useDefaultFilters) {
        super(registry, useDefaultFilters);
    }

    protected void registerFilters() {
        // 只搜索添加了CustomerComponent注解的类
        addIncludeFilter(new AnnotationTypeFilter(CustomerComponent.class));
    }

    /**
     * 指定收缩路径
     */
    @Override
    protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
        return super.doScan(basePackages);
    }

}
