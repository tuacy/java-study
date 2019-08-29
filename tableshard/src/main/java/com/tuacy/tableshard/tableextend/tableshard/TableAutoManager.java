package com.tuacy.tableshard.tableextend.tableshard;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @name: TableAutoManager
 * @author: tuacy.
 * @date: 2019/8/29.
 * @version: 1.0
 * @Description:
 */
public enum TableAutoManager {

    INSTANCE;

    private Logger logger = LoggerFactory.getLogger(TableAutoManager.class);
    private final String DBMODEL_BASE_PACKAGE = "com.pilot.ioserver.basic.biz.pbl.entity.dbmodel";

    private Map<String, TableAutoCreateConfig> dbModelTableShardConfigMap = Maps.newHashMap();


    public void init() {
        // 初始化DBModel，并将信息缓存起来
        List<Class<?>> classess;
        try {
            classess = getDBModelTableShardClassess();
        } catch (Exception e) {
            logger.error("解析DBModel类出现错误，无法初始化。");
            return;
        }

        // 用来监测是否存在绑定表重复
        List<String> tableNameList = Lists.newArrayList();
        for (Class<?> aClass : classess) {
            ATableAutoCreate annotation = aClass.getAnnotation(ATableAutoCreate.class);
            if(annotation == null){
                continue;
            }

            String tableName = annotation.tableName();
            if(tableName.isEmpty()){
                logger.error("存在加了@DBModelTableShardClassSetting注解的DBModel没有绑定表：" + aClass.getName());
                return ;
            }

            if(tableNameList.contains(tableName)){
                logger.error("存在加了@DBModelTableShardClassSetting注解的DBModel重复绑定同一个表");
                return ;
            }

            dbModelTableShardConfigMap.put(tableName, new TableAutoCreateConfig()
                    .setTableName(tableName)
                    .setAutoCreateTableMapperClass(annotation.autoCreateTableMapperClass())
                    .setAutoCreateTableMapperMethodName(annotation.autoCreateTableMapperMethodName()));
        }
    }

    /**
     * 获取包内加了@DBModelTableShardClassSetting的DBModel文件
     * @return 所有加了@DBModelTableShardClassSetting的类
     */
    private List<Class<?>> getDBModelTableShardClassess() throws Exception {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(Thread.currentThread().getContextClassLoader());

        List<Class<?>> classes = Lists.newArrayList();
        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + DBMODEL_BASE_PACKAGE.replace(".", "/") + "/**/*.class";
        Resource[] resources = resolver.getResources(packageSearchPath);
        if(resources.length == 0){
            return Collections.emptyList();
        }

        String packageBasePath = DBMODEL_BASE_PACKAGE.replace(".", File.separator);
        for (Resource resource : resources) {
            try{
                // 改成支持子目录的形式
                String resourceFilePath = resource.getFile().getPath();
                String resourcePackage = resourceFilePath.substring(resourceFilePath.indexOf(packageBasePath)).replace(File.separator, ".").replace(".class", "");
                classes.add((Thread.currentThread().getContextClassLoader().loadClass(resourcePackage)));
            } catch(Exception ignored) {
            }
        }

        List<Class<?>> output = Lists.newArrayList();
        for (Class<?> aClass : classes) {
            if(aClass.isAnnotationPresent(ATableAutoCreate.class)){
                output.add(aClass);
            }
        }

        return output;
    }

}
