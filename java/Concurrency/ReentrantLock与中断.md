## 中断状态

* interrupted()方法与 isInterrupted()方法都是反映当前线程的是否处于中断状态的 
* interrupted()方法会清除中断标识位，isInterrupted()方法不会清除中断标识位 
* interrupted()测试的是**当前线程**的中断状态 
* isInterrupted()测试的是**调用该方法的对象**所表示的线程 
* wait()/sleep()等阻塞方法当被中断时，抛出异常，这是JVM层的协议

```java
//它测试的是当前线程(current thread)的中断状态，且这个方法会清除中断状态,设置中断状态为false
public static boolean interrupted() {
    return currentThread().isInterrupted(true);
}
//isInterrupted()方法不会清除中断状态
public boolean isInterrupted() {
    return isInterrupted(false);
}
private native boolean isInterrupted(boolean ClearInterrupted);
```



```java
public void interrupt() {
    if (this != Thread.currentThread())
        checkAccess();

    synchronized (blockerLock) {
        Interruptible b = blocker;
        if (b != null) {
            interrupt0();           // Just to set the interrupt flag
            b.interrupt(this);
            return;
        }
    }
    interrupt0();
}
```

```java
thread.interrupt();//请求中断thread线程，设置thread线程的中断状态为ture
if (this.interrupted()) {//查看当前线程的中断状态，并设置当前线程中断状态为false
    System.out.println("should be stopped and exit");
    break;
    //更好的方式是直接再这里抛出中断异常，后再catch块中捕获
}

catch(InterruptedException e) {
    //重新使当前线程变为中端状态，使得高层代码获取到该线程的中断状态，并在高层代码去处理
    Thread.currentThread().interrupt();//这样处理比较好
}
```

```java
//关键！抛出了InterruptedExcption异常，当其它线程中断调用此方法的线程时，此线程立即退出阻塞状态，并抛出异常 
public final native void wait(long timeout) throws InterruptedException;

//因为没有进入阻塞状态，所以不用抛中断异常
public static native void yield();

public static native void sleep(long millis) throws InterruptedException;

```



## ReentrantLock

AQS的本质上是一个同步器/阻塞锁的基础框架，其作用主要是提供加锁、释放锁，并在内部维护一个FIFO等待队列，用于存储由于锁竞争而阻塞的线程 

* 非公平锁整个流程
  * 先尝试CAS获取锁，获取成功执行业务代码
  * 获取不成功，获取锁状态，如果是0，再一次CAS尝试获取
    * 如果获取不成功则，尝试加入阻塞队列，如果加入成功，中断自己
  * 如果不是0，则看独占锁是否是当前线程
    * 如果是，状态+1，获取锁成功，进入业务代码
    * 如果不是，尝试加入阻塞队列
* 加入阻塞队列步骤
  * 先通过循环CAS方法将线程节点加入阻塞队列尾部
  * 加入尾部后，先检查该节点的前驱节点是否是head
    * 如果是，再一次尝试获取锁，**获取锁成功**，返回false，**且cancelAcquire**
* cancelAcquire作用（前提是获取锁**失败**）
  * 因为当前节点获取成功了，所以要将当前节点从阻塞队列中移除
  * 先检查当前节点的前驱节点等待状态是否大于0，如果是说明，前驱节点已经被取消了，则把前驱节点移除，一直到前驱节点的状态小于0或前驱节点为head
  * 将当前节点设置成cancel(1)状态
  * 如果当前节点是tail，则直接移除当前节点就可以了，完成工作
  * 如果不是tail(上一步骤移除不成功也说明当前节点不是tail了)
    * 


## ReentrantLock与Synchronized的区别

* **都是可重入锁**：一个线程多次调用相同的锁，不需要重写获取锁，只要将锁计数加1即可，释放锁时，锁计数减1，直到为0时，释放锁资源
* Synchronized时JVM实现的，用起来比较简单，且不需要手动释放锁
* ReentrantLock时JDK实现的，功能比Synchronized更强大，更加灵活，需要再finally块中手动释放锁
* JVM团队会不断对Synchronized锁进行优化，如现在的自旋锁/轻量级锁/偏向锁，以后性能也会越来越好
* ReentrantLock有公平和非公平锁模式，而Synchronized只是公平锁
* ReenTrantLock提供了一个Condition（条件）类，用来实现分组唤醒需要唤醒的线程们（如阻塞集合用到的NOTEMPTY.wait，NOTFULL.wait）这个wait方法底层调用的还是 LockSupport.part(this)
* synchronized要么随机唤醒一个线程要么唤醒全部线程 
* ReenTrantLock提供了一种能够中断等待锁的线程的机制，通过lock.lockInterruptibly()来实现这个机制 
* ReenTrantLock实现了trylock功能，尝试获取锁，或尝试再一段时间内获取锁，若获取不成功则返回false
* lock 必须在 finally 块中释放。否则，如果受保护的代码将抛出异常，锁就有可能永远得不到释放！ 

如果你需要实现ReenTrantLock的三个独有功能时 ，才使用ReentranLock，否则使用Synchronzied