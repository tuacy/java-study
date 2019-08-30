package com.tuacy.tableshard.tableextend.tableshard;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @name: TableCreateManager
 * @author: tuacy.
 * @date: 2019/8/29.
 * @version: 1.0
 * @Description: TableCreateManager是一个单利类, 会在程序启动的时候最相应的初始化处理, 里面会保存建表的一些配置
 */
public enum TableCreateManager {

    INSTANCE;

    /**
     * key: 基础表名, value: 建表的一些配置(说白了就是我们可以通过这个配置,找到对应的建表语句)
     */
    private Map<String, TableCreateConfig> dbModelTableShardConfigMap = Maps.newHashMap();

    /**
     * 程序启动的时候会调用该方法.
     */
    public void startInit(String[] basePackages) {
        if (basePackages == null || basePackages.length == 0) {
            return;
        }
        // 初始化DBModel，并将信息缓存起来
        List<Class<?>> classesList = Lists.newArrayList();
        for (String basePackage : basePackages) {
            try {
                classesList.addAll(getDbModelTableCreateClass(basePackage));
            } catch (Exception e) {
                throw new RuntimeException("解析model类出现错误，无法初始化。");
            }
        }

        // 用来监测是否存在绑定表重复
        List<String> tableNameList = Lists.newArrayList();
        for (Class<?> aClass : classesList) {
            TableCreate annotation = aClass.getAnnotation(TableCreate.class);
            if (annotation == null) {
                continue;
            }

            String tableName = annotation.tableName();
            if (tableName.isEmpty()) {
                throw new RuntimeException("存在加了@TableCreate注解的model类,没有指定表名字：" + aClass.getName());
            }

            if (tableNameList.contains(tableName)) {
                throw new RuntimeException("存在加了@TableCreate注解的model重复绑定同一个表");
            } else {
                tableNameList.add(tableName);
            }

            dbModelTableShardConfigMap.put(tableName, new TableCreateConfig()
                    .setTableName(tableName)
                    .setAutoCreateTableMapperClass(annotation.autoCreateTableMapperClass())
                    .setAutoCreateTableMapperMethodName(annotation.autoCreateTableMapperMethodName()));
        }
    }

    /**
     * 获取包内加了@TableCreate的Model文件
     *
     * @return 所有加了@TableCreate的类
     */
    private List<Class<?>> getDbModelTableCreateClass(String basePackage) throws Exception {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(Thread.currentThread().getContextClassLoader());

        List<Class<?>> classes = Lists.newArrayList();
        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + basePackage.replace(".", "/") + "/**/*.class";
        Resource[] resources = resolver.getResources(packageSearchPath);
        if (resources.length == 0) {
            return Collections.emptyList();
        }

        String packageBasePath = basePackage.replace(".", File.separator);
        for (Resource resource : resources) {
            try {
                // 改成支持子目录的形式
                String resourceFilePath = resource.getFile().getPath();
                String resourcePackage = resourceFilePath.substring(resourceFilePath.indexOf(packageBasePath)).replace(File.separator, ".").replace(".class", "");
                classes.add((Thread.currentThread().getContextClassLoader().loadClass(resourcePackage)));
            } catch (Exception ignored) {
            }
        }

        List<Class<?>> retList = Lists.newArrayList();
        for (Class<?> aClass : classes) {
            // 确保类添加了TableCreate注解
            if (aClass.isAnnotationPresent(TableCreate.class)) {
                retList.add(aClass);
            }
        }

        return retList;
    }

    /**
     * 获取类分片配置信息
     *
     * @param className 类型
     */
    public TableCreateConfig getClassConfig(String className) {
        if (StringUtils.isEmpty(className)) {
            return null;
        }

        return dbModelTableShardConfigMap.get(className);
    }

}
