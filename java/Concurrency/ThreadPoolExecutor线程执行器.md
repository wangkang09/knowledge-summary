# ThreadPoolExecutor

## newFixedThreadPool 

```java
public static ExecutorService newFixedThreadPool(int nThreads) {  
    return new ThreadPoolExecutor(nThreads, nThreads,  
                                  0L, TimeUnit.MILLISECONDS,  
                                  new LinkedBlockingQueue<Runnable>());  
} 
```

* 固定大小的线程池
* 当线程达到nThreads时，就不增加了，新来的任务放到阻塞队列中
* 阻塞队列是无界的
* 一般情况下没问题，当任务很大和多的时候，可能导致资源无限消耗



## newCachedThreadPool

```java
public static ExecutorService newCachedThreadPool() {  
    return new ThreadPoolExecutor(0, Integer.MAX_VALUE,  
                                  60L, TimeUnit.SECONDS,  
                                  new SynchronousQueue<Runnable>());  
}  
```

* 核心线程数是0
* 当有任务时，直接新建一个线程
* 线程空闲时间达到60s，则收回，相当于线程由缓存时间
* 适合没有固定大小并且比较快速能完成的小任务，在60s内能重用已创建的线程



## newScheduledThreadPool


```java
public ScheduledThreadPoolExecutor(int corePoolSize) {
    super(corePoolSize, Integer.MAX_VALUE, 0, NANOSECONDS,
          new DelayedWorkQueue());
}
```

* 关键是阻塞队列是一个延时队列
* 是一个无界阻塞队列



## newSingleThreadExecutor

```java
public static ExecutorService newSingleThreadExecutor() {
    return new FinalizableDelegatedExecutorService
        (new ThreadPoolExecutor(1, 1,
                                0L, TimeUnit.MILLISECONDS,
                                new LinkedBlockingQueue<Runnable>()));
}
```

* 由一个线程串行执行任务




## ThreadPoolExecutor最核心的构造方法

| 参数名                   | 作用                                                         |
| ------------------------ | ------------------------------------------------------------ |
| corePoolSize             | 核心线程池大小                                               |
| maximumPoolSize          | 最大线程池大小                                               |
| keepAliveTime            | 线程池中超过corePoolSize数目的空闲线程最大存活时间；可以allowCoreThreadTimeOut(true)使得核心线程有效时间 |
| TimeUnit                 | keepAliveTime时间单位                                        |
| workQueue                | 阻塞任务队列                                                 |
| threadFactory            | 新建线程工厂                                                 |
| RejectedExecutionHandler | 当提交任务数超过maxmumPoolSize+workQueue之和时，任务会交给RejectedExecutionHandler来处理 |

**workQueue**

1. ArrayBlockingQueue :  有界的数组队列  
2. LinkedBlockingQueue : 可支持有界/无界的队列,使用链表实现 
3. PriorityBlockingQueue : 优先队列,可以针对任务排序  
4. SynchronousQueue : 采用这种queue，任何的task会被直接交到thread手中，queue本身不缓存任何的task，所以如果所有的线程在忙的话，新进入的task是会被拒绝的 

****

**RejectedExecutionHandler**：针对任务无法处理时的一些自保护处理 

1. AbortPolicy：直接抛出Reject exception ，默认

2. Discard： 直接忽略该runnable,不可取  

3. DiscardOldest： 丢弃最早入队列的的任务  

4. CallsRun： 直接让原先的client thread做为worker线程,进行执行  

****

**因为execute中加入队列的方法是offer机制，加入可能失败，所以为了不丢失任务，可以实现RejectedExecutionHandler来处理加入失败的任务**

```java
private class CustomRejectedExecutionHandler implements RejectedExecutionHandler {  

    @Override  
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {  
        try {
            // 核心改造点，由blockingqueue的offer改成put阻塞方法
            executor.getQueue().put(r);//一直阻塞到成功加入阻塞队列为止
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }  
} 
```



线程管理机制
![线程管理机制](.\cite\线程管理机制.jpg)

线程管理机制2

![线程管理机制](.\cite\线程管理机制2.jpg)

## 功能

| 功能                        | 实现代码                                                     |
| --------------------------- | ------------------------------------------------------------ |
| 创建线程执行器              | ThreadPoolExecutor executor = ()Executors.newCachedPool();   |
| 普通执行                    | executor.execute(task);                                      |
| 返回结果                    | Future<Object> result = executor.submit(task);//执行完可能result 还是空，等会才有值 |
| 返回第一个成功的结果        | result = executor.invokeAny(taskList);                       |
| 返回所有结果                | resultList = executor.invokeAll(taskList);                   |
| 取消任务                    | result.cancel(true);//true 时，表示任务正在运行也要被取消    |
| [控制任务完成][1]           | 继承 FutureTask，复写 done() 方法                            |
| [分离任务启动与结果处理][2] | 使用 CompletionService 类                                    |



## 总结

* 如果任务量非常大，要用有界队列，防止OOM
* 如果任务量很大，还要求每个任务都处理成功，要对提交的任务进行阻塞提交，重写拒绝机制，改为阻塞提交。保证不抛弃一个任务
* 最大线程数一般设为2N+1最好，N是CPU核数
* 核心线程数，看应用，如果是任务，一天跑一次，设置为0，合适，因为跑完就停掉了，如果是常用线程池，看任务量，是保留一个核心还是几个核心线程数
* 如果要获取任务执行结果，用CompletionService，但是注意，获取任务的结果的要重新开一个线程获取，如果在**主线程**获取，就要等任务都提交后才获取，就**会阻塞大量任务结果**，队列过大OOM，所以最好异步开个线程获取结果



## 参考文献

 [ThreadPoolExecutor使用详解](https://www.cnblogs.com/zedosu/p/6665306.html)

 [ThreadPoolExecutor理解](https://www.aliyun.com/jiaocheng/580149.html)



[1]: ./cite/控制任务完成代码.md
[2]: ./cite/分离任务的启动与结果的处理代码.md



