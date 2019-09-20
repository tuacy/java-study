package com.tuacy.study.springboot.hook.importBeanDefinitionRegistrar.beanioc;

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
public class BeanIocScannerRegister implements ImportBeanDefinitionRegistrar, ResourceLoaderAware {

    private final static String PACKAGE_NAME_KEY = "basePackages";

    private ResourceLoader resourceLoader;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        AnnotationAttributes annoAttrs = AnnotationAttributes.fromMap(annotationMetadata.getAnnotationAttributes(BeanIocScan.class.getName()));
        if (annoAttrs == null || annoAttrs.isEmpty()) {
            return;
        }
        // 搜索路径
        String[] basePackages = (String[]) annoAttrs.get(PACKAGE_NAME_KEY);
        BeanIocClassPathBeanDefinitionScanner scanner = new BeanIocClassPathBeanDefinitionScanner(beanDefinitionRegistry, false);
        scanner.setResourceLoader(resourceLoader);
        scanner.registerFilters();
        scanner.doScan(basePackages);
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}
