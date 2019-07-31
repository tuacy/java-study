package com.tuacy.study.lock;

/**
 * @name: SynchronizedTest
 * @author: tuacy.
 * @date: 2019/7/31.
 * @version: 1.0
 * @Description:
 */
public class SynchronizedTest {


    /**
     * 相对于Object lockObject = new Object();而言
     * 推荐用byte[] lockObject = new byte[0]
     * 后者汇编语句少。执行快
     */
    private final byte[] lockObject = new byte[0];

    /**
     * synchronized用来给方法加锁
     */
    public synchronized void lockFunction() {
        //TODO:
    }

    public void lockBlok() {
        //TODO
        // synchronized给代码块加锁
        synchronized(lockObject) {
            //TODO
        }
        //TODO
    }

}
