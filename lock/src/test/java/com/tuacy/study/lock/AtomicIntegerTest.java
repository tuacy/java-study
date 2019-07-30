package com.tuacy.study.lock;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

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
    public void atomicInteger() {
        System.out.println(atomicInteger.getAndSet(10));

        System.out.println(atomicInteger.getAndSet(20));
    }

    public static final AtomicIntegerFieldUpdater<Person> fieldUpdater = AtomicIntegerFieldUpdater.newUpdater(Person.class, "age");

    @Test
    public void atomicIntegerFieldUpdater() {

        // 可以在多个线程里面操作，线程安全
        Person person = new Person();
        fieldUpdater.set(person, 10);

    }

    class Person {
        private volatile int age;

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }

}
