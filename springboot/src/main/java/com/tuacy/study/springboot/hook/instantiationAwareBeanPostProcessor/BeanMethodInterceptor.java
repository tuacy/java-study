package com.tuacy.study.springboot.hook.instantiationAwareBeanPostProcessor;

import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @name: BeanMethodInterceptor
 * @author: tuacy.
 * @date: 2019/9/25.
 * @version: 1.0
 * @Description:
 */
public class BeanMethodInterceptor implements MethodInterceptor {
    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println("目标方法前:" + method+"\n");
        Object object = methodProxy.invokeSuper(o, objects);
        System.out.println("目标方法后:" + method+"\n");
        return object;
    }
}
