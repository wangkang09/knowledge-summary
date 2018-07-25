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
![AQS队列](https://raw.githubusercontent.com/wangkang09/shein-note/master/java/Concurrency/img/AQS_Queue.png "AQS队列")

* AQS中还有一个表示状态的字段state
	* ReentrantLocky用它表示线程重入锁的次数
	* Semaphore用它表示剩余的许可数量
	* FutureTask用它表示任务的状态
	* 对state变量值的更新都采用CAS操作保证更新操作的原子性

* AbstractQueuedSynchronizer继承了AbstractOwnableSynchronizer
	* 这个类只有一个变量：exclusiveOwnerThread
	* 表示当前占用该锁的线程，并且提供了相应的get，set方法


### 3 lock()与unlock()实现原理 ###

#### 3.1 基础知识 ####
* **可重入锁**。可重入锁是指同一个线程可以多次获取同一把锁。ReentrantLock和synchronized都是可重入锁。
* **可中断锁**。可中断锁是指线程尝试获取锁的过程中，是否可以响应中断。
	* synchronized是不可中断锁
	* ReentrantLock则提供了中断功能

* **公平锁与非公平锁**
	* 公平锁是指多个线程同时尝试获取同一把锁时，获取锁的顺序按照线程达到的顺序
	* 非公平锁则允许线程“插队”
	* synchronized是非公平锁
	* ReentrantLock的默认实现是非公平锁，但是也可以设置为公平锁

* **CAS操作**(CompareAndSwap)。CAS 操作包含三个操作数 —— 内存位置（V）、预期原值（A）和新值(B)。如果内存位置的值与预期原值相匹配，那么处理器会自动将该位置值更新为新值。否则，处理器不做任何操作。

#### 3.2 内部结构 ####
    public ReentrantLock() {
      sync = new NonfairSync();
    }
     
    public ReentrantLock(boolean fair) {
      sync = fair ? new FairSync() : new NonfairSync();
    }

* 默认的是非公平锁
* 可以指定成公平锁

#### 3.3 NonfairSync ####
    final void lock() {
    	if (compareAndSetState(0, 1))
    		setExclusiveOwnerThread(Thread.currentThread());
    	else
    		acquire(1);
    }

* 首先用一个CAS操作，判断state是否为0
	* 0表示锁未被占用
	* 如果是0则把它置1
	* 并且设置当前线程为该锁的独占线程

* 非公平性体现在：如果占用锁的线程刚释放，state置0，而排队等待的线程还**未唤醒**时，新来的线程就直接抢占了该锁  

---
    public final void acquire(int arg) {
    	if (!tryAcquire(arg) &&
    		acquireQueued(addWaiter(Node.EXCLUSIVE), arg))
    		selfInterrupt();
    }

* **尝试获取锁**(tryAcquire)。如果成功直接返回
* **入队**(addWaiter)。由于上文中提到线程A已经占用了锁，所以B和C执行tryAcquire失败，并且入等待队列。如果线程A拿着锁死死不放，那么B和C就会被挂起。
* **挂起**(acquireQueued)。B和C相继执行acquireQueued(final Node node, int arg)。这个方法让已经入队的线程尝试获取锁，若失败则会被挂起。
* 点击查看[acquire 内部逻辑详情][1]









[1]:D:\github\repository\shein-note\distributedSystem\CAS.md