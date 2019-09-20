package com.tuacy.study.springboot.hook.importSelector;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @name: DynamicSelectConfigure
 * @author: tuacy.
 * @date: 2019/9/20.
 * @version: 1.0
 * @Description:
 */
@Configuration
@Import(DynamicSelectImport.class)
public class DynamicSelectConfigure {
}
