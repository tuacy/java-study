package com.tuacy.study.springboot.hook.importBeanDefinitionRegistrar;

import com.google.common.collect.Lists;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @name: RunStartManager
 * @author: tuacy.
 * @date: 2019/8/16.
 * @version: 1.0
 * @Description: 自动启动类的管理器
 */
public enum RunStartManager {

    INSTANCE;

    private List<InnerAutoStartClassInfo> startList;

    public void autoStartScan(String[] basePackage) {
        // 初始化规约信息
        List<Class<IRunStart>> classess;
        try {
            classess = getClassesList(basePackage);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        List<InnerAutoStartClassInfo> classInfo = Lists.newArrayList();
        for (Class<IRunStart> aClass : classess) {
            RunStart annotation = aClass.getAnnotation(RunStart.class);
            if (annotation == null) {
                continue;
            }
            classInfo.add(new InnerAutoStartClassInfo(aClass, annotation.des(), annotation.order()));
        }
        startList = classInfo;
    }

    public void autoStartInvoke() {
        if (startList == null || startList.isEmpty()) {
            return;
        }
        startList.forEach(item -> {
            try {
                IRunStart instance = item.getAutoStartClass().newInstance();
                instance.start(item.getDes());
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }


    /**
     * 获取指定包目录下所有实现了IAutoStart接口，并且添加了AutoStart注解的类
     *
     * @param packageNames 包名
     * @return 所有加了规约注解的类
     */
    public List<Class<IRunStart>> getClassesList(String[] packageNames) throws Exception {
        if (packageNames == null || packageNames.length == 0) {
            return Lists.newArrayList();
        }

        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(Thread.currentThread().getContextClassLoader());

        List<Class<?>> classes = new ArrayList<>();
        for (String packageName : packageNames) {
            String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + packageName.replace(".", "/") + "/**/*.class";
            Resource[] resources = resolver.getResources(packageSearchPath);
            if (resources.length == 0) {
                return Collections.emptyList();
            }

            for (Resource resource : resources) {
                try {
                    classes.add((Thread.currentThread().getContextClassLoader().loadClass((packageName + "." + resource.getFilename()).replace(".class", ""))));
                } catch (Exception ignored) {
                }
            }
        }

        List<Class<IRunStart>> output = Lists.newArrayList();
        for (Class<?> aClass : classes) {
            if (aClass.isAnnotationPresent(RunStart.class) && IRunStart.class.isAssignableFrom(aClass)) {
                output.add((Class<IRunStart>) aClass);
            }
        }

        return output;
    }


    private static class InnerAutoStartClassInfo {
        Class<IRunStart> autoStartClass;
        String des;
        int order;

        public InnerAutoStartClassInfo(Class<IRunStart> autoStartClass, String des, int order) {
            this.autoStartClass = autoStartClass;
            this.des = des;
            this.order = order;
        }

        public Class<IRunStart> getAutoStartClass() {
            return autoStartClass;
        }

        public void setAutoStartClass(Class<IRunStart> autoStartClass) {
            this.autoStartClass = autoStartClass;
        }

        public String getDes() {
            return des;
        }

        public void setDes(String des) {
            this.des = des;
        }

        public int getOrder() {
            return order;
        }

        public void setOrder(int order) {
            this.order = order;
        }
    }

}
