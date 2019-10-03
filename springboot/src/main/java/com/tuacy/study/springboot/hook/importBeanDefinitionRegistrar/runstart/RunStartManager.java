package com.tuacy.study.springboot.hook.importBeanDefinitionRegistrar.runstart;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

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

    /**
     * 查找指定包下,添加了RunStart注解,并且实现了IRunStart接口的类
     */
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

    /**
     * 启动扫描到的添加了RunStart注解,并且实现了IRunStart接口的类
     */
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

        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AssignableTypeFilter(IRunStart.class));
        scanner.addIncludeFilter(new AnnotationTypeFilter(RunStart.class));
        List<Class<IRunStart>> classes = new ArrayList<>();
        for (String packageName : packageNames) {

            String packageBasePath = packageName.replace(".", "/");
            Set<BeanDefinition> findClasses = scanner.findCandidateComponents(packageName);
            if (!findClasses.isEmpty()) {
                findClasses.forEach(beanDefinition -> {
                    try {
                        classes.add((Class<IRunStart>) Thread.currentThread().getContextClassLoader().loadClass(beanDefinition.getBeanClassName()));
                    } catch (Exception ignored) {

                    }

                });
            }
        }

        return classes;
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
