## ReentrantLock实现原理 ##

### 1. synchronized和lock ###

#### 1.1 synchronized的优势 ####
* synchronized是java内置的关键字，它提供了一种独占的加锁方式
* synchronized的获取和释放锁由JVM实现，用户不需要显示的释放锁，非常方便、
* 因为是内置的，所以会不断优化


#### 1.2 synchronized的局限性 ####
* 当线程尝试获取锁的时候，如果获取不到锁会一直阻塞
* 如果获取锁的线程进入休眠或者阻塞，除非当前线程异常，否则其他线程尝试获取锁必须一直等待

### 2. Lock 简介 ###

