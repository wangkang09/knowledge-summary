## 线程池 ThreadPoolExecutor

### `Executors` 工厂本质

`Executors` 线程池工厂类提供了很多创建不同特性的线程池核心都是 `ThreadPoolExecutor`，

1. 一般线程池： `newFixedThreadPool()`

```java
public static ExecutorService newFixedThreadPool(int nThreads, ThreadFactory threadFactory) {
    return new ThreadPoolExecutor(nThreads, nThreads,
                                  0L, TimeUnit.MILLISECONDS,
                                  new LinkedBlockingQueue<Runnable>(),
                                  threadFactory);
}
```

1. 定时线程池：`newScheduledThreadPool()`

```java
public static ScheduledExecutorService newScheduledThreadPool(int corePoolSize) {
    return new ScheduledThreadPoolExecutor(corePoolSize);
}

public ScheduledThreadPoolExecutor(int corePoolSize) {
    super(corePoolSize, Integer.MAX_VALUE, 0, NANOSECONDS, new DelayedWorkQueue());
}
public ThreadPoolExecutor(int corePoolSize,
                          int maximumPoolSize,
                          long keepAliveTime,
                          TimeUnit unit,
                          BlockingQueue<Runnable> workQueue) {
    this(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, Executors.defaultThreadFactory(), defaultHandler);
}
```

### ThreadPoolExecutor 详解

#### 构造方法详解

1. `int corePoolSize` ：线程池中的线程数量
2. `int maximumPoolSize` : 线程池中最大的线程数量
3. `long keepAliveTime` : 当线程池中的线程数量，超过了 `corePoolSize` ，多余的空闲线程的存活时间。即超过`corePoolSize`的空闲线程，在多长时间内销毁
4. `TimeUnit unit` : keepAliveTime 的单位时间
5. `BlockingQueue<Runnable> workQueue` : 任务队列，被提交但尚未执行的任务。本身是`BlockingQueue`，存放 `Runnable`
6. `ThreadFactory threadFactory` : 线程工厂，用于创建线程。默认使用`DefaultThreadFactory`。建议使用自定义的`ThreadFactory`，可以自定义线程的名字，如使用 `guava`的 `ThreadFactoryBuilder`
7. `RejectedExecutionHandler handler` : 拒绝策略，当任务太多来不及处理，如何拒绝

```java
public ThreadPoolExecutor(int corePoolSize,
                              int maximumPoolSize,
                              long keepAliveTime,
                              TimeUnit unit,
                              BlockingQueue<Runnable> workQueue,
                              ThreadFactory threadFactory,
                              RejectedExecutionHandler handler)
```

#### 任务队列

1. 直接提交的队列：`SynchronousQueue`

- 没有容量，不保存任务，总是将任务提交给线程执行，如果没有空闲的线程，则创建线程，如达到最大数量，则执行拒绝策略
- 通常需要设置很大的 `maximumPoolSize` 的值（Integer.MAX_VALUE），否则很容易执行异常；而且需要适当的延长 `keepAliveTime` 时间
- `Executors` 中的`newCachedThreadPool()` 是使用直接提交的队列 `SynchronousQueue`

1. 有界任务队列：`ArrayBlockingQueue`，需要设置容量参数，`ArrayBlockingQueue(int capacity)`

- 有新任务需要执行，如果线程池实际线程数小于`corePoolSize`，优先创建新的线程
- 若大于`corePoolSize`，任务进入等待队列
- 等待队列已满，总线程数不大于`maximumPoolSize`的前提下，创建新的线程
- 若大于`maximumPoolSize`，执行拒绝策略
- 所有只有当系统非常繁忙，才有可能将线程数提升到`corePoolSize`以上，否则确保核心线程数维持在`corePoolSize`

1. 无界任务队列：`LinkedBlockingQueue`

- 无界任务队列不会有任务入队列失败的情况
- 有新任务需要执行，如果线程池实际线程数小于`corePoolSize`，优先创建新的线程
- 若已经达到`corePoolSize`，线程不会增加，任务进入等待队列
- 如果任务的增加速度远远高于任务的处理速度，那么任务队列会越来越大，知道耗尽资源
- `Executors` 中的`newSingleThreadExecutor()`、`newFixedThreadPool()` 都是使用无界任务队列 `LinkedBlockingQueue`

1. 优先任务队列：`PriorityBlockingQueue`，控制任务的优先执行顺序，特殊的无界队列

- `ArrayBlockingQueue`、`LinkedBlockingQueue` 都是按照先进先出的算法处理
- `PriorityBlockingQueue` 可以根据任务自身的优先级顺序先后执行

1. 延迟队列：`DelayedWorkQueue`，`ScheduledThreadPoolExecutor`内部类，博客延迟线程池文章

- `ScheduledThreadPoolExecutor` 是`ThreadPoolExecutor` 子类
- `DelayedWorkQueue`，是无界队列
- `Executors` 中的`newScheduledThreadPool()` 使用延迟队列 `DelayedWorkQueue`

#### 拒绝策略

1. JDK 内置的策略：所有的策略实现 `RejectedExecutionHandler` 接口

- `AbortPolicy` : 直接抛出异常，默认的策略
- `DiscardOldestPolicy` : 丢弃最老的一个请求，也就是即将被执行的一个任务，并尝试再次提交当前任务
- `CallerRunsPolicy` : 只要线程池未关闭，该策略直接在调用者线程中，运行当前被丢弃的任务
- `DiscardPolicy` : 丢弃无法处理的任务，不予任何处理

1. 自定义策略：实现 `RejectedExecutionHandler` 接口

- `Runnable r`：请求执行的任务
- `ThreadPoolExecutor executor`：当前的线程池

```java
public interface RejectedExecutionHandler {
    void rejectedExecution(Runnable r, ThreadPoolExecutor executor);
}
```

## 自定义线程池实现

1. 实现有优先级的任务线程

```java
public class CustomTaskThread implements Runnable, Comparable<CustomTaskThread> {

    private String name;

    public CustomTaskThread() {
    }

    public CustomTaskThread(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(100);
            System.out.println(name + " ");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    @Override
    public int compareTo(CustomTaskThread o) {
        //线程名称中标注的优先级
        int me = Integer.parseInt(this.name.split("_")[1]);
        int other = Integer.parseInt(o.name.split("_")[1]);

        if (me > other) {
            return 1;
        } else if (me < other) {
            return -1;
        } else {
            return 0;
        }
    }
}
```

1. 自定义线程池调用执行任务：使用优先级队列 `PriorityBlockingQueue`

```java
/**
 * 建议使用带 ThreadFactory 的构造，方便自定义线程的名字
 * ThreadFactoryBuilder 是 guava 中的类
 */
private static void test2() {

    ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("custom-name-%d").build();
    ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(100, 200, 0, TimeUnit.SECONDS, new PriorityBlockingQueue<>(), threadFactory);
    for (int i = 0; i < 1000; i++) {
        threadPoolExecutor.execute(new CustomTaskThread("CustomThreadPoolExecutor_" + Integer.toString(999 - i)));
    }
    threadPoolExecutor.shutdown();
}
```

1. 执行结果分析：一开始有大量的空闲线程，不需要等待队列而是被直接执行任务。之后就会进入优先级等待队列

```java
CustomThreadPoolExecutor_999 
CustomThreadPoolExecutor_998 
CustomThreadPoolExecutor_997 
.
.
.
CustomThreadPoolExecutor_907 
CustomThreadPoolExecutor_900
CustomThreadPoolExecutor_0 
CustomThreadPoolExecutor_4 
CustomThreadPoolExecutor_1 
CustomThreadPoolExecutor_2 
CustomThreadPoolExecutor_3 
.
.
.
```

## 优化线程池大小

1. 影响线程池大小的因素：CPU、内存等因素
2. 《Java并发编程实践》中有个估算公式

```java
Ncpu = CPU的数量
Ucpu = 目标CPU的使用率, 0<=Ucpu<=1
W/C = 等待时间与计算时间的比率

最优的线程池大小公式：
Nthread = Ncpu * Ucpu * (1 + W/C)
```

1. Java 实现

- CPU 数量： `Runtime.getRuntime().availableProcessors()`

## 扩展 ThreadPoolExecutor：

1. 重载方法 `beforeExecute` 和 `afterExecute`
2. 这两个方法在 `ThreadPoolExecutor.Worker.runWorker()`方法中被调用

```java
final void runWorker(Worker w) {
    ...
    beforeExecute(wt, task);
    Throwable thrown = null;
    try {
        task.run();
    } catch (RuntimeException x) {
        thrown = x; throw x;
    } catch (Error x) {
        thrown = x; throw x;
    } catch (Throwable x) {
        thrown = x; throw new Error(x);
    } finally {
        afterExecute(task, thrown);
    }
    ...
}
```

3. 自定义扩展

```java
ThreadPoolExecutor
public class CustomExpandThreadPoolExecutor extends ThreadPoolExecutor {

    public CustomExpandThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        System.out.println("线程执行开始前" + t.getId() + "_" + t.getName() + "_" + ((CustomTaskThread) r).getName());
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        System.out.println("线程执行结束后 " + Thread.currentThread().getName());
        System.out.println("结束后 PoolSize " + this.getPoolSize());
    }
}
```

