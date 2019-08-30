package com.tuacy.tableshard.tableextend.tableshard;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @name: TableCreateScanRegister
 * @author: tuacy.
 * @date: 2019/8/30.
 * @version: 1.0
 * @Description: 配合TableCreateScan注解使用,找到所有添加了TableCreate注解的类,保存建表对应的信息,保存在TableCreateManager下面
 */
public class TableCreateScanRegister implements ImportBeanDefinitionRegistrar {

    private final static String PACKAGE_NAME_KEY = "basePackages";

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {

        AnnotationAttributes annoAttrs = AnnotationAttributes.fromMap(annotationMetadata.getAnnotationAttributes(TableCreateScan.class.getName()));
        if(annoAttrs == null || annoAttrs.isEmpty()){
            return;
        }

        String[] basePackages = (String[]) annoAttrs.get(PACKAGE_NAME_KEY);
        TableCreateManager.INSTANCE.startInit(basePackages);
    }

}
