package com.tuacy.study.distributelock.distributedlock;

/**
 * @name: LockFailAction
 * @author: tuacy.
 * @date: 2019/8/20.
 * @version: 1.0
 * @Description:
 */
public enum LockFailAction {
    /**
     * 放弃
     */
    GIVEUP,
    /**
     * 继续
     */
    CONTINUE;
}
