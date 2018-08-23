## ReentrantLock与Synchronized的区别

1. 实现机制不一样：JDK层面和JVM层面
2. 公平性：有公平非公平和只有非公平
3. 绑定多个条件：
4. 中断等待锁的线程的机制 ：lock.lockInterruptibly() 
5. ReentrantLock需要再finally块中，释放锁