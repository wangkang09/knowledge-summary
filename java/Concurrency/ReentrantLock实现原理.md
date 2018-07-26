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

---
### 2 AQS ###
* AbstractQueuedSynchronizer简称AQS，是一个用于构建锁和同步容器的框架
* AQS解决了在实现同步容器时设计的大量细节问题
* concurrent包内许多类都是基于AQS构建，例如ReentrantLock，Semaphore，CountDownLatch，ReentrantReadWriteLock，FutureTask等
* **AQS使用一个FIFO的队列表示排队等待锁的线程**
* 队列头节点称作“哨兵节点”或者“哑节点”，它**不与任何线程关联**
* 其他的节点与等待线程关联，**每个节点维护一个等待状态**waitStatus
![AQS队列](https://raw.githubusercontent.com/wangkang09/knowledge-summary/master/java/Concurrency/img/AQS_Queue.png "AQS队列")

* AQS中还有一个表示状态的字段state
	* ReentrantLocky用它表示线程**重入锁的次数**
	* Semaphore用它表示剩余的**许可数量**
	* FutureTask用它表示**任务的状态**
	* 对state变量值的更新都采用CAS操作保证更新操作的原子性

* AbstractQueuedSynchronizer继承了AbstractOwnableSynchronizer
	* 这个类只有一个变量：exclusiveOwnerThread
	* 表示当前占用该锁的线程，并且提供了相应的get，set方法

---
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

#### 3.3 NonfairSync Lock####
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

* <font color=red>非公平性体现在</font>：如果占用锁的线程刚释放，state置0，而排队等待的线程还**未唤醒**时，新来的线程就直接抢占了该锁  

---
    public final void acquire(int arg) {
    	if (!tryAcquire(arg) &&
    		acquireQueued(addWaiter(Node.EXCLUSIVE), arg))
    		selfInterrupt();
    }

* **尝试获取锁**(tryAcquire)。如果成功直接返回
* **入队**(addWaiter)。由于上文中提到线程A已经占用了锁，所以B和C执行tryAcquire失败，并且入等待队列。如果线程A拿着锁死死不放，那么B和C就会被挂起。
* **挂起**(acquireQueued)。B和C相继执行acquireQueued(final Node node, int arg)。这个方法让已经入队的线程尝试获取锁，若失败则会被挂起。
* **如果有一个线程一直占有，其他线程基本上都被挂起**
* 点击查看[acquire 内部逻辑详情][1]

#### 3.4 NonfairSync UnLock####
    public void unlock() {
    	sync.release(1);
    }
      
    public final boolean release(int arg) {
	    if (tryRelease(arg)) {
		    Node h = head;
		    if (h != null && h.waitStatus != 0)
		    	unparkSuccessor(h);
		    return true;
	    }
	    return false;
    }

* 先尝试释放锁
	* 若成功，查看头节点的状态是否是SIGNAL
		* 如果是，唤醒头结点的下一个节点关联的线程
	* 若失败，返回false，解锁失败

---
    /**
     * 释放当前线程占用的锁
     * @param releases
     * @return 是否释放成功
     */
    protected final boolean tryRelease(int releases) {
	    // 计算释放后state值
	    int c = getState() - releases;
	    // 如果不是当前线程占用锁,那么抛出异常
	    if (Thread.currentThread() != getExclusiveOwnerThread())
	    	throw new IllegalMonitorStateException();
	    boolean free = false;
	    if (c == 0) {
		    // 锁被重入次数为0,表示释放成功
		    free = true;
		    // 清空独占线程
		    setExclusiveOwnerThread(null);
	    }
	    // 更新state值
	    setState(c);
	    return free;
    }

* 当前线程若不持有锁，抛出异常
* 若持有，计算释放后的state值
	* 若为0，表示已经成功释放，清空独占线程，返回true
	* 若没有彻底释放，返回false

![ReentrantLock非公平锁获取流程](https://raw.githubusercontent.com/wangkang09/knowledge-summary/master/java/Concurrency/img/ReentrantLock%E9%9D%9E%E5%85%AC%E5%B9%B3%E9%94%81%E8%8E%B7%E5%8F%96%E6%B5%81%E7%A8%8B.png "ReentrantLock非公平锁获取流程")  

---
#### 3.5 FairSync####
* 和非公平锁的差别：仅仅是**不去检查state状态**

        final void lock() {
            acquire(1);
        }

---
### 4 超时机制 ###

* tryLock(long timeout, TimeUnit unit) 提供了超时获取锁的功能
* 如果在指定的时间内获取到锁，就返回true，否则，返回false
* tryLock使用的都是非公平锁，即使定义的是公平锁

---

    public boolean tryLock(long timeout, TimeUnit unit)
    throws InterruptedException {
    	return sync.tryAcquireNanos(1, unit.toNanos(timeout));
    }

    public final boolean tryAcquireNanos(int arg, long nanosTimeout)
    throws InterruptedException {
    	if (Thread.interrupted())
	    	throw new InterruptedException();
	    	return tryAcquire(arg) ||
	    		doAcquireNanos(arg, nanosTimeout);
    }

* 如果线程被中断，直接抛异常
* 未中断，获取锁
	* 成功，直接返回
	* 失败，进入doAcquireNanos

---
    /**
     * 在有限的时间内去竞争锁
     * @return 是否获取成功
     */
    private boolean doAcquireNanos(int arg, long nanosTimeout)
    throws InterruptedException {
	    // 起始时间
	    long lastTime = System.nanoTime();
	    // 线程入队
	    final Node node = addWaiter(Node.EXCLUSIVE);
	    boolean failed = true;
	    try {
		    // 又是自旋!
		    for (;;) {
			    // 获取前驱节点
			    final Node p = node.predecessor();
			    // 如果前驱是头节点并且占用锁成功,则将当前节点变成头结点
			    if (p == head && tryAcquire(arg)) {
				    setHead(node);
				    p.next = null; // help GC
				    failed = false;
				    return true;
			    }
			    // 如果已经超时,返回false
			    if (nanosTimeout <= 0)
			    	return false;
			    // 超时时间未到,且需要挂起
			    if (shouldParkAfterFailedAcquire(p, node) &&
			    	nanosTimeout > spinForTimeoutThreshold)
				    // 阻塞当前线程直到超时时间到期
				    LockSupport.parkNanos(this, nanosTimeout);
			    long now = System.nanoTime();
			    // 更新nanosTimeout
			    nanosTimeout -= now - lastTime;
			    lastTime = now;
			    if (Thread.interrupted())
				    //相应中断
				    throw new InterruptedException();
		    }
	    } finally {
	    if (failed)
	    cancelAcquire(node);
	    }
    }

* 线程先入队列，然后自旋，尝试获取锁
	* 成功，则返回
	* 失败，则在队列里找一个安全点，把自己挂起，直到超时
* 循环原因
	* 当前线程节点的前驱节点可能不是SIGNAL
	* 那么在这一轮循环中线程不会被挂起，然后更新时间，开始新一轮的尝试

### 总结 ###
**理解AQS，就很容易理解ReentrantLock的实现原理**

[1]:https://github.com/wangkang09/knowledge-summary/blob/master/java/Concurrency/acquires%E4%BB%A3%E7%A0%81%E9%80%BB%E8%BE%91.md