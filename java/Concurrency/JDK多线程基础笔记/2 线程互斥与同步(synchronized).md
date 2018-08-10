## 线程互斥（线程安全）

### synchronized 简介

1. `synchronized` 是 Java 内建的同步机制，所以也有人称其为 Intrinsic Locking，它提供了互斥的语义和可见性，当一个线程已经获取当前锁时，其他试图获取的线程只能等待或者阻塞在那里
2. `synchronized` 是 Java 中最为常用的同步方法之一，实现比较简单，代码简洁，可读性和维护性较好
3. 在JDK早期版本中，性能并不好，只适合锁竞争不是特别激烈的场合。目前随着JVM进步得到很好的优化，性能与重入锁（ReentrantLock）差距缩小
4. JDK源码中 `synchronized` 的使用也有很多，如同步容器 `Hashtable`, 同步包装器(Synchronized Wrapper)，我们可以调用 Collections 工具类提供的包装方法，来获取一个同步的包装容器（如 Collections.synchronizedMap）但是它们都是利用非常粗粒度的同步方式，在高并发情况下，性能比较低下
5. 更好的选择是使用并发包（JUC）提供的线程安全容器，这些容器基本是使用重入锁（ReentrantLock）实现，`synchronized` 和 `ReentrantLock`的比较，见重入锁（ReentrantLock）博文

### synchronized 用法

1. 锁定对象：一定要同一个对象

- `synchronized` 锁定非静态方法，这个等同于把方法全部语句用 `synchronized` 块包起来。一个方法建议一个 `synchronized` ，不然容易产生死锁

```java
private synchronized void get(String name) {}
```

- `synchronized` 锁定业务类对象。一般使用`synchronized(this)`

```java
private void get2(String name) {
    int len = name.length();
    synchronized (this) {
        for (int i = 0; i < len; i++) {
            System.out.print(name.charAt(i));
        }
        System.out.println();
    }
}
```

1. `synchronized` 锁定同步块。相比较与锁定对象，锁定块更加精确，减少了锁范围，效率更高。

```java
private void get3(String name) {
    ...
    synchronized (name) {
    
    }
    ...
}
```

1. 锁类：锁定字节码

- 锁静态方法：等价与锁定当前 Class 上

```java
public synchronized static void get5(String name) {}
```

- 锁 Class 对象上。直接显示锁定字节码，如

```java
public static void get4(String name) {
    int len = name.length();
    synchronized (DataObject.class) {
        for (int i = 0; i < len; i++) {
            System.out.print(name.charAt(i));
        }
        System.out.println();
    }
}
```

## 线程同步（多线程复杂交互）

### 方法简介

1. `synchronized`： 保证线程安全，线程互斥
2. `wait`： 可以让线程等待当前对象上的通知(`notify`被调用)，在`wait`的过程中，线程会释放对象锁，供其他线程使用。当接收到对象上的通知后(`notify`被调用)，就能重新获取对象的独占锁，并且继续运行
3. `notify`：可以唤醒一个等待在当前对象上的线程。如果有多个线程等待，讲随机选择一个

### 示例代码

1. 两个线程：子线程循环3次，主线程循环5次，然后子线程又循环3次，主线程5次，如此一共循环2次

- `synchronized`、`wait`、`notify` 操作的必须是同一个对象

```java
public class ThreadCommunication {

    public static void main(String[] args) {
        Data data = new Data();
        new Thread(new MainThread(data), "thead 0 ").start();
        new Thread(new SubThread(data), "thead 1 ").start();
    }

    /**
     * 主线程
     */
    static class MainThread implements Runnable {

        private Data data;

        public MainThread(Data data) {
            this.data = data;
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < 2; i++) {
                    data.mainMethod();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 子线程
     */
    static class SubThread implements Runnable {

        private Data data;

        public SubThread(Data data) {
            this.data = data;
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < 2; i++) {
                    data.subMethod();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 业务对象
     */
    static class Data {
        /**
         * 默认子线程运行
         */
        private volatile boolean isSubRunnable = true;

        synchronized void mainMethod() throws InterruptedException {

            // 主线程先等待
            while (isSubRunnable) {
                this.wait();
            }


            for (int i = 0; i < 5; i++) {
                System.out.println("Main线程运行次数 ： " + i);
            }


            isSubRunnable = true;
            this.notify();
        }

        synchronized void subMethod() throws InterruptedException {

            // 一开始是true,先子线程运行
            while (!isSubRunnable) {
                this.wait();
            }

            for (int i = 0; i < 3; i++) {
                System.out.println("Sub线程运行次数 ： " + i);
            }

            isSubRunnable = false;
            this.notify();//唤醒
        }
    }
}
```

1. 线程同步： 简单实现一个阻塞队列

```java
public class CustomBlockQueue {
    private List<Object> list = new ArrayList<>();

    public synchronized Object pop() throws InterruptedException {
        // 如果队列为空，等待
        while (list.isEmpty()) {
            this.wait();
        }
        if (list.size() > 0) {
            // 队列不为空，返回第一个对象
            return list.remove(0);
        } else {
            return null;
        }
    }

    public synchronized void put(Object object) {
        // 添加到队列当中
        list.add(object);
        // 通知一个 pop()方法，可以取数据
        this.notify();
    }
}
```

### synchronized 总结

1. 代码简洁，线程安全，性能靠谱，功能没有 `ReentrantLock` 丰富
2. 锁对象、锁代码块、锁class类三种用法
3. `synchronized` 加锁的方法之间也是互斥的（队列），原因在于锁定的是同一个对象
4. 只能保证线程安全，无法控制复杂逻辑的多线程交互，如需实现多线程交互，需要配合使用Object对象的 `wait`、`notify` 方法