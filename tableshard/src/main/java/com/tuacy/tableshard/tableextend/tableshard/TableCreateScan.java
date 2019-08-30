package com.tuacy.tableshard.tableextend.tableshard;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @name: TableCreateScan
 * @author: tuacy.
 * @date: 2019/8/30.
 * @version: 1.0
 * @Description:
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(TableCreateScanRegister.class)
public @interface TableCreateScan {

    String[] basePackages() default {};

}
