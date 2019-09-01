package com.tuacy.tableshard.tableextend.multisource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.Map;

/**
 * @name: DynamicRoutingDataSource
 * @author: tuacy.
 * @date: 2019/6/24.
 * @version: 1.0
 * @Description: 动态数据源设置，每次访问之前设置，访问完成之后在清空
 * (AbstractRoutingDataSource相当于数据源路由中介,能有在运行时, 根据某种key值来动态切换到真正的DataSource上)
 */
public class DynamicRoutingDataSource extends AbstractRoutingDataSource {

    private static final ThreadLocal<EDataSourceType> contextHolder = new ThreadLocal<>();

    /**
     * 构造函数
     *
     * @param defaultTargetDataSource 默认的数据源
     * @param targetDataSources       多数据源每个key对应一个数据源
     */
    public DynamicRoutingDataSource(DataSource defaultTargetDataSource, Map<Object, Object> targetDataSources) {
        // 设置默认数据源
        super.setDefaultTargetDataSource(defaultTargetDataSource);
        // 设置多数据源. key value的形式
        super.setTargetDataSources(targetDataSources);
        super.afterPropertiesSet();
    }

    /**
     * 多数据源对应的key, 会通过这个key找到我们需要的数据源
     */
    @Override
    protected Object determineCurrentLookupKey() {
        return getDataSource();
    }

    /**
     * 设置使用哪个数据源
     *
     * @param dataSource 数据源对应的名字
     */
    public static void setDataSource(EDataSourceType dataSource) {
        contextHolder.set(dataSource);
    }

    /**
     * 获取数据源对应的名字
     * @return 数据源对应的名字
     */
    public static EDataSourceType getDataSource() {
        return contextHolder.get();
    }

    /**
     * 清空掉
     */
    public static void clearDataSource() {
        contextHolder.remove();
    }

}
