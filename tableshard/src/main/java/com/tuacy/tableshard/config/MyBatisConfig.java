package com.tuacy.tableshard.config;

import com.tuacy.tableshard.tableextend.tableshard.TableShardInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @name: MyBatisConfig
 * @author: tuacy.
 * @date: 2019/8/30.
 * @version: 1.0
 * @Description:
 */
@Configuration
public class MyBatisConfig {

    /**
     * 分表插件
     */
    @Bean
    public TableShardInterceptor tableSplitInterceptor() {
        return new TableShardInterceptor();
    }


}
