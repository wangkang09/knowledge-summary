## 公平锁与非公平锁/Synchronized和ReentrantLock的区别

### Synchronized和ReentrantLock的区别

1. Synchronized是JVM层面的，ReentrantLock是JDK层面的
2. Synchronized的锁使用起来和简单，不用手动释放锁，且只有wait/notifiy/notifyAll方法来配合它使用。因为简单所以也不灵活。
3. ReentrantLock比Synchronized的锁灵活的多，主要提供了几个方法
   1. tryLock，尝试获取锁，不成功就返回，不阻塞
   2. tryLock(time)，尝试获取锁，超出时间后，若还是没获取到则返回
   3. lockInterruptibly，优先响应中断，如果当前线程被别的线程中断，会抛出中断异常，而其他方法，不会响应
   4. Condition类，多条件的唤醒和阻塞，比wait/notify更灵活，阻塞队列就用的是多条件



## 非公平锁的区别

1. 在获取锁时，先看state是否为0，如果是，还要看阻塞队列里是否有节点，或当前对象是否是阻塞队列中的第一个节点，如果是才尝试用CAS置state为1。所以肯定是先进阻塞队列的先获取到锁

