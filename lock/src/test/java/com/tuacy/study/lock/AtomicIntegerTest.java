package com.tuacy.study.lock;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @name: AtomicIntegerTest
 * @author: tuacy.
 * @date: 2019/7/30.
 * @version: 1.0
 * @Description:
 */
public class AtomicIntegerTest {

    private AtomicInteger atomicInteger = new AtomicInteger();

    @Test
    public void getAndSet() {
        System.out.println(atomicInteger.getAndSet(10));

        System.out.println(atomicInteger.getAndSet(20));
    }

}
