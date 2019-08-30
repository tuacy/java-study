package com.tuacy.tableshard.tableextend.multidatasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.google.common.collect.Maps;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.Map;

/**
 * @name: DynamicDataSourceConfig
 * @author: tuacy.
 * @date: 2019/6/24.
 * @version: 1.0
 * @Description: 数据源配置管理
 */

public class DynamicDataSourceConfig {

    /**
     * 基础数据库
     *
     * @return DataSource
     */
    @Bean(name = "basicDataSource")
    @ConfigurationProperties("spring.datasource.druid.basic")
    public DataSource basicDataSource() {
        return new DruidDataSource();
    }

    /**
     * 历史数据库
     *
     * @return DataSource
     */
    @Bean(name = "historyDataSource")
    @ConfigurationProperties("spring.datasource.druid.history")
    public DataSource historyDataSource() {
        return new DruidDataSource();
    }

    /**
     * 统计数据库
     *
     * @return DataSource
     */
    @Bean(name = "statisDataSource")
    @ConfigurationProperties("spring.datasource.druid.statis")
    public DataSource statisDataSource() {
        return new DruidDataSource();
    }

    @Bean
    @Primary
    public DynamicRoutingDataSource dataSource(DataSource basicDataSource, DataSource historyDataSource, DataSource statisDataSource) {
        Map<Object, Object> targetDataSources = Maps.newHashMapWithExpectedSize(3);
        targetDataSources.put(EDataSourceType.BASIC, basicDataSource);
        targetDataSources.put(EDataSourceType.HISTORY, historyDataSource);
        targetDataSources.put(EDataSourceType.STATIS, statisDataSource);
        return new DynamicRoutingDataSource(basicDataSource, targetDataSources);
    }

}
