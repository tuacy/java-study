package com.tuacy.tableshard.tableextend.tableshard;

import com.google.common.collect.Lists;
import com.tuacy.tableshard.hook.SpringContextHolder;
import com.tuacy.tableshard.utils.ReflectUtil;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ReflectorFactory;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * @name: TableShardInterceptor
 * @author: tuacy.
 * @date: 2019/8/13.
 * @version: 1.0
 * @Description: 自动建表 + 分表 拦截器的实现
 */
@Intercepts({
        @Signature(
                type = StatementHandler.class,
                method = "prepare",
                args = {Connection.class, Integer.class}
        )
})
public class TableShardInterceptor implements Interceptor {

    /**
     * sql语句里面去获取表名的依据（主要，全部是小写的）
     */
    private final static String[] SQL_TABLE_NAME_FLAG_PREFIX = {"from ", "join ", "update ", "insert into "};

    private static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();
    private static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();
    private static final ReflectorFactory REFLECTOR_FACTORY = new DefaultReflectorFactory();

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        if (!(invocation.getTarget() instanceof RoutingStatementHandler)) {
            return invocation.proceed();
        }

        try {
            RoutingStatementHandler statementHandler = (RoutingStatementHandler) invocation.getTarget();
            // MetaObject是mybatis里面提供的一个工具类，类似反射的效果
            MetaObject metaStatementHandler = MetaObject.forObject(statementHandler, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY, REFLECTOR_FACTORY);
            BoundSql boundSql = (BoundSql) metaStatementHandler.getValue("delegate.boundSql");//获取sql语句
            String originSql = boundSql.getSql();

            if (StringUtils.isEmpty(originSql)) {
                return invocation.proceed();
            }

            MappedStatement mappedStatement = (MappedStatement) metaStatementHandler.getValue("delegate.mappedStatement");
            // 判断方法上是否添加了 TableShardAnnotation 注解，因为只有添加了TableShard注解的方法我们才会去做分表处理
            TablePrepareHandler tablePrepareHandler = getTableShardAnnotation(mappedStatement);

            // 没有加@TablePrepareHandler注解则退出
            if (tablePrepareHandler == null) {
                return invocation.proceed();
            }

            boolean enableAutoCreateTable = tablePrepareHandler.enableAutoCreateTable();
            boolean enableTableShard = tablePrepareHandler.enableTableShard();
            // 自动建表和分表是否开启，都没有则退出往下走
            if (!enableAutoCreateTable && !enableTableShard) {
                invocation.proceed();
            }

            String[] appointTable = tablePrepareHandler.appointTable();
            if (appointTable.length == 0) {
                List<String> tableNameList = getTableNamesBySql(originSql);
                if (tableNameList == null || tableNameList.isEmpty()) {
                    return invocation.proceed();
                } else {
                    // 去掉前后空格和/n
                    tableNameList = tableNameList.stream().map(item -> {
                        if (item == null) {
                            return null;
                        }
                        return item.trim().replaceAll("[\r\n]", "");
                    }).collect(Collectors.toList());
                    appointTable = new String[tableNameList.size()];
                    tableNameList.toArray(appointTable);
                }
            }


            // 获取分表表名处理策略
            Class<? extends ITableNameStrategy> strategyClass = tablePrepareHandler.strategy();
            ITableNameStrategy tableStrategy = null;
            if (!strategyClass.equals(TableNameStrategyVoid.class)) {
                tableStrategy = strategyClass.newInstance();
            }

            String dependValue = getDependFieldValue(tablePrepareHandler, metaStatementHandler, mappedStatement);

            // 启用自动建表
            if (tablePrepareHandler.enableAutoCreateTable()) {
                SqlSessionTemplate template = SpringContextHolder.getBean(SqlSessionTemplate.class);
                for (String tableName : appointTable) {
                    TableAutoCreateConfig classConfig = TableCreateManager.INSTANCE.getClassConfig(tableName);
                    if (classConfig == null) {
                        // 没有找到建表语句则跳过
                        continue;
                    }

                    String createSqlMethodPath = classConfig.getAutoCreateTableMapperClass().getName() + "." + classConfig.getAutoCreateTableMapperMethodName();
                    String sql = template.getConfiguration().getMappedStatement(createSqlMethodPath).getBoundSql("delegate.boundSql").getSql();
                    if (StringUtils.isEmpty(sql)) {
                        // 建表sql为空时不理，直接跳过
                        continue;
                    }

                    sql = sql.replace(tableName + "@{suffix}", dependValue == null ? "" : strategyClass == TableNameStrategyVoid.class ? dependValue : tableStrategy.tableName(tableName, dependValue));

                    Connection conn = (Connection) invocation.getArgs()[0];
                    boolean preAutoCommitState = conn.getAutoCommit();
                    conn.setAutoCommit(false);//将自动提交关闭
                    try (PreparedStatement countStmt = conn.prepareStatement(sql)) {
                        // 把新语句设置回去
                        metaStatementHandler.setValue("delegate.boundSql.sql", sql);
                        countStmt.execute();
                        conn.commit();//执行完后，手动提交事务
//                        System.out.println(isSuccess);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        conn.setAutoCommit(preAutoCommitState);//在把自动提交打开
                    }
                }
            }

            // 启用分表
            if (strategyClass != TableNameStrategyVoid.class) {
                if (tablePrepareHandler.enableTableShard()) {
                    String updateSql = originSql;
                    for (String tableName : appointTable) {
                        // 策略处理表名
                        String newTableName = tableStrategy.tableName(tableName, dependValue);
                        for (String item : SQL_TABLE_NAME_FLAG_PREFIX) {
                            updateSql = updateSql.replaceAll(item + tableName, item + newTableName);
                        }
                    }

//                    System.out.println(updateSql);
                    // 把新语句设置回去，替换表名
                    metaStatementHandler.setValue("delegate.boundSql.sql", updateSql);
                }
            }
        } catch (Exception ignored) {
            // ignore 任何一个地方有异常都去执行原始操作 -- invocation.proceed()
        }
        return invocation.proceed();
    }

    /**
     * 从参数里面找到指定对象指定字段对应的值
     */
    private String getDependFieldValue(TablePrepareHandler tablePrepareHandler, MetaObject metaStatementHandler, MappedStatement mappedStatement) throws Exception {

        // 以上情况下不满足则走@TableShardParam机制
        String id = mappedStatement.getId();
        String className = id.substring(0, id.lastIndexOf("."));
        String methodName = id.substring(id.lastIndexOf(".") + 1);
        Method[] methods = Class.forName(className).getMethods();
        Method method = null;
        for (Method me : methods) {
            if (me.getName().equals(methodName) && me.isAnnotationPresent(tablePrepareHandler.annotationType())) {
                method = me;
            }
        }

        if (method == null) {
            return null;
        }

        Parameter[] parameters = method.getParameters();
        if (parameters.length == 0) {
            return null;
        }

        int flag = 0;
        Parameter parameter = null;
        for (Parameter p : parameters) {
            // TableShardParam和Param需要同时添加
            if (p.getAnnotation(TableShardParam.class) != null && p.getAnnotation(Param.class) != null) {
                parameter = p;
                flag++;

            }
        }

        // 参数没有注解则退出
        if (flag == 0) {
            return null;
        }

        // 多个则抛异常
        if (flag > 1) {
            throw new RuntimeException("存在多个指定@TableShardParam的参数，无法处理");
        }

        String tableSharedFieldParamKey = parameter.getAnnotation(Param.class).value();
        TableShardParam annotation = parameter.getAnnotation(TableShardParam.class);
        Class<?> parameterType = parameter.getType();
        if (isPrimitive(parameterType) || StringUtils.isEmpty(annotation.value())) {
            return getPrimitiveParamFieldValue(metaStatementHandler, tableSharedFieldParamKey);
        } else {
            return getParamObjectFiledValue(metaStatementHandler, tableSharedFieldParamKey, annotation.value());
        }
    }

    /**
     * 判断是否是基础类型 9大基础类型及其包装类
     *
     * @return 是否是基础类型
     */
    private boolean isPrimitive(Class<?> clazz) {
        if (clazz.isPrimitive()) {
            return true;
        }

        try {
            if (((Class) clazz.getField("TYPE").get(null)).isPrimitive()) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }

        return clazz.equals(String.class);
    }

    /**
     * 解析sql获取表名
     *
     * @param sql sql
     * @return 表名列表
     */
    private List<String> getTableNamesBySql(String sql) {
        List<String> tableNameList = Lists.newArrayList();

        for (String item : SQL_TABLE_NAME_FLAG_PREFIX) {
            String sqlTemp = sql;
            int tableNameFlag = sqlTemp.toLowerCase().indexOf(item);
            while (tableNameFlag != -1) {
                String afterSql = sqlTemp.substring(tableNameFlag + item.length(), sql.length() - 1).trim();
                sqlTemp = afterSql;
                // 第一个位置是空格
                tableNameList.add(afterSql.substring(0, afterSql.indexOf(" ")));
                tableNameFlag = afterSql.indexOf(item);
            }
        }

        return tableNameList;
    }

    /**
     * 获取方法上的TableShard注解
     *
     * @param mappedStatement MappedStatement
     * @return TableShard注解
     */
    private TablePrepareHandler getTableShardAnnotation(MappedStatement mappedStatement) {
        TablePrepareHandler tablePrepareHandler = null;
        try {
            String id = mappedStatement.getId();
            String className = id.substring(0, id.lastIndexOf("."));
            String methodName = id.substring(id.lastIndexOf(".") + 1);
            final Method[] method = Class.forName(className).getMethods();
            for (Method me : method) {
                if (me.getName().equals(methodName) && me.isAnnotationPresent(TablePrepareHandler.class)) {
                    tablePrepareHandler = me.getAnnotation(TablePrepareHandler.class);
                    break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return tablePrepareHandler;
    }

    /**
     * 从参数里面找到指定对象指定字段对应的值--基础类型
     */
    private String getPrimitiveParamFieldValue(MetaObject metaStatementHandler, String dependFieldName) {
        BoundSql boundSql = (BoundSql) metaStatementHandler.getValue("delegate.boundSql");
        Object parameterObject = boundSql.getParameterObject();
        if (parameterObject == null) {
            return null;
        }

        return ((MapperMethod.ParamMap) parameterObject).get(dependFieldName).toString();
    }

    /**
     * 获取参数里面的对象
     */
    private Object recursiveGetEffectiveObject(Object srcObject) {

        if (!(srcObject instanceof List)) {
            return srcObject;
        }
        Object listItemObject = ((List) srcObject).get(0);
        while (listItemObject instanceof List) {
            listItemObject = ((List) listItemObject).get(0);
        }
        return listItemObject;
    }


    /**
     * 从参数里面找到指定对象指定字段对应的值--对象
     */
    private String getParamObjectFiledValue(MetaObject metaStatementHandler, String fieldParamKey, String dependFieldName) {

        BoundSql boundSql = (BoundSql) metaStatementHandler.getValue("delegate.boundSql");
        Object parameterObject = boundSql.getParameterObject();
        Object filterFiledObject = ((MapperMethod.ParamMap) parameterObject).get(fieldParamKey);

        if (filterFiledObject == null) {
            return null;
        }
        Object dependObject = recursiveGetEffectiveObject(filterFiledObject);
        try {
            return ReflectUtil.getFieldValue(dependObject, dependFieldName);
        } catch (Exception ignored) {
        }

        return null;
    }

    @Override
    public Object plugin(Object target) {
        // 当目标类是StatementHandler类型时，才包装目标类，否者直接返回目标本身,减少目标被代理的次数
        return (target instanceof RoutingStatementHandler) ? Plugin.wrap(target, this) : target;
    }

    @Override
    public void setProperties(Properties properties) {
    }
}
