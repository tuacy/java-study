package com.tuacy.tableshard.tableextend.tableshard;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @name: TableShardParam
 * @author: tuacy.
 * @date: 2019/8/30.
 * @version: 1.0
 * @Description: 添加在参数上的注解, 一定要配置mybatis 的Param注解使用
 * <p>
 * 我们是这样考虑的,分表核心在于确定表的名字,表的名字怎么来,肯定是通过某个参数来获取到.
 * 所以,这里我们设计TableShardParam注解,用于添加在参数上,让我们方便的获取到通过那个参数来获取表名
 * 1. int insertItem(@TableShardParam(dependFieldName = "recTime") @Param("item") AccHour item);
 * -- 分表依据对应AccHour对象recTime属性对应的值
 * 2. int insertList(@TableShardParam(dependFieldName = "recTime") @Param("list") List<AccHour> list);
 * -- 分表依据对应list的第一个对象recTime属性对应的值
 * 3. List<AccHour> selectLIst(@TableShardParam() @Param("startTime") Long startTIme, @Param("endTime") Long endTime);
 * -- 分表依据对应endTime对应的值
 */
@Documented
@Inherited
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface TableShardParam {

    @AliasFor("dependFieldName")
    String value() default "";

    /**
     * dependFieldName取到我们需要的获取表名的依据
     */
    @AliasFor("value")
    String dependFieldName() default "";

}
