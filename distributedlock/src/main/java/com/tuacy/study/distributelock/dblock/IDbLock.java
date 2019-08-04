package com.tuacy.study.distributelock.dblock;

public interface IDbLock {

    boolean lock();

    void unlock();

}
