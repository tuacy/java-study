package com.tuacy.study.springboot.hook.importBeanDefinitionRegistrar.customercomponent;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @name: RunStartScannerRegister
 * @author: tuacy.
 * @date: 2019/8/16.
 * @version: 1.0
 * @Description:
 */
public class CustomerComponentScannerRegister implements ImportBeanDefinitionRegistrar, ResourceLoaderAware {

    private ResourceLoader resourceLoader;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        AnnotationAttributes annoAttrs = AnnotationAttributes.fromMap(annotationMetadata.getAnnotationAttributes(CustomerComponent.class.getName()));
        if (annoAttrs == null || annoAttrs.isEmpty()) {
            return;
        }
        MyClassPathBeanDefinitionScanner scanner = new MyClassPathBeanDefinitionScanner(beanDefinitionRegistry, false);
        scanner.setResourceLoader(resourceLoader);
        scanner.registerFilters();
        scanner.doScan("com.faderw.school.domain");
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}
