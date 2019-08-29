package com.tuacy.tableshard.tableextend.multidatasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.Map;

/**
 * @name: DynamicRoutingDataSource
 * @author: tuacy.
 * @date: 2019/6/24.
 * @version: 1.0
 * @Description: 动态数据源设置，每次访问之前设置，访问完成之后在清空
 */
public class DynamicRoutingDataSource extends AbstractRoutingDataSource {

    private static final ThreadLocal<EDataSourceType> contextHolder = new ThreadLocal<>();

    public DynamicRoutingDataSource(DataSource defaultTargetDataSource, Map<Object, Object> targetDataSources) {
        super.setDefaultTargetDataSource(defaultTargetDataSource);
        super.setTargetDataSources(targetDataSources);
        super.afterPropertiesSet();
    }

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
     *
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
