package com.tuacy.study.springboot.hook.beanpostprocessor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @name: RountingBeanProxyFactory
 * @author: tuacy.
 * @date: 2019/8/23.
 * @version: 1.0
 * @Description:
 */
public class RountingBeanProxyFactory {

    public static Object crateProxy(Class targeClass, Map<String, Object> bean) {
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setInterfaces(targeClass);
        proxyFactory.addAdvice(new VersionRoutingMethodInterceptor(targeClass, bean));
        return proxyFactory.getProxy();
    }

    static class VersionRoutingMethodInterceptor implements MethodInterceptor {
        private String classSwitch;
        private Object beanOfSwitchOn;
        private Object beanOfSwitchOff;

        public VersionRoutingMethodInterceptor(Class targetClass, Map<String, Object> bean) {
            String interfaceName = StringUtils.uncapitalize(targetClass.getSimpleName());
            if (targetClass.isAnnotationPresent(RoutingSwitch.class)) {
                this.classSwitch = ((RoutingSwitch) targetClass.getAnnotation(RoutingSwitch.class)).value();
            }
            this.beanOfSwitchOn = bean.get(buildBeanName(interfaceName, true));
            this.beanOfSwitchOff = bean.get(buildBeanName(interfaceName, false));
        }

        private String buildBeanName(String interfaceName, boolean isSwitch) {
            return interfaceName + "Impl" + (isSwitch ? "V2" : "V1");
        }

        private Object getTargetBean(String switchName) {
            boolean switchOn;
            if (RountingVersion.A.equals(switchName)) {
                switchOn = false;
            } else if (RountingVersion.B.equals(switchName)) {
                switchOn = true;
            } else {
                switchOn = false;
            }
            return switchOn ? beanOfSwitchOn : beanOfSwitchOff;
        }

        @Override
        public Object invoke(MethodInvocation methodInvocation) throws Throwable {
            Method method = methodInvocation.getMethod();
            String switchName = this.classSwitch;
            if (method.isAnnotationPresent(RoutingSwitch.class)) {
                switchName = method.getAnnotation(RoutingSwitch.class).value();
            }
            if (StringUtils.isEmpty(switchName)) {
                throw new IllegalStateException("RountingSwitch's value is blank, method:" + method.getName());
            }
            return methodInvocation.getMethod().invoke(getTargetBean(switchName), methodInvocation.getArguments());
        }
    }

}
