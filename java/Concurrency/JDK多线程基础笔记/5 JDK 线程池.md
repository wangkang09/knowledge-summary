## JDK 线程池

### 简介

1. 一个系统的资源有限，线程的创建销毁需要时间与资源、线程本身占用内存（OOM），大量线程回收给GC带来压力
2. 对于线程的使用必须掌握一个度，在有限的范围内，适当的增加线程可以明显的提高系统的吞吐量
3. 为了节省系统在多线程并发时不断的创建与销毁线程带来的开销，需要使用线程池
4. 线程池的基本功能就是进行线程的复用

- 系统接受一个提交的任务，需要一个线程时，并不是直接创建线程，而是先去线程池查找时候有空余的线程，若有；这直接使用；若没有再去创建新的线程。
- 任务完成后，不会简单的销毁线程，而是将线程放入线程池的空闲队列，等待下次使用



### 线程池 Executor 框架

JDK 的 `java.util.concurrent` 是其并发的核心包（JUC）。其中 `ThreadPoolExecutor` 表示一个线程池，`Executors` 是线程池工厂类，通过 `Executors` 可以获取一定功能的线程池

![image](.\cite\线程池.jpg)

## Executors 线程池工厂类

`Executors` 线程池工厂类提供了很多创建不同特性的线程池，分别有一般线程池和定时线程池，这些线程池核心都是 `ThreadPoolExecutor`，如

### 一般线程池

1. `newFixedThreadPool` : 返回固定线程数量的线程池，线程池中的线程数量始终不变

- 当有一个新的任务提交时，线程中有空闲线程，则立即执行
- 若没有，任务会被暂时存在一个任务队列中，等待有空闲线程，便处理在任务队列中的任务

```java
class TaskThread implements Runnable {

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName());
    }
}

public void test2() {
    // 启动三个线程
    ExecutorService executor = Executors.newFixedThreadPool(3);
    executor.execute(new TaskThread());
    executor.execute(new TaskThread());
    executor.execute(new TaskThread());
    executor.execute(new TaskThread());
    executor.execute(new TaskThread());
}

结果：自定义启多少个线程，这边是3个线程
pool-1-thread-1
pool-1-thread-1
pool-1-thread-1
pool-1-thread-2
pool-1-thread-3
```

2. `newSingleThreadExecutor` ：返回一个只有一个线程的线程池

- 若大于1个任务被提交到该线程池，任务保存在任务队列中，等待线程空闲，按先进先出的顺序执行任务

```java
class TaskThread implements Runnable {

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName());
    }
}

public void test1() {
    // 只有一个线程
    ExecutorService executor = Executors.newSingleThreadExecutor();
    executor.execute(new TaskThread());
    executor.execute(new TaskThread());
    executor.execute(new TaskThread());
}

结果：只有一个线程
pool-1-thread-1
pool-1-thread-1
pool-1-thread-1
```

3. `newCachedThreadPool` : 返回一个可根据实际情况调整线程数量的线程池

- 线程池的线程数量不确定，但若有空闲线程可以复用，则会优先复用
- 若所有线程都在工作，又有新的任务提交，则会创建新的线程处理任务
- 所有线程在当前任务执行完毕后，将返回线程池进行复用

```
class TaskThread implements Runnable {

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName());
    }
}

public void test3() {
    // 缓存的线程池 有几个任务就可以有几个线程
    ExecutorService executor = Executors.newCachedThreadPool();
    executor.execute(new TaskThread());
    executor.execute(new TaskThread());
    executor.execute(new TaskThread());
    executor.execute(new TaskThread());
    executor.execute(new TaskThread());
}

结果：有多少任务就起多少线程
pool-1-thread-1
pool-1-thread-2
pool-1-thread-3
pool-1-thread-5
pool-1-thread-4
```

1. `newWorkStealingPool`：并行级别的线程池，并行级别决定了同一时刻最多有多少个线程在执行，并行级别默认是前系统的 CPU 个数

```java
class TaskThread implements Runnable {

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName());

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}

public void test4() {
    // parallelism - 默认为 CPU 个数
    // 观察打印日志，都是2条打印一次，说明同一时刻最多2个线程
    ExecutorService executor = Executors.newWorkStealingPool(2);
    executor.execute(new TaskThread());
    executor.execute(new TaskThread());
    executor.execute(new TaskThread());
    executor.execute(new TaskThread());
    executor.execute(new TaskThread());
    executor.execute(new TaskThread());
}

结果：都是2条打印一次，说明同一时刻最多2个线程
```

### 定时线程池

返回一个 `ScheduledExecutorService` 对象，该接口在 `ExecutorService` 接口之上扩展了给定时间执行某个任务的功能。如在某个固定的延迟之后执行，或者周期性执行某个任务

1. `newSingleThreadScheduledExecutor` : 返回一个 `ScheduledExecutorService` 对象，线程池大小为 1

```java
class ScheduledTaskThread implements Runnable {

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName());
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {

        }
    }
}

// 只有一个线程
ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

// 如 1 秒后输出
executor.schedule(new Thread(new ScheduledTaskThread()), 1, TimeUnit.SECONDS);
executor.schedule(new Thread(new ScheduledTaskThread()), 1, TimeUnit.SECONDS);
executor.schedule(new Thread(new ScheduledTaskThread()), 1, TimeUnit.SECONDS);

输出：只有一个线程
pool-1-thread-1
pool-1-thread-1
pool-1-thread-1
```

2. `newScheduledThreadPool` : 返回一个 `ScheduledExecutorService` 对象，该方法可以指定线程数量

```java
class ScheduledTaskThread implements Runnable {

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName());
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {

        }
    }
}

public void test2() {
    // 启动三个线程
    ScheduledExecutorService executor = Executors.newScheduledThreadPool(3);
    executor.schedule(new Thread(new ScheduledTaskThread()), 1, TimeUnit.SECONDS);
    executor.schedule(new Thread(new ScheduledTaskThread()), 1, TimeUnit.SECONDS);
    executor.schedule(new Thread(new ScheduledTaskThread()), 1, TimeUnit.SECONDS);
}

输出：自定义启多少个线程
pool-1-thread-1
pool-1-thread-2
pool-1-thread-3
```

3. `scheduleAtFixedRate` 与 `scheduleWithFixedDelay` 区别

- initialDelay ：延迟时长，即多少时间后执行
- period ：间隔时长
- `scheduleAtFixedRate` ：在 `initialDelay` 后开始执行，然后在`initialDelay + period` 后执行，接着在 `initialDelay + 2 * period` 后执行，依此类推。与任务内部消耗的时间无关
- `scheduleWithFixedDelay` : 在 `initialDelay` 后开始执行，在每一次任务执行终止（任务内部消耗的时间计算在内）和下一次执行开始之间都存在给定的延迟（period）

```java
public void test3() {
    // 启动三个线程
    ScheduledExecutorService executor = Executors.newScheduledThreadPool(10);

    // 有频率的定时器, 如 1 秒后输出，每隔 2秒再次输出。
    // 也就是将在 initialDelay(1) 后开始执行，然后在initialDelay + period 后执行，接着在 initialDelay + 2 * period 后执行，依此类推。(任务内部消耗的时间无关)
    executor.scheduleAtFixedRate(new Thread(new ScheduledTaskThread()), 1, 2, TimeUnit.SECONDS);


    // 推迟的定时器，如 1 秒后输出，3秒后再次输出。
    // 创建并执行一个在给定初始延迟后首次启用的定期操作，随后，在每一次执行终止和下一次执行开始之间都存在给定的延迟。(任务内部消耗的时间计算在内)
    executor.scheduleWithFixedDelay(new Thread(new ScheduledTaskThread()), 1, 2, TimeUnit.SECONDS);
}
```



## ExecutorService 详解

### ExecutorService api详解

1. `void execute(Runnable command)`：提交一个 Runnable 任务

```java
public void executeTest() {
    // 启动三个线程
    ExecutorService executor = Executors.newFixedThreadPool(3);
    // 提交一个 Runnable  任务
    executor.execute(new InTaskThread());
    // 关闭线程池
    executor.shutdown();
}
```

2. `<T> Future<T> submit(Callable<T> task)`：提交一个带有返回值的任务（Callable），返回值为Future

- 任务的执行完成结果，Future.get()方法返回成功执行后的结果
- 如果你想要阻塞当前线程直到执行完成返回结果，那么你可以这样做 `executor.submit(new InTaskCallable()).get()`

```java
public void submitTest() throws ExecutionException, InterruptedException {
    // 启动三个线程
    ExecutorService executor = Executors.newFixedThreadPool(3);

    // 提交一个 Callable 任务
    Future<String> future = executor.submit(new InTaskCallable());
    String s = future.get();

    // 关闭线程池
    executor.shutdown();
}
```

3. 执行一组任务，等待至少一个任务或者多个任务完成（ExecutorCompletionService 扩展了这些方法的实现），见博客Future相关文章

- `invokeAny()`：执行一组任务，当成功执行完一个任务（没有抛异常），就返回结果，不论正常返回还是异常结束，未执行的任务都会被取消
- `invokeAll()`：执行一组任务，返回一个Future的list，完成的任务可能正常结束或者异常结束，结果在 Future 中。
- `invokeAny`、`invokeAll`另外都还有传入超时参数的方法 `long timeout, TimeUnit unit`

```java
public void batchTest() throws InterruptedException, ExecutionException {
    // 启动三个线程
    ExecutorService executor = Executors.newFixedThreadPool(3);

    // 一个任务完成
    String s = executor.invokeAny(Lists.newArrayList(new InTaskCallable()));
    // 带超时
    executor.invokeAny(Lists.newArrayList(new InTaskCallable()), 1000L, TimeUnit.SECONDS);

    // 多个任务完成
    List<Future<String>> futures = executor.invokeAll(Lists.newArrayList(new InTaskCallable()));
    // 带超时
    executor.invokeAll(Lists.newArrayList(new InTaskCallable()), 1000L, TimeUnit.SECONDS);

}
```

1. 判断线程池状态

- `isShutdown()`：如果线程池停止完成返回 true
- `isTerminated()`：当所有的任务都停止了，返回true. 注意：只有调用 shutdown 或者 shutdownNow 才返回true
- `awaitTermination(60, TimeUnit.SECONDS)`：调用此方法，在shutdown请求发起后，除非以下任何一种情况发生，否则当前线程将一直到阻塞
  - 所有任务执行完成
  - 超过超时时间
  - 当前线程被中断

```java
public void statusTest() throws InterruptedException {
    // 启动三个线程
    ExecutorService executor = Executors.newFixedThreadPool(3);
    // 提交一个 Runnable  任务
    executor.execute(new InTaskThread());

    // 如果线程池停止完成返回true
    executor.isShutdown();

    // 如果线程池停止完成返回true
    // 当所有的任务都停止了，返回true， 注意：只有调用 shutdown 或者 shutdownNow 才返回true
    executor.isTerminated();

    // 调用此方法，在shutdown请求发起后，除非以下任何一种情况发生，否则当前线程将一直到阻塞。
    // 1、所有任务执行完成
    // 2、超过超时时间
    // 3、当前线程被中断
    executor.awaitTermination(60, TimeUnit.SECONDS);
}
```

1. 关闭线程池

- `shutdown()`：发起一个关闭请求，已提交的任务会执行，但不会接受新的任务请求了。这个方法不会等待已提交的任务全部执行完成，如果你希望这样做，可以使用awaitTermination方法
- `shutdownNow()`：这个方法会停掉所有执行中的任务，取消等待中的任务，返回等待执行的任务的list。方法不会等待执行中的任务停止，如果你希望这样做，可以使用awaitTermination方法

```java
public void shutdownTest() {
    // 启动三个线程
    ExecutorService executor = Executors.newFixedThreadPool(3);
    // 提交一个 Runnable  任务
    executor.execute(new InTaskThread());

    // 发起一个关闭请求，已提交的任务会执行，但不会接受新的任务请求了
    executor.shutdown();

    // 立即关闭
    // 这个方法会停掉所有执行中的任务，取消等待中的任务，返回等待执行的任务 的list
    // 方法不会等待执行中的任务停止，如果你希望这样做，可以使用awaitTermination方法
    List<Runnable> runnables = executor.shutdownNow();
}
```

1. 合理的线程池关闭方法，来自 Guava：版本不同，略有出入

- 首先通过调用 shutdown 方法来拒绝加入的任务
- 等待已提交的任务执行完成结束。如果没有成功，调用 shutdownNow 来取消暂停的任务

```java
public static boolean shutdownAndAwaitTermination(ExecutorService service, long timeout, TimeUnit unit) {
    // checkNotNull(unit);
    // Disable new tasks from being submitted
    service.shutdown();
    try {
        long halfTimeoutNanos = TimeUnit.NANOSECONDS.convert(timeout, unit) / 2;
        // Wait for half the duration of the timeout for existing tasks to terminate
        if (!service.awaitTermination(halfTimeoutNanos, TimeUnit.NANOSECONDS)) {
            // Cancel currently executing tasks
            service.shutdownNow();
            // Wait the other half of the timeout for tasks to respond to being cancelled
            service.awaitTermination(halfTimeoutNanos, TimeUnit.NANOSECONDS);
        }
    } catch (InterruptedException ie) {
        // Preserve interrupt status
        Thread.currentThread().interrupt();
        // (Re-)Cancel if current thread also interrupted
        service.shutdownNow();
    }
    return service.isTerminated();
}
```

