package com.tuacy.study.springboot.aware.ImportAware;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.stereotype.Component;

import java.util.Set;

@Configuration
@Component
public class BeanImportAware implements ImportAware {
    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        Set<String> sets = importMetadata.getAnnotationTypes();
        int a = 10;
    }
}
