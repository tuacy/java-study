package com.tuacy.tableshard.tableextend.tableshard;

import com.google.common.collect.Lists;
import com.tuacy.tableshard.hook.SpringContextHolder;
import com.tuacy.tableshard.utils.ReflectUtil;
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
import java.sql.SQLException;
import java.util.*;

/**
 * @name: TableShardInterceptor
 * @author: tuacy.
 * @date: 2019/8/13.
 * @version: 1.0
 * @Description: 分表拦截器
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
     * sql中from标识（小写）
     */
    private final static String SQL_FROM_FLAG_PREFIX = "from ";

    /**
     * sql中join标识（小写）
     */
    private final static String SQL_JOIN_FLAG_PREFIX = "join ";

    private static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();
    private static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();
    private static final ReflectorFactory REFLECTOR_FACTORY = new DefaultReflectorFactory();

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        if (invocation.getTarget() instanceof RoutingStatementHandler) {
            try {
                RoutingStatementHandler statementHandler = (RoutingStatementHandler) invocation.getTarget();
                // MetaObject是mybatis里面提供的一个工具类，类似反射的效果
                MetaObject metaStatementHandler = MetaObject.forObject(statementHandler, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY, REFLECTOR_FACTORY);
                BoundSql boundSql = (BoundSql) metaStatementHandler.getValue("delegate.boundSql");//获取sql语句
                String originSql = boundSql.getSql();
                if (!StringUtils.isEmpty(originSql)) {
                    MappedStatement mappedStatement = (MappedStatement) metaStatementHandler.getValue("delegate.mappedStatement");
                    // 判断方法上是否添加了 TableShardAnnotation 注解，因为只有添加了TableShard注解的方法我们才会去做分表处理
                    TablePrepareHandler tablePrepareHandler = getTableShardAnnotation(mappedStatement);
                    if (tablePrepareHandler != null) {
                        boolean enableAutoCreateTable = tablePrepareHandler.enableAutoCreateTable();
                        boolean enableTableShard = tablePrepareHandler.enableTableShard();
                        if (!enableAutoCreateTable && !enableTableShard) {
                            invocation.proceed();
                        }

                        // 策略
                        Class<? extends ITableNameStrategy> strategyClass = tablePrepareHandler.strategy();
                        String dependValue = getDependFieldValue(tablePrepareHandler, metaStatementHandler, mappedStatement);

                        String[] appointTable = tablePrepareHandler.appointTable();
                        if (appointTable.length == 0) {
                            List<String> tableNameList = getTableNamesBySql(originSql);
                            if (tableNameList == null || tableNameList.isEmpty()) {
                                return invocation.proceed();
                            } else {
                                appointTable = new String[tableNameList.size()];
                                tableNameList.toArray(appointTable);
                            }
                        }

                        // 策略实例
                        ITableNameStrategy tableStrategy = null;
                        if (!strategyClass.equals(TableNameStrategyVoid.class)) {
                            tableStrategy = strategyClass.newInstance();
                        }

                        // 启用自动建表
                        if (tablePrepareHandler.enableAutoCreateTable()) {
                            SqlSessionTemplate template = SpringContextHolder.getBean(SqlSessionTemplate.class);
                            for (String tableName : appointTable) {
                                TableAutoCreateConfig classConfig = TableCreateManager.INSTANCE.getClassConfig(tableName);
                                if (classConfig == null) {
                                    // 没有找到建表语句
                                    continue;
                                }

                                String createSqlMethodPath = classConfig.getAutoCreateTableMapperClass().getName() + "." + classConfig.getAutoCreateTableMapperMethodName();
                                String sql = template.getConfiguration().getMappedStatement(createSqlMethodPath).getBoundSql("delegate.boundSql").getSql();
                                sql = sql.replace(tableName + "@{suffix}", dependValue == null ? "" : strategyClass == TableNameStrategyVoid.class ? dependValue : tableStrategy.tableName(tableName, dependValue));

                                Connection conn = (Connection) invocation.getArgs()[0];
                                conn.setAutoCommit(false);//将自动提交关闭
                                try (PreparedStatement countStmt = conn.prepareStatement(sql)) {
                                    // 把新语句设置回去
                                    metaStatementHandler.setValue("delegate.boundSql.sql", sql);
                                    boolean isSuccess = countStmt.execute();
                                    conn.commit();//执行完后，手动提交事务
                                    conn.setAutoCommit(true);//在把自动提交打开
                                    System.out.println(isSuccess);
                                } catch (Exception e) {
                                    e.printStackTrace();
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
                                    updateSql = updateSql.replaceAll(SQL_FROM_FLAG_PREFIX + tableName, SQL_FROM_FLAG_PREFIX + newTableName);
                                    updateSql = updateSql.replaceAll(SQL_JOIN_FLAG_PREFIX + tableName, SQL_JOIN_FLAG_PREFIX + newTableName);
                                }

                                System.out.println(updateSql);
                                // 把新语句设置回去，替换表名
                                metaStatementHandler.setValue("delegate.boundSql.sql", updateSql);
                            }
                        }
                    }
                }
            } catch (Exception ignored) {
                // ignore 任何一个地方有异常都去执行原始操作 -- invocation.proceed()
            }
        }
        return invocation.proceed();
    }

    private String getDependFieldValue(TablePrepareHandler tablePrepareHandler, MetaObject metaStatementHandler, MappedStatement mappedStatement) throws Exception {
        String id = mappedStatement.getId();
        String className = id.substring(0, id.lastIndexOf("."));
        String methodName = id.substring(id.lastIndexOf(".") + 1);
        Method[] methods = Class.forName(className).getMethods();
        Method method = null;
        for (Method me : methods) {
            if (me.getName().equals(methodName) && me.isAnnotationPresent(TablePrepareHandler.class)) {
                method = me;
            }
        }

        Parameter[] parameters = method.getParameters();
        if (parameters.length == 0) {
            return null;
        }

        int flag = 0;
        Parameter parameter = null;
        for (Parameter p : parameters) {
            if (p.getAnnotation(TableShardParam.class) != null) {
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

        TableShardParam annotation = parameter.getAnnotation(TableShardParam.class);
        String dependFileName = annotation.dependFieldName();
        if (StringUtils.isEmpty(dependFileName)) {
            return getPrimitiveParamFieldValue(metaStatementHandler, parameter.getName());
        } else {
            return getParamObjectFiledValue(metaStatementHandler, parameter.getType(), annotation.dependFieldName());
        }
    }

    /**
     * 解析sql获取表名
     *
     * @param sql sql
     * @return 表名列表
     */
    private List<String> getTableNamesBySql(String sql) {
        List<String> tableNameList = Lists.newArrayList();

        // from
        String sqlFromTemp = sql;
        int fromFlag = sqlFromTemp.toLowerCase().indexOf(SQL_FROM_FLAG_PREFIX);
        while (fromFlag != -1) {
            String afterSql = sqlFromTemp.substring(fromFlag + SQL_FROM_FLAG_PREFIX.length(), sql.length() - 1);
            if (!afterSql.contains(" ")) {
                // 表示表在最后，例如select * from test,截取到最后是test，不包含空格
                tableNameList.add(afterSql);
                break;
            }

            tableNameList.add(afterSql.substring(0, afterSql.indexOf(" ")));
            fromFlag = afterSql.indexOf(SQL_FROM_FLAG_PREFIX);
            sqlFromTemp = afterSql;
        }

        // join
        String sqlJoinTemp = sql;
        int joinFlag = sqlJoinTemp.toLowerCase().indexOf(SQL_JOIN_FLAG_PREFIX);
        while (joinFlag != -1) {
            String afterSql = sqlJoinTemp.substring(joinFlag + SQL_FROM_FLAG_PREFIX.length(), sql.length() - 1);
            if (!afterSql.contains(" ")) {
                // 表示表在最后，例如select * from test,截取到最后是test，不包含空格
                tableNameList.add(afterSql);
                break;
            }

            tableNameList.add(afterSql.substring(0, afterSql.indexOf(" ")));
            joinFlag = afterSql.indexOf(SQL_JOIN_FLAG_PREFIX);
            sqlJoinTemp = afterSql;
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
     * 从参数里面找到指定对象指定字段对应的值--对象
     */
    private String getParamObjectFiledValue(MetaObject metaStatementHandler, Class<?> dependClass, String dependFieldName) {
        BoundSql boundSql = (BoundSql) metaStatementHandler.getValue("delegate.boundSql");
        Object parameterObject = boundSql.getParameterObject();
        if (parameterObject == null) {
            return null;
        }
        Object dependObject = null;
        try {
            if (dependClass.equals(void.class)) {
                if (dependFieldName == null || dependFieldName.equals("")) {
                    return String.valueOf(dependObject);
                }
            } else if (dependClass.isInstance(parameterObject)) {
                // 参数是我们需要的对象
                dependObject = parameterObject;
            } else if (parameterObject instanceof ArrayList) {
                // 参数是一个list
                Object listItemObject = ((ArrayList) parameterObject).get(0);
                if (dependClass.isInstance(listItemObject)) {
                    dependObject = listItemObject;
                }
            } else if (parameterObject instanceof Map) {
                // 参数是一个map，遍历各个参数
                for (Map.Entry entry : (Set<Map.Entry>) ((Map) parameterObject).entrySet()) {
                    if (dependClass.isInstance(entry.getValue())) {
                        dependObject = entry.getValue();
                        break;
                    }
                }
            }

            if (dependObject == null) {
                return null;
            }

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
