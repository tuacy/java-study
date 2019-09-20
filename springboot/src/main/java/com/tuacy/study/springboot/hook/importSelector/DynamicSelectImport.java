package com.tuacy.study.springboot.hook.importSelector;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @name: DynamicSelectImport
 * @author: tuacy.
 * @date: 2019/9/20.
 * @version: 1.0
 * @Description:
 */
public class DynamicSelectImport implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[0];
    }
}
