## 简介

1. 线程间的协调工作光有锁是不够的，在业务层，需要复杂的线程间协作逻辑。`Condition` 对象可以实现线程间的复杂协作
2. `Condition` 是与锁相关的。通过`lock.newCondition()`方法可以生成一个与锁绑定的`Condition`对象
3. `Condition`与锁的关系，就如同`Object.wait()`、`Object.notify()` 函数与 `synchronized`关键字一样，配合完成多线程间的复杂协作
4. 如果单纯用wait()，notifyAll()同时会唤醒消费者和生产者，粒度比较粗

## `Condition`接口

### 接口方法

1. `await`：使得当前线程等待，同时释放当前锁。当其他线程中使用`signal`、`signalAll`方法时，线程会重新获得锁并继续执行。或者线程中断时，也能跳出等待
2. `awaitUninterruptibly`：与`await`基本相同，但是其不会在等待过程中响应中断
3. `await(long time, TimeUnit unit)`：使得当前线程等待，同时释放当前锁。除了被`signal`唤醒、线程中断，增加了定时唤醒的功能
4. `awaitNanos(long nanosTimeout)`：与 `await(long time, TimeUnit unit)`相同
5. `awaitUntil(Date deadline)`：功能与 `await(long time, TimeUnit unit)` 相同，参数为最后期限时间
6. `signal`：用于唤醒一个在等待中的线程
7. `signalAll`：唤醒所有在等待中的线程

### 简单示例

```java
public class ConditionTest {

    public static void main(String[] args) {
        ObjectData data = new ObjectData();
        new Thread(new MainThread(data), "MainThread_").start();
        new Thread(new SubThread(data), "SubThread_").start();
    }

    static class MainThread implements Runnable {

        private ObjectData data;

        public MainThread(ObjectData data) {
            this.data = data;
        }

        @Override
        public void run() {
            for (int i = 0; i < 3; i++) {
                data.mainMethod(i);
            }
        }
    }

    static class SubThread implements Runnable {

        private ObjectData data;

        public SubThread(ObjectData data) {
            this.data = data;
        }

        @Override
        public void run() {
            for (int i = 0; i < 3; i++) {
                data.subMethod(i);
            }
        }
    }

    /**
     * 数据业务类
     */
    static class ObjectData {
        /**
         * 注意：同一个锁对象
         */
        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
        /**
         * sub 方法先处理
         */
        private boolean subFirst = true;

        void mainMethod(int index) {
            lock.lock();//锁掉

            try {
                while (subFirst) {
                    condition.await();//等待
                }

                for (int i = 0; i <= 2; i++) {
                    System.out.println(Thread.currentThread().getName() + i + ", 第" + index + "循环");
                }

                subFirst = true;
                condition.signal();//唤醒
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();//解锁
            }
        }

        void subMethod(int index) {
            lock.lock();//锁掉
            try {
                while (!subFirst) {
                    condition.await();//等待
                }

                for (int i = 0; i <= 5; i++) {
                    System.out.println(Thread.currentThread().getName() + i + ", 第" + index + "循环");
                }
                subFirst = false;
                condition.signal();//唤醒
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();//解锁
            }
        }
    }
}
```

## 应用

### 阻塞队列

```java
public class CustomQueue {

    final Lock lock = new ReentrantLock();

    /**
     * 两个Condition基于同一个Lock
     * "放"功能的线程的 Condition
     */
    final Condition notFull = lock.newCondition();
    /**
     * "取"功能的线程的 Condition
     */
    final Condition notEmpty = lock.newCondition();

    /**
     * 容器容量为100
     */
    final Object[] items = new Object[100];

    private int putptr, takeptr, count;

    /**
     * 往队列放数据
     *
     * @param x
     * @throws InterruptedException
     */
    public void put(Object x) throws InterruptedException {
        lock.lock();
        try {
            // 如果“缓冲已满”，则等待；直到“缓冲”不是满的，才将x添加到缓冲中。
            while (count == items.length) {
                notFull.await();
            }
            //把 x 放入队列
            items[putptr] = x;
            //如果位置放到100了，从新从 0 开始放
            if (++putptr == items.length) {
                putptr = 0;
            }
            ++count;//放进去一个就 count++
            notEmpty.signal();//唤醒"取"的 Condition
        } finally {
            lock.unlock();
        }
    }

    /**
     * 从队列取数据
     *
     * @return
     * @throws InterruptedException
     */
    public Object take() throws InterruptedException {
        lock.lock();
        try {
            // 如果“缓冲为空”，则等待；直到“缓冲”不为空，才将x从缓冲中取出。
            while (count == 0) {
                // "取"的等待
                notEmpty.await();
            }
            Object x = items[takeptr];
            //取值已到 100 从 0开始取
            if (++takeptr == items.length) {
                takeptr = 0;
            }
            --count;//取走一个 count--
            notFull.signal();//唤醒"放"的
            return x;
        } finally {
            lock.unlock();
        }
    }
}
```

### JDK 阻塞队列实现：ArrayBlockingQueue

1. 定义生成了与 ReentrantLock 绑定的2个 Condition

   ```java
   final ReentrantLock lock;
   private final Condition notEmpty;
   private final Condition notFull;
   ....
   lock = new ReentrantLock(fair);
   notEmpty = lock.newCondition();
   notFull =  lock.newCondition();
   ```

2. `put` 方法：往队列放数据

```java
public void put(E e) throws InterruptedException {
    checkNotNull(e);
    final ReentrantLock lock = this.lock;
    lock.lockInterruptibly();   // 对put做同步
    try {
        while (count == items.length) //如果当前队列已满
            notFull.await();    // 等待队列有足够的空间
        enqueue(e);
    } finally {
        lock.unlock();
    }
}

private void enqueue(E x) {
    // assert lock.getHoldCount() == 1;
    // assert items[putIndex] == null;
    final Object[] items = this.items;
    items[putIndex] = x;
    if (++putIndex == items.length)
        putIndex = 0;
    count++;
    notEmpty.signal();  // 通知需要take的线程，队列已经有数据了
}
```

1. `take` 方法

```java
public E take() throws InterruptedException {
    final ReentrantLock lock = this.lock;
    lock.lockInterruptibly();   // 对 take 做同步
    try {
        while (count == 0)  // 如果队列为空
            notEmpty.await();   // 则消费队列需要等待一个非空的信号
        return dequeue();
    } finally {
        lock.unlock();
    }
}

private E dequeue() {
    // assert lock.getHoldCount() == 1;
    // assert items[takeIndex] != null;
    final Object[] items = this.items;
    @SuppressWarnings("unchecked")
    E x = (E) items[takeIndex];
    items[takeIndex] = null;
    if (++takeIndex == items.length)
        takeIndex = 0;
    count--;
    if (itrs != null)
        itrs.elementDequeued();
    notFull.signal();   // 通知 put 线程已有空间
    return x;
}
```