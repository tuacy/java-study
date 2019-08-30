package com.tuacy.tableshard;

import com.tuacy.tableshard.tableextend.multidatasource.DynamicDataSourceConfig;
import com.tuacy.tableshard.tableextend.tableshard.TableCreateScan;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Import;

@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class
})
@Import({DynamicDataSourceConfig.class})
@MapperScan(basePackages = {"com.tuacy.tableshard.mapper"})
@TableCreateScan(basePackages = {"com.tuacy.tableshard.entity.model"})
public class TableShardBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(TableShardBootApplication.class, args);
        // 我们已经添加了spring-boot-starter-web，所以下面一段代码可以去掉了
        try {
            // 阻塞住，要不然一跑完就结束了，因为我这里没有添加starter-web
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
