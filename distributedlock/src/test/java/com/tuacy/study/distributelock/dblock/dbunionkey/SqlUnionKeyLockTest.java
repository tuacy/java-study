package com.tuacy.study.distributelock.dblock.dbunionkey;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SqlUnionKeyLockTest {


    @Test
    public void unionKeyLock() {

        SqlUnionKeyLock lock = new SqlUnionKeyLock("OrderNum");
        lock.lock();

    }

}
