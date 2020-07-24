AQS队列同步器
=================
***一个基于UnSafe实现的公平可重入锁的demo***  
***包含AQS队列同步器核心原理***
----------
使用例子:
-----------------
    Mylock mylock =new Mylock();
    mylock.lock();
    #同步代码块
    mylock.unLock();