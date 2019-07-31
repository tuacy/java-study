package com.tuacy.study.lock;

import org.junit.Test;

import java.util.concurrent.Semaphore;

/**
 * @name: SemaphoreTest
 * @author: tuacy.
 * @date: 2019/7/30.
 * @version: 1.0
 * @Description:
 */
public class SemaphoreTest {

    // 同步关键类，构造方法传入的数字是多少，则同一个时刻，只运行多少个线程同时运行制定代码
    private static final Semaphore semaphore = new Semaphore(3);

    private static class InformationThread extends Thread {
        private final String name;
        private final int age;

        InformationThread(String name, int age) {
            this.name = name;
            this.age = age;
        }

        @Override
        public void run() {
            try {
                semaphore.acquire();
                System.out.println(Thread.currentThread().getName() + ":大家好，我是:" + name + ",我今年:" + age + "岁。当前时间为：" + System.currentTimeMillis());
                Thread.sleep(1000);
                System.out.println(name + "要准备释放许可证了，当前时间为：" + System.currentTimeMillis());
                System.out.println("当前可使用的许可数为：" + semaphore.availablePermits());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                semaphore.release();
            }
        }
    }

    @Test
    public void semaphore() {
        String[] name = {"李明", "王五", "张杰", "王强", "赵二", "李四", "张三"};
        int[] age = {26, 27, 33, 45, 19, 23, 41};
        for (int i = 0; i < name.length; i++) {
            Thread t1 = new InformationThread(name[i], age[i]);
            t1.start();
        }

        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
