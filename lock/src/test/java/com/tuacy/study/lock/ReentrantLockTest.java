package com.tuacy.study.lock;

import com.google.common.util.concurrent.Uninterruptibles;
import org.junit.Test;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @name: ReentrantLockTest
 * @author: tuacy.
 * @date: 2019/7/30.
 * @version: 1.0
 * @Description:
 */
public class ReentrantLockTest {

    private final ReentrantLock reentrantLock = new ReentrantLock();
    private int value = 0;

    //----------------------------ReentrantLock readWriteLock 的使用
    @Test
    public void lock() {
        CountDownLatch latch = new CountDownLatch(20);
        for (int index = 0; index < 20; index++) {
            new Thread(() -> {
                // 使用之前先获取锁
                reentrantLock.lock();
                try {
                    Uninterruptibles.sleepUninterruptibly(1, TimeUnit.SECONDS);
                    value = value + 1;
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    // 使用完之后释放锁
                    reentrantLock.unlock();
                    latch.countDown();
                }
            }).start();
        }

        try {
            // 等待所有的线程执行完
            latch.await();
            System.out.println("value = " + value);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    //-------------------------ReentrantLock condition 的使用

    class Buffer {
        private final ReentrantLock reentrantLock;
        private final Condition fullCondition;
        private final Condition emptyCondition;
        private final int maxSize;
        private final List<Date> storage;

        Buffer(int size) {
            // 使用锁lock，并且创建两个condition，相当于两个阻塞队列
            reentrantLock = new ReentrantLock();
            fullCondition = reentrantLock.newCondition();
            emptyCondition = reentrantLock.newCondition();
            maxSize = size;
            storage = new LinkedList<>();
        }

        // 往队列里面放数据
        public void put() {
            reentrantLock.lock();
            try {
                while (storage.size() == maxSize) {
                    // 如果队列满了
                    System.out.print(Thread.currentThread().getName() + ": wait \n");
                    // 阻塞生产线程
                    fullCondition.await();
                }
                storage.add(new Date());
                System.out.print(Thread.currentThread().getName() + ": put:" + storage.size() + "\n");
                Thread.sleep(1000);
                emptyCondition.signalAll();//唤醒消费线程
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                reentrantLock.unlock();
            }
        }

        // 从队列里面取出数据
        public void take() {
            reentrantLock.lock();
            try {
                while (storage.size() == 0) {
                    // 如果队列满了
                    System.out.print(Thread.currentThread().getName() + ": wait \n");
                    // 阻塞消费线程
                    emptyCondition.await();
                }
                Date d = ((LinkedList<Date>) storage).poll();
                System.out.print(Thread.currentThread().getName() + ": take:" + storage.size() + "\n");
                Thread.sleep(1000);
                // 唤醒生产线程
                fullCondition.signalAll();

            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                reentrantLock.unlock();
            }
        }
    }

    // 生产者
    class Producer implements Runnable {
        private Buffer buffer;

        Producer(Buffer b) {
            buffer = b;
        }

        @Override
        public void run() {
            while (true) {
                buffer.put();
            }
        }
    }

    // 消费者
    class Consumer implements Runnable {
        private Buffer buffer;

        Consumer(Buffer b) {
            buffer = b;
        }

        @Override
        public void run() {
            while (true) {
                buffer.take();
            }
        }
    }

    @Test
    public void condition() {
        Buffer buffer = new Buffer(10);
        Producer producer = new Producer(buffer);
        Consumer consumer = new Consumer(buffer);
        for (int i = 0; i < 3; i++) {
            new Thread(producer, "producer-" + i).start();
        }
        for (int i = 0; i < 3; i++) {
            new Thread(consumer, "consumer-" + i).start();
        }

        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
