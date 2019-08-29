package com.tuacy.tableshard.tableextend.tableshard;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @name: AutoCreateTableScanRunner
 * @author: tuacy.
 * @date: 2019/8/29.
 * @version: 1.0
 * @Description: spring 启动之后执行该方法
 */
@Component
public class TableAutoCreateScanRunner implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        TableAutoManager.INSTANCE.init();
    }
}
