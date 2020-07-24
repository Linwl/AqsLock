package com.linwl.locktest;

import com.linwl.locktest.locktool.Mylock;
import com.linwl.locktest.locktool.Task;

import java.util.concurrent.CountDownLatch;

/**
 * @program: locktest
 * @description: 起始类
 * @author: linwl
 * @create: 2020-07-24 13:56
 **/
public class app {

   public static void main(String[] args){
       Mylock mylock =new Mylock();
       CountDownLatch countDownLatch = new CountDownLatch(3);
       for (int i=0; i<3; i++) {
           Thread thread = new Thread(new Task(countDownLatch,mylock));
           thread.setName("线程-" + (i+1));
           thread.start();
           countDownLatch.countDown();
       }
    }
}
