package com.linwl.locktest.locktool;

import java.util.concurrent.CountDownLatch;

/**
 * @program: locktest
 * @description:
 * @author: linwl
 * @create: 2020-07-24 14:46
 **/
public class Task implements Runnable{

    private final CountDownLatch countDownLatch;

    private Mylock mylock;

    public Task(CountDownLatch countDownLatch,Mylock mylock) {
        this.countDownLatch = countDownLatch;
        this.mylock = mylock;
    }

    @Override
    public void run() {
        try {
            countDownLatch.await();
            System.out.println(Thread.currentThread().getName() + "启动时间是" + System.currentTimeMillis());
            mylock.lock();
            Thread.sleep(5000);
            mylock.unLock();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
