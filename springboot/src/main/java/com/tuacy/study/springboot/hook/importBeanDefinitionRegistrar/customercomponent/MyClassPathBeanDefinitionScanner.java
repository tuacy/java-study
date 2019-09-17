package com.tuacy.study.springboot.hook.importBeanDefinitionRegistrar.customercomponent;

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.util.Set;

/**
 * @name: MyClassPathBeanDefinitionScanner
 * @author: tuacy.
 * @date: 2019/9/16.
 * @version: 1.0
 * @Description:
 */
public class MyClassPathBeanDefinitionScanner extends ClassPathBeanDefinitionScanner {

    public MyClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry, boolean useDefaultFilters) {
        super(registry, useDefaultFilters);
    }

    protected void registerFilters() {
        addIncludeFilter(new AnnotationTypeFilter(CustomerComponent.class));
    }

    @Override
    protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
        return super.doScan(basePackages);
    }

}
