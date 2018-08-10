## 非阻塞同步/无锁并行

### 简介

1. 基于锁的同步方式，也是一种阻塞的线程间的同步方式，无论是信号量（Semaphore）、CountDownLatch、重入锁（ReentrantLock）、内部锁（synchronized），收到核心资源的限制，不同线程间在锁竞争时，总不能避免相互等待，从而阻塞当前线程
2. 非阻塞同步就是为了解决这一问题，最简单的一种非阻塞同步就是 `ThreadLocal`，每个线程拥有各自的独立变量副本，因此计算时，无需相互等待；还有基于比较并交换（Compare And Swap）CAS算法实现的无锁并发
3. 与锁的实现相比，无锁计算的设计和实现较为复杂，但是由于其非阻塞性，对死锁天生免疫，没有锁竞争带来的系统开销，有更加优越的性能

### CAS 算法

1. CAS 算法包含三个参数V、E、N

- `V`：表示要更新的变量
- `E`：表示预期值
- `N`：表示新值

1. 算法过程

- 仅当 V 值等于 E 值时，才会将 V 值设置为 N
- 如果 V 值和 E 值不同，则说明已经有其他线程做了更新，则当前线程什么都不做
- 最后 CAS 返回当前 V 的真实值

1. CAS 操作是抱着乐观的态度进行的，当多个线程同时使用 CAS 操作一个变量时，只有一个会胜出，并成功更新，其余都会失败。
2. JDK 中就是 CAS 实现的并发组件：

- 原子操作类：`AtomicInteger`
- 非阻塞队列：`ConcurrentLinkedQueue`
- 并发Map：`ConcurrentHashMap` （JDK8的设计）。JDK7 是使用`Segment`分段锁，减少锁粒度

## JDK 的原子操作

### 原子操作类（java.util.concurrent.atomic）

1. `AtomicInteger`：整数
2. `AtomicIntegerArray`：整数数组
3. `AtomicLong`：长整型
4. `AtomicIntegerArray`：长整型数组
5. `AtomicReference`：普通对象
6. 等等

### 原子类的核心方法，以 AtomicInteger 为例

1. `get`：获取当前值
2. `set`：设置当前值
3. `getAndSet`：设置新值，并返回旧值
4. `compareAndSet(int expect, int update)`：如果当前值为`expect`，则设置为 `update`
5. `getAndIncrement`：当前值加1，返回旧值
6. `getAndDecrement`：当前值减1，返回旧值
7. `getAndAdd(int delta)`：当前值加 `delta`，返回旧值
8. `incrementAndGet`：当前值加1，返回新值
9. `decrementAndGet`：当前值减1，返回新值
10. `addAndGet(int delta)`：当前值加 `delta`，返回新值

### getAndSet 方法为例

1. `getIntVolatile`：获取当前值
2. `compareAndSwapInt`：更新数据
3. 在 `getAndSetInt` 方法中，是一个do-while 循环，用于多线程间的冲突处理，即当前线程受其他线程影响而更新失败时，会不停的尝试，直到成功

```java
public final int getAndSet(int newValue) {
    return unsafe.getAndSetInt(this, valueOffset, newValue);
}

public final int getAndSetInt(Object var1, long var2, int var4) {
    int var5;
    do {
        var5 = this.getIntVolatile(var1, var2);
    } while(!this.compareAndSwapInt(var1, var2, var5, var4));

    return var5;
}
```

### 性能比较

```java
public class AtomicAndLock {


    void testSyncTask() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(MAX_THREAD);

        long startTime = System.currentTimeMillis();

        SyncTask syncTask = new SyncTask(startTime, this);
        for (int i = 0; i < TASK_COUNT; i++) {
            executorService.execute(syncTask);
        }

        Thread.sleep(10000L);
    }

    void testAtomicTask() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(MAX_THREAD);

        long startTime = System.currentTimeMillis();

        AtomicTask atomicTask = new AtomicTask(startTime);
        for (int i = 0; i < TASK_COUNT; i++) {
            executorService.execute(atomicTask);
        }

        Thread.sleep(10000L);
    }


    /**
     * 线程数
     */
    private static final int MAX_THREAD = 3;
    /**
     * 任务数量
     */
    private static final int TASK_COUNT = 3;
    /**
     * 目标总数
     */
    private static final int TARGET_COUNT = 1000000;

    private AtomicInteger acount = new AtomicInteger(0);

    private int count = 0;

    /**
     * 有所的加法
     *
     * @return
     */
    protected synchronized int inc() {
        return ++count;
    }

    protected synchronized int getCount() {
        return count;
    }

    class SyncTask implements Runnable {
        String name;
        long startTime;
        AtomicAndLock atomicAndLock;


        public SyncTask(long startTime, AtomicAndLock atomicAndLock) {
            this.startTime = startTime;
            this.atomicAndLock = atomicAndLock;
        }

        @Override
        public void run() {
            int inc = atomicAndLock.inc();
            while (inc < TARGET_COUNT) {
                inc = atomicAndLock.inc();
            }

            long endTime = System.currentTimeMillis();
            System.out.println("SyncTask spend : " + (endTime - startTime) + " ms, " + "value = " + inc);
        }
    }


    class AtomicTask implements Runnable {
        String name;
        long startTime;

        public AtomicTask(long startTime) {
            this.startTime = startTime;
        }

        @Override
        public void run() {
            int inc = acount.incrementAndGet();
            while (inc < TARGET_COUNT) {
                inc = acount.incrementAndGet();
            }
            long endTime = System.currentTimeMillis();
            System.out.println("AtomicTask spend : " + (endTime - startTime) + " ms, " + "value = " + inc);
        }
    }

}


输出：性能提升明显
SyncTask spend : 76 ms, value = 1000001
SyncTask spend : 76 ms, value = 1000002
SyncTask spend : 76 ms, value = 1000000

AtomicTask spend : 30 ms, value = 1000000
AtomicTask spend : 30 ms, value = 1000001
AtomicTask spend : 30 ms, value = 1000002
```