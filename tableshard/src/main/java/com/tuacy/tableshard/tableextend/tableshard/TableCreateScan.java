package com.tuacy.tableshard.tableextend.tableshard;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @name: TableCreateScan
 * @author: tuacy.
 * @date: 2019/8/30.
 * @version: 1.0
 * @Description: TableCreateScan注解用于标识, 表model对应的包名
 * 我们去去这个包里面查找所有带有TableCreate注解的类,保存建表对应的信息
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(TableCreateScanRegister.class)
public @interface TableCreateScan {

    String[] basePackages() default {};

}
