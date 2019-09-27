package com.tuacy.study.springboot.aware;

import com.tuacy.study.springboot.aware.ImportAware.BeanImportAware;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @name: ImportAwareTest
 * @author: tuacy.
 * @date: 2019/9/27.
 * @version: 1.0
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest()
public class ImportAwareTest {

    private BeanImportAware beanImportAware;

    @Autowired
    public void setBeanImportAware(BeanImportAware beanImportAware) {
        this.beanImportAware = beanImportAware;
    }


    @Test
    public void test() {
        System.out.println(beanImportAware.getName());
    }
}
