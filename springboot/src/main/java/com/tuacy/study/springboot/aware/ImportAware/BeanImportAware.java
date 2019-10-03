package com.tuacy.study.springboot.aware.ImportAware;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

@Configuration
public class BeanImportAware implements ImportAware {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {

        AnnotationAttributes annoAttrs = AnnotationAttributes.fromMap(importMetadata.getAnnotationAttributes(ChangeAttribute.class.getName()));
        if (annoAttrs != null) {
            // 获取到ChangeAttribute注解里面的属性
            name = (String) annoAttrs.get("value");
        }

    }
}
