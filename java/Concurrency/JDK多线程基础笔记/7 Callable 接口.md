## Callable 接口

### Runnable 与 Callable

1. 两者都是自定义 Task 可以实现的接口，区别在于 `Callable` 有返回值
2. `Runnable.run()`方法，`Callable.call()` 方法

```java
public interface Runnable {
    public abstract void run();
}

public interface Callable<V> {
    V call() throws Exception; // 有返回值，可以抛出异常
}
```

### JDK 的 Future 实现

1. Future 模式

- 一段程序提交了一个请求，期望得到一个答复，但是如果这个请求的处理是漫长的。单通的单线程，调用函数是同步的
- Future 模式下，调用函数是异步的，而原先等待返回的时间，在主调用函数中，可以处理其他事务

1. JDK 对于Future专门做了封装,主要是 `Callable、Future、FutureTask`

![image](.\cite\FutureTask类图.jpg)

## Future 模式

### Future 接口

1. `boolean cancel(boolean mayInterruptIfRunning)`：取消任务
2. `boolean isCancelled()`：是否已经取消
3. `boolean isDone()`：是否已经完成
4. `V get()`：获取返回结果
5. `V get(long timeout, TimeUnit unit)`：获取返回结果，可以设置超时时间

### FutureTask 类

1. 同时实现 `Runnable`、`Future` 接口
2. 所以它既可以作为 Runnable 被线程执行，又可以作为 Future 得到Callable的返回值

### `ExecutorService` 执行

```java
<T> Future<T> submit(Callable<T> task);
<T> Future<T> submit(Runnable task, T result);
Future<?> submit(Runnable task);
```

### Future 使用

实现 Callable 接口的自定义任务

```java
class InCallableTask implements Callable<String> {
    @Override
    public String call() throws Exception {
        Thread.sleep(1000L);
        return "Hello SAM !! " + Thread.currentThread().getName();
    }
}

class InCallableTaskLongTime implements Callable<String> {
    @Override
    public String call() throws Exception {
        Thread.sleep(10000L);
        return "Hello SAM, Long Time Task : " + Thread.currentThread().getName();
    }
}
```

#### 执行一个任务

1. 一般用法，提交 Callable，使用 Future 接收

```java
void test1() throws ExecutionException, InterruptedException {
    ExecutorService executor = Executors.newFixedThreadPool(1);
    // 提交一个 Callable
    Future<String> future = executor.submit(new InCallableTask());

    System.out.println(future.isDone());

    // 会一直阻塞, 直到任务完成，结果返回
    System.out.println(future.get());
}
```

1. 使用 `FutureTask` 实现

```java
void test2() throws ExecutionException, InterruptedException {
    // Callable 任务使用 FutureTask 封装
    FutureTask<String> future = new FutureTask<>(new InCallableTask());
    ExecutorService executor = Executors.newFixedThreadPool(1);
    // 提交 FutureTask , 因为FutureTask 实现了 Runnable, Future 接口
    executor.submit(future);

    // 会一直阻塞, 直到任务完成，结果返回
    System.out.println("返回的数据： " + future.get());

    executor.shutdown();
}
```

#### 执行多个任务

1. 一般用法：多次提交，任务谁先完成就先返回

```java
void test3() throws Exception {
    ExecutorService executor = Executors.newFixedThreadPool(1);

    // 一般使用需要构建多个 FutureTask 对象
    FutureTask<String> future = new FutureTask<>(new InCallableTask());
    FutureTask<String> futureLongTime = new FutureTask<>(new InCallableTaskLongTime());

    // 提交两个任务
    executor.submit(future);
    executor.submit(futureLongTime);

    System.out.println("干其他事情去吧..........");

    System.out.println(future.get());
    System.out.println(futureLongTime.get());

    executor.shutdown();
}
```

1. `invokeAll`、`invokeAny` 方法实现

- `invokeAll`：任务都完成后一起返回，会等待最慢的那个任务完成后一起返回
- `invokeAny`：返回最先完成的那个任务，未执行的任务都会被取消

```java
void test4() throws ExecutionException, InterruptedException {
    ExecutorService executor = Executors.newFixedThreadPool(1);

    List<Callable<String>> list = new ArrayList<>(2);
    list.add(new InCallableTask());
    list.add(new InCallableTaskLongTime());

    // 需要所有的任务完成后才返回
    // 两个任务都完成后一起返回，会等待最慢的那个任务完成后一起返回
    List<Future<String>> futures = executor.invokeAll(list);
    for (Future<String> future : futures) {
        System.out.println(future.get());
    }

    // 返回最先完成的那个任务，未执行的任务都会被取消
    String result = executor.invokeAny(list);
    System.out.println(result);
}
```

1. 使用 `ExecutorCompletionService` 提交一组任务，先完成的先返回

```java
void test5() throws Exception {
    ExecutorService executor = Executors.newFixedThreadPool(1);
    // 创建 ExecutorCompletionService 类，内部使用 LinkedBlockingQueue 存放 Future，可以手动指定任务个数
    ExecutorCompletionService<String> ecs = new ExecutorCompletionService<>(executor);

    // 任务列表
    ArrayList<Callable<String>> tasks = Lists.newArrayList(new InCallableTask(), new InCallableTaskLongTime());

    List<Future<String>> futures = new ArrayList<>();

    // 提交一组任务
    for (Callable<String> callable : tasks) {
        Future<String> future = ecs.submit(callable);
        // 这边可以
        futures.add(future);
    }

    // 获取任务结果: 内部使用 BlockingQueue 存放 Future, 先完成的先返回
    for (int i = 0; i < tasks.size(); i++) {
        System.out.println(ecs.take().get());
    }

    // 这边应该在 finally 中处理
    for (Future<String> future : futures) {
        future.cancel(true);
    }

    executor.shutdown();
}
```

## Guava 的实现

### 使用 ListenableFuture 提交任务

1. `ListenableFuture` 方法可以添加回调、监控、异常捕获等
2. `MoreExecutors`新增了很多有用的方法，如 关闭线程池
3. 回调函数 `addCallback` 不会阻塞

```java
public void test1() throws ExecutionException, InterruptedException {

    // 自定义线程池名称，使用 ThreadFactory 的构造方法
    ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("guava-ListenableFuture-%s").build();
    ExecutorService executorService = Executors.newFixedThreadPool(10, threadFactory);

    // 将 ExecutorService 转为 ListeningExecutorService
    ListeningExecutorService service = MoreExecutors.listeningDecorator(executorService);
    // 提交任务
    ListenableFuture<String> listenableFuture = service.submit(new InCallableTask());

    // 可以注册一个回调, 在回调函数中对异步处理的结果进行处理
    // 这个方法与 JDK 中的 future.get() 不同，不会 callback() 函数不阻塞当前线程
    Futures.addCallback(listenableFuture, new FutureCallback<String>() {
        // we want this handler to run immediately after we push the big red button!
        @Override
        public void onSuccess(String result) {
            System.out.println("获取的结果 " + result);
        }

        @Override
        public void onFailure(Throwable thrown) {
            thrown.printStackTrace();
        }
    });

    // 上面任务的异常，不会影响主线程后续的执行
    System.out.println("这边会先执行？ main task done.....");

    MoreExecutors.shutdownAndAwaitTermination(service, 60, TimeUnit.SECONDS);
}
```

### 使用 FutureTask 提交任务

针对JDK 的 FutureTask 转换而来的, Guava 提供 `ListenableFutureTask.create(Callable<V>)` 和`ListenableFutureTask.create(Runnable, V)`

```java
public void test2() {
    // 自定义线程池名称，使用 ThreadFactory 的构造方法
    ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("guava-ListenableFutureTask-%s").build();
    ThreadPoolExecutor executorService = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), threadFactory);

    // 将 ThreadPoolExecutor 转为 ListeningExecutorService
    ListeningExecutorService service = MoreExecutors.listeningDecorator(executorService);

    // 创建 ListenableFutureTask
    ListenableFutureTask<String> listenableFutureTask = ListenableFutureTask.create(new InCallableTask());
    // 提交任务
    service.submit(listenableFutureTask);

    Futures.addCallback(listenableFutureTask, new FutureCallback<String>() {
        @Override
        public void onSuccess(String result) {
            System.out.println("获取的结果： " + result);
        }

        @Override
        public void onFailure(Throwable thrown) {
            thrown.printStackTrace();
        }
    });

    System.out.println("这边会先执行？ main task done.....");

    MoreExecutors.shutdownAndAwaitTermination(service, 60, TimeUnit.SECONDS);
}
```

### 提交一组任务

1. `Futures.allAsList(futures)`：如果一个任务发生异常, 这边会抛出异常
2. `Futures.successfulAsList`：返回一个ListenableFuture ，该Future的结果包含所有成功的Future，按照原来的顺序，当其中之一Failed 或者 cancel，则用null替代
3. 以上两个方法都是阻塞的

```java
public void test3() throws ExecutionException, InterruptedException {
    // 自定义线程池名称，使用 ThreadFactory 的构造方法
    ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("guava-ListenableFutureTask-%s").build();
    ThreadPoolExecutor executorService = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), threadFactory);

    // 将 ThreadPoolExecutor 转为 ListeningExecutorService
    ListeningExecutorService service = MoreExecutors.listeningDecorator(executorService);


    // 任务列表
    ArrayList<Callable<String>> tasks = Lists.newArrayList(new InCallableTask(), new InCallableTaskLongTime());

    List<ListenableFuture<String>> futures = Lists.newArrayList();

    // 提交任务
    for (Callable<String> task : tasks) {
        futures.add(service.submit(task));
    }
    // 返回一个ListenableFuture ，该Future的结果包含所有成功的Future，按照原来的顺序，当其中之一Failed或者cancel，则用null替代
    ListenableFuture<List<String>> listListenableFuture = Futures.successfulAsList(futures);

    // 堵塞，直到所有任务完成，然后一起返回
    // [Hello SAM!!  guava-ListenableFutureTask-0, Hello SAM, long time task guava-ListenableFutureTask-0]
    // 如果一个任务发生异常: [null, Hello SAM, long time task guava-ListenableFutureTask-0]
    System.out.println(listListenableFuture.get());

    // allAsList与 successfulAsList的区别
    // 如果一个任务发生异常, 这边会抛出异常，
    ListenableFuture<List<String>> listListenableFuture1 = Futures.allAsList(futures);
    System.out.println(listListenableFuture1.get());

    System.out.println("这边不会先执行 main task done.....");

    // 一定要记得关闭
    MoreExecutors.shutdownAndAwaitTermination(service, 60, TimeUnit.SECONDS);
}
```

### 其他功能（具体见 Guava 文章）

1. 增加异常捕获等

```java
public void test4() throws ExecutionException, InterruptedException {
    // 自定义线程池名称，使用 ThreadFactory 的构造方法
    ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("guava-ListenableFutureTask-%s").build();
    ThreadPoolExecutor executorService = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), threadFactory);

    // 将 ThreadPoolExecutor 转为 ListeningExecutorService
    ListeningExecutorService service = MoreExecutors.listeningDecorator(executorService);

    final InCallableTask inCallableTask = new InCallableTask();
    ListenableFuture<String> listenableFuture = service.submit(inCallableTask);

    Futures.catching(listenableFuture, RuntimeException.class, new Function<RuntimeException, String>() {
        @Override
        public String apply(RuntimeException input) {
            // 这边可以做一些其他事情,，如重新调用，各种异常处理
            return "你好，异常被我吃了";
        }
    });

    Futures.catchingAsync(listenableFuture, RuntimeException.class, new AsyncFunction<RuntimeException, String>() {
        @Override
        public ListenableFuture<String> apply(RuntimeException input) throws Exception {
            return listenableFuture;
        }
    });

    System.out.println(listenableFuture.get());
}
```