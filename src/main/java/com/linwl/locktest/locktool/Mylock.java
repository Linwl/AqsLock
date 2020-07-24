package com.linwl.locktest.locktool;

/**
 * @program: locktest
 * @description:
 * @author: linwl
 * @create: 2020-07-24 13:57
 **/
public class Mylock {

    /**
     * 加锁状态
     */
    private volatile int state=0;

    /**
     * 锁的持有者
     */
    private Thread lockHolder =null;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Thread getLockHolder() {
        return lockHolder;
    }

    public void setLockHolder(Thread lockHolder) {
        this.lockHolder = lockHolder;
    }


    /**
     * 加锁
     */
    public void lock(){

    }

    /**
     * 释放锁
     */
    public void unLock()
    {

    }



}
