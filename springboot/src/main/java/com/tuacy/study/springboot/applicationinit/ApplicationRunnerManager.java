package com.tuacy.study.springboot.applicationinit;

import com.tuacy.study.springboot.scanrunstart.RunStartManager;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @name: ApplicationRunnerManager
 * @author: tuacy.
 * @date: 2019/8/16.
 * @version: 1.0
 * @Description:
 */
@Component
//@Order(value = 10)
public class ApplicationRunnerManager implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        RunStartManager.INSTANCE.autoStartInvoke();
    }
}
