package com.tuacy.study.lock;

import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @name: ReadWriteLockTest
 * @author: tuacy.
 * @date: 2019/7/30.
 * @version: 1.0
 * @Description:
 */
public class ReadWriteLockTest {


    private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    // 读操作
    private void readFile(Thread thread) {
        readWriteLock.readLock().lock();
        boolean readLock = readWriteLock.isWriteLocked();
        if (!readLock) {
            System.out.println("当前为读锁！");
        }
        try {
            for (int i = 0; i < 5; i++) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(thread.getName() + ":正在进行读操作……");
            }
            System.out.println(thread.getName() + ":读操作完毕！");
        } finally {
            System.out.println("释放读锁！");
            readWriteLock.readLock().unlock();
        }
    }

    // 写操作
    private void writeFile(Thread thread) {
        readWriteLock.writeLock().lock();
        boolean writeLock = readWriteLock.isWriteLocked();
        if (writeLock) {
            System.out.println("当前为写锁！");
        }
        try {
            for (int i = 0; i < 5; i++) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(thread.getName() + ":正在进行写操作……");
            }
            System.out.println(thread.getName() + ":写操作完毕！");
        } finally {
            System.out.println("释放写锁！");
            readWriteLock.writeLock().unlock();
        }
    }


    @Test
    public void readWriteLock() {

        ExecutorService readService = Executors.newCachedThreadPool();
        readService.execute(new Runnable() {
            @Override
            public void run() {
                readFile(Thread.currentThread());
            }
        });
        ExecutorService writeService = Executors.newCachedThreadPool();
        writeService.execute(new Runnable() {
            @Override
            public void run() {
                writeFile(Thread.currentThread());
            }
        });

        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            // ignore
        }
    }

}
