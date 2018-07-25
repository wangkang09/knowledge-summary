## ReentrantLock实现原理 ##

### 1 synchronized和lock ###

#### 1.1 synchronized的优势 ####
* synchronized是java内置的关键字，它提供了一种独占的加锁方式
* synchronized的获取和释放锁由JVM实现，用户不需要显示的释放锁，非常方便、
* 因为是内置的，所以会不断优化


#### 1.2 synchronized的局限性 ####
* 当线程尝试获取锁的时候，如果获取不到锁会一直阻塞
* 如果获取锁的线程进入休眠或者阻塞，除非当前线程异常，否则其他线程尝试获取锁必须一直等待

#### 1.3 Lock 简介 ####
    Lock api 如下:
	---
    void lock();
    void lockInterruptibly() throws InterruptedException;
    boolean tryLock();
    boolean tryLock(long time, TimeUnit unit) throws InterruptedException;
    void unlock();
    Condition newCondition();

* 其中最常用的是 lock 和 unlock
* 因为需要手动释放锁，最好使用try..catch，并在finally块中释放锁，这样即使异常，也能成功释放锁，**所以可以interrupt**

### 2 AQS ###
* AbstractQueuedSynchronizer简称AQS，是一个用于构建锁和同步容器的框架
* AQS解决了在实现同步容器时设计的大量细节问题
* concurrent包内许多类都是基于AQS构建，例如ReentrantLock，Semaphore，CountDownLatch，ReentrantReadWriteLock，FutureTask等
* **AQS使用一个FIFO的队列表示排队等待锁的线程**
* 队列头节点称作“哨兵节点”或者“哑节点”，它**不与任何线程关联**
* 其他的节点与等待线程关联，**每个节点维护一个等待状态**waitStatus
![AQS队列](https://raw.githubusercontent.com/wangkang09/shein-note/master/java/Concurrency/img/AQS_Queue.png)