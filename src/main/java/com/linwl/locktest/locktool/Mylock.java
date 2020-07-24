package com.linwl.locktest.locktool;

import sun.misc.Unsafe;

import java.text.MessageFormat;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.LockSupport;

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

    /**
     * 获取锁等待队列
     */
    private ConcurrentLinkedQueue<Thread> waiters = new ConcurrentLinkedQueue<>();

    /**
     * unsafe魔法类
     */
    private static final Unsafe UNSAFE = UnSafeTool.getUnSafe();

    /**
     * 锁状态偏移量
     */
    private static long stateOffset;

    static {
        try {
            stateOffset = UNSAFE.objectFieldOffset(Mylock.class.getDeclaredField("state"));
        } catch (NoSuchFieldException e) {
            throw new Error(e);
        }
    }

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
        if (acquire()){
            System.out.println(Thread.currentThread().getName() + "加锁成功");
            return;
        }
        //加锁失败，则进入等待队列，等待被唤醒，
        waiters.offer(Thread.currentThread());

        //自旋去获取锁
        for (;;){
            if (acquire()){
                //加锁成功，出队列
                System.out.println(Thread.currentThread().getName() + " 加锁成功");
                waiters.remove(Thread.currentThread());
                return;
            }
            //阻塞并释放cpu使用权
            LockSupport.park();
        }

    }

    /**
     * 释放锁
     */
    public void unLock()
    {
         //检验是否是持锁线程
        if (Thread.currentThread() != getLockHolder()){
            throw new RuntimeException(MessageFormat.format("LockHolder is not current thread, currentThead is {0}," +
                    "LockHolder thread is {1}!",Thread.currentThread().getName(),getLockHolder().getName()));
        }
        //清空持锁线程
        setLockHolder(null);
        //恢复锁状态，必须先清空持锁线程再回复锁状态，否则可能发生：新线程设置持锁线程后，在这儿这里清空了
        setState(0);
        System.out.println(Thread.currentThread().getName() + " 释放锁");
        //唤醒等待队列中第一个线程
        Thread first = waiters.peek();
        if (first != null){
            LockSupport.unpark(first);
            System.out.println(Thread.currentThread().getName() + " 唤醒 " + first.getName());
        }
    }

    /**
     * 尝试加锁
     * @return
     */
    private boolean acquire(){

        //先判断锁是否被持有
        if(getState() == 0)
        {
            if(!shouldPark() &&compareAndSwapInt(getState(),1))
            {
                setLockHolder(Thread.currentThread());
                return true;
            }
        }
        //支持可重入
        if (Thread.currentThread().equals(getLockHolder())){
            int update=getState();
            setState(update++);
            return true;
        }
        return false;
    }


    /**
     * 是否阻塞
     * @return
     */
    private boolean shouldPark(){
        return waiters.size() != 0 && waiters.peek() != Thread.currentThread();
    }

    /**
     * CAS更新锁状态
     * @param expect 预期值
     * @param update 更新后的值
     * @return
     */
    private boolean compareAndSwapInt(int expect, int update){
        return UNSAFE.compareAndSwapInt(this, stateOffset, expect, update);
    }
}
