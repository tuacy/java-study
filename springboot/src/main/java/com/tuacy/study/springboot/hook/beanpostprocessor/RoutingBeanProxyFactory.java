package com.tuacy.study.springboot.hook.beanpostprocessor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @name: RoutingBeanProxyFactory
 * @author: tuacy.
 * @date: 2019/8/23.
 * @version: 1.0
 * @Description:
 */
public class RoutingBeanProxyFactory {

    public static Object crateProxy(Class targetClass, Map<String, Object> bean) {
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setInterfaces(targetClass);
        proxyFactory.addAdvice(new RoutingMethodInterceptor(targetClass, bean));
        return proxyFactory.getProxy();
    }

    /**
     * 方法执行拦截器
     */
    static class RoutingMethodInterceptor implements MethodInterceptor {
        // 通过在类上添加注解指定的实现类
        private Class classOptionRouting;
        private Map<String, Object> implBeanMap;

        public RoutingMethodInterceptor(Class targetClass, Map<String, Object> bean) {
            implBeanMap = bean;
            // 如果类上面添加了指定用哪个class的注解
            if (targetClass.isAnnotationPresent(RoutingSwitch.class)) {
                this.classOptionRouting = ((RoutingSwitch) targetClass.getAnnotation(RoutingSwitch.class)).switchClass();
            }
        }

        @Override
        public Object invoke(MethodInvocation methodInvocation) throws Throwable {
            Method method = methodInvocation.getMethod();
            Class switchClass = this.classOptionRouting;
            if (method.isAnnotationPresent(RoutingSwitch.class)) {
                switchClass = method.getAnnotation(RoutingSwitch.class).switchClass();
            }
            if (switchClass == null) {
                throw new IllegalStateException("RoutingSwitch's switchClass is blank, method:" + method.getName());
            }
            String implBeanName = StringUtils.uncapitalize(switchClass.getSimpleName());
            Object targetBean = implBeanMap.get(implBeanName);
            if (targetBean == null) {
                throw new BeanCreationException("no bean");
            }
            return methodInvocation.getMethod().invoke(targetBean, methodInvocation.getArguments());
        }
    }

}
