package com.tuacy.tableshard.tableextend.multidatasource;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @name: DataSourceAspect
 * @author: tuacy.
 * @date: 2019/6/24.
 * @version: 1.0
 * @Description: 多数据源切换对应的切面
 */
@Aspect
@Component
@Order(value = 1)
public class DataSourceAspect {

    /**
     * 所有添加了DataSourceAnnotation的方法都进入切面
     */
    @Pointcut("@annotation(com.tuacy.tableshard.tableextend.multidatasource.DataSourceAnnotation)")
    public void dataSourcePointCut() {
    }

    @Around("dataSourcePointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();

        DataSourceAnnotation ds = method.getAnnotation(DataSourceAnnotation.class);
        if (ds == null) {
            DynamicRoutingDataSource.setDataSource(EDataSourceType.BASIC);
        } else {
            DynamicRoutingDataSource.setDataSource(ds.sourceType());
        }

        try {
            return point.proceed();
        } finally {
            DynamicRoutingDataSource.clearDataSource();
        }
    }
}
