[TOC]

# 1 Spring

## 1.1 spring AOP

### 代理模式

* 说到spring AOP就要先说一下设计模式中的代理模式，代理模式首先有一个目标类，并且这个目标类要实现一些接口，我们要创建一个代理类，这个代理类实现了目标类相同的接口，并且依赖了抽象接口对象。这样我们把目标类注入进去，并在代理类方法中调用目标类的方法，并且实现一些横切代码，如写日志等。这样这个代理类就可以实现目标类的所有方法，并且增加一些功能。这就是一个简单的静态代理模式。但是这个代理模式明显有一个缺陷，如果一些类实现相同的横切代码，但是这些类实现的接口不一样，我们就必须要为不同的接口实现不同的代理类
* 之后就有了动态代理的机制，它主要由java提供的反射包中的Proxy类和InvocationHandler接口组成。首先，我们要创建一个invocationHandler的实现类，这个类里面依赖了一个Object对象，用来接收目标对象。还重写了invoke方法，在这个方法中，我们插入横切代码，并通过method.invoke()方法反射调用目标对象对应的方法
* 最后使用Proxy.newProxyInstance()方法，根据我们实现的invocationHandler类，动态生成代理对象

```java
public class MyInvocationHandler implements InvocationHandler {

    private Object object;

    public MyInvocationHandler(Object object){
        this.object = object;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // TODO Auto-generated method stub
        System.out.println("MyInvocationHandler invoke begin");
        System.out.println("proxy: "+ proxy.getClass().getName());
        System.out.println("method: "+ method.getName());
        for(Object o : args){
            System.out.println("arg: "+ o);
        }
        //通过反射调用 被代理类的方法
        method.invoke(object, args);
        System.out.println("MyInvocationHandler invoke end");
        return null;
    }
}
ClassLoader loader = Thread.currentThread().getContextClassLoader();
//指明被代理类实现的接口
Class<?>[] interfaces = s.getClass().getInterfaces();
// 创建被代理类的委托类,之后想要调用被代理类的方法时，都会委托给这个类的invoke(Object proxy, Method method, Object[] args)方法
MyInvocationHandler h = new MyInvocationHandler(s);
//生成代理类
Person proxy = (Person)Proxy.newProxyInstance(loader, interfaces, h);

```



```java
//生成的代理类中的方法
public final boolean equals(Object var1) throws  {
    try {
        return ((Boolean)super.h.invoke(this, m1, new Object[]{var1})).booleanValue();
    } catch (RuntimeException | Error var3) {
        throw var3;
    } catch (Throwable var4) {
        throw new UndeclaredThrowableException(var4);
    }
}
```

* 动态生成的代理类中有目标类实现接口的所有方法，并且在对应方法中，调用了invocationHandler类中的invoke方法，这样就间接调用了目标类的方法，并实现了相应的自定义逻辑

### springAOP术语

我是简单定义了一个切面类，切面类里，定义了一些pointcut和advice，通过定义的pointcut指定被增强的方法，advice就是我们的横切代码，这样就实现了aop的整个过程，当客户端调用了pointcut指定的方法时，就会执行我们的横切代码，从而实现额外的逻辑



问题：spring配置文件有上下文的概念，子类上下文可以读取父类上下文的信息，而父类上下文取不到子类中的信息，我在配置切面的时候将切面放到了子类上下文中，而目标类是在controller中，controller是在父类中，初始化父类中的bean时，根本读取不到切面信息，所有controller是不会被动态代理的，父类就是定义前端控制器是定义的配置文件路径



## 1.2 spring IOC

### 原理

首先，IOC是一个控制反转的概念，它把创建对象的任务交给容器，让容器来管理对象和管理对象之间的依赖。

### 容器管理对象的方式

管理对象的方式大致可以分为3种

* 直接编码方式：我们在程序中直接定义一个map，自己手动创建对象，并将对象和对象id放入map中，这样我们需要的时候就直接去map中取
* 配置文件方法：就如spring的xml文件，通过<bean>节点来定义需要注入的对象，spring启动的时候，通过xmlBeanDefinitionReader解析xml文件，将解析后的内容映射到BeanDefinition中，然后将生成的BeanDefinition类注册到BeanDefinitionRegistry中，最后spring通过beanFactory中的bean的注册信息，通过反射调用构造器初始化bean，并包装成beanWrapper对象，之后，对bean的属性进行配置
* 注解接方式，主要由@Component及其子注解、@Autowird和@Resource，最后要开启注解扫描，扫描所有被特定注解注释的类，把它注入容器中
* 管理对象之间依赖的方式也是以上3种

### 依赖注入的方式

依赖注入的方式有3种

* 构造器注入，在bean中声明一个构造器属性，构造器引用一个对象，对于有多个构造器，只能声明一个构造器参数
* setter方式注入，也是在bean中声明一个property属性，property引用了一个对象



## 1.3 spring Bean 生命周期

* 通过xmlBeanDefinitionReader解析xml文件，将解析后的内容映射到BeanDefinition中
* 将生成的BeanDefinition类注册到BeanDefinitionRegistry中
* spring通过beanFactory中的bean的注册信息，通过反射调用构造器初始化bean，并包装成beanWrapper对象
* 对bean进行属性注入
* 如果bean实现了相关Aware接口，在对bean进行相关的配置
* 如果bean实现了beanPostProcessor，对bean进行相关配置，如aop
* 如果bean实现了initializingBean接口或者指定了init-method方法，就调用相关方法
* 如果bean实现了DisposableBean接口或者指定了destory-method方法，在bean生命周期结束时，调用相关方法



# 2 Oracle

## 2.1 索引

### 索引类型

* b+树索引：oracle默认的索引类型，它根据ROWID快速定位访问行
* 位图索引：适用于基数比较少的列，如性别
* 降序索引：默认的索引是升序，可以指定desc变成降序
* 函数索引：索引是某一列或多列的某个函数表达式
* 反转索引：反转了b+数的字节码，使索引更加均匀
* 分区索引：本地索引、全局索引（索引列第一列必须是前缀列）

### 应该创建索引的字段

* 经常作为查询条件的字段
* 经常需要排序的字段
* 经常用作多表连接的字段

### 不应该创建索引的字段

* 经常增删改的表
* 查询中很少用到的字段
* 小表

### 索引失效的情况

* 在索引中使用is null is not null，改为column大于等于或小于等于一个不存在的值
* 使用不匹配的函数
* 不要使用 not != <> ，改为 > or <
* 字段中不要有算术表达式

## 2.2 SQL优化

* 主要sql的执行顺序，将过滤数据最多的条件首先执行，如in和exist的区别
* 不要使用\*，一方面不灵活，另一方面，oracle通过字典把\*转换成实际的字典，浪费时间
* 多列索引用OR会造成全表扫描
* 某些情况用Exist代替Distinct，多表关联，且其中一张表有唯一属性
* group by having好使
* 尽量用>=代替>

## 2.3 事务

事务是一个完整的逻辑单元，一个事务有一个或者多个sql组成，一个事务中的sql要么都执行成功，要么都回滚，从而保持数据的一致性

* 原子性
* 一致性：事务结束后，数据应该和期望的数据一致，就是说执行事务中的数据不能被别的事务更改
* 隔离性：这就涉及到隔离级别了
* 持久性：事务结束后，对数据库的修改是持久的，不能够回滚

## 2.4 隔离级别
| 隔离级别          | 描述     | 脏读 | 不可重复读 | 幻读 |
| ----------------- | -------- | ---- | ---------- | ---- |
| Read   uncommited | 读未提交 | √    | √          | √    |
| Read   commited   | 读已提交 | ×    | √          | √    |
| Repeated   read   | 可重复读 | ×    | ×          | √    |
| Serializable      | 串行读   | ×    | ×          | ×    |

* 不可重复读：对事务读取到的数据，不能修改，但是对没有读到的数据可以增删改查
* 幻读：当对没有读到的数据进行增删改时，一个数据两次读取的条数会不一样，或者总额不一样



# 3 Java锁

## 3.1 wait/sleep/yiled/jion的区别

首先，sleep()是Thread类的静态方法，当当前线程调用Thread.sleep(n)时，当前线程进入休眠状态，但如果获取到锁的话，不释放锁。当休眠时间到了，进入可运行状态，直到获取到cpu时间，进入运行状态；

wait()是Object类的方法，首先必须在Synchronized代码块中，且object.wait()的object对象是Synchronized中指明的对象，当调用object.wait()方法时，当前线程释放锁资源，进入object对象的等待队列中，需要其他线程调用此object对象的notify或notifyAll方法唤醒，使其进入object对象的锁池队列中，当某个线程获取到锁使，进入可运行状态，直到获取到cpu时间，进入可运行状态

yiled和sleep方法相似，都是Thread类的静态方法，调用时，也不释放锁，只是释放cpu时间，直接进入可运行状态，查看是否有相同优先权的线程，如果有将执行权交给它，如果没有继续执行

join方法和wait方法相似，都是Object类的方法。object.join()中的object是一个线程对象，在当前线程中调用此object线程对象的join方法，使得当前线程一直阻塞到object线程完成后，object线程调用object.notifyAll方法唤醒当前线程；原理是当前线程获取了object的线程锁，并在一个while循环中调用object.wait()方法，循环条件是object线程活着，所以只有object线程结束且，调用object.notifyAll方法当前线程才会被真正的唤醒

## 3.2 死锁问题

死锁产生原因：在多个线程进行资源获取的情况下，相互只有了对方的资源，形成了一个循环等待链表

死锁产生的必要条件：

* 互斥条件：一个资源只能被一个线程所持有
* 请求保持条件：当线程请求其他资源时，先不释放以获得的资源，直到请求资源成功，才释放已获得的资源
* 不可剥夺条件：线程已获得的资源，不可被其他线程剥夺，只能自己释放
* 循环等待条件：在发生死锁时，必然存在一个循环等待链表

死锁解决方法：

* 资源一次性分配
* 如果有3个资源和3个线程能造成死锁，那么这3个资源同一个时刻最多只能被其中的两个线程访问，这样就避免了循环等待链表

## 3.3 volatile关键字

可见性：

volatile是Java提供的一种轻量级的同步机制，保证了线程的可见性。当一条线程修改了共享变量的值时，新值对于其他线程来说可以立即得知

1. 当写一个volatile变量时，会把线程工作内存中的值强制刷新都主存中
2. 这个写操作会导致其他线程中的缓存无效

但对于volatile修饰的变量只有是原子操作才能保证线程安全，**因为它只保证可见性和有序性，但没有保证原子性**

禁止指令重排：

指令重排是编译器和处理器为了优化程序对指令进行排序的手段

当一个变量被volatile修饰时，变量之上和变量之下的代码仍可以重排，但它的位置不会变化，如果把synchornized代码块看成一个整体的话，这个整体就相当于volatile的指令重排特性

## 3.4 Synchronized关键字

synchronized是Java內建的同步机制，它提供了一种独占的加锁方法，用户不需要显示的释放锁，保证了线程安全。

1. 作用于方法上时，锁住的是对象实例
2. 作用于静态方法时，锁住的是类实例
3. 作用于某一object对象时，锁住的是object对象实例

`synchronized`、`wait`、`notify` 操作的必须是同一个对象

## 3.5 锁优化

为了获得更好的性能，JVM在内置锁上做了非常多的优化。

**自旋锁**：在线程获取锁失败后，先进行自旋操作一段时间，尝试获取锁，如果不成功再进入等待队列。

* 优点：如果在自旋的时间内获取到锁，则较少了一次线程切换的时间；这个可以使用在锁竞争时间比较短的场景中。
* 缺点：自旋锁要占用CPU，如果是CPU密集型应用，就会得不偿失

**轻量级锁**：使用轻量级锁时，不需要申请互斥量，只需要CAS更新MarkWord中的线程占有标识，如果获取成功，记录锁状态为轻量级。否则，说明已有其他线程获取了轻量级锁，此时发生了锁竞争，会膨胀为重量级锁。也可以先自旋优化下

轻量级锁，在获取轻量级锁时使用一次CAS，释放的时候又使用一次。

偏向锁，只有在获取锁的时候使用一次CAS，是对象一直偏向该线程

**偏向锁**：和轻量级锁类似，不需要申请互斥量。偏向锁假定，从始至终只会有一个线程获取锁资源，通过一次CAS获得MarkWord中的偏向锁标识，退出时不使用CAS释放锁。当有另一个锁尝试CAS时，会使锁膨胀成轻量级。偏向锁不能自旋优化。

**适用场景**：

偏向锁：一个锁资源只会被一个线程所调用的情形

轻量级：基本上不存在锁竞争的情况下，这样就使所有线程不用获取互斥量，还保证了线程安全

自旋锁：锁竞争时间比较短，基本上很少有锁竞争，即使有，几次自旋内就会获取成功



# 4 并发

## 4.1 多线程简单实现

Java实现多线程大体有三种简单方式：继承`Thread`类，实现`Runnable`接口，实现`Callable`接口

## 4.2 线程同步辅助类

线程同步辅助类主要由：Semaphore、countDownLatch、CyclicBarrier、Phaser、Exchanger

### Semaphore

允许并控制多个线程同时访问某一个资源，通过acquire/release来获取和释放资源，要放在try/catch/finally块中，在try中获取资源，在finally块中释放资源

### countDownLatch

有一个线程调用countDownLatch.await()方法，进入阻塞状态；多个线程调用countDownLatch.countDown()方法，当所有线程都调用countDown方法后，唤醒主线程

可以简单实现fork/join的功能

### CyclicBarrier

多个线程共同调用cyclicBarrier.await()方法进入阻塞状态，当调用的次数达到时，唤醒所有线程阻塞的线程，**且还有线程再次调用await方法，则重新计算次数**

### Phaser

并发阶段任务，用来控制多个线程分阶段共同完成任务的情形，如所有学生都考完，才能进行下一场考试

### Exchanger

它提供一个同步点，当两个线程都达到这个同步点时，进行数据交换，可以用于遗传算法和两人互相校对工作

## 4.3 线程池框架

* 线程池框架主要是有三个概念组成：ThreadPoolExecutor、任务（Runnable/Callable/FutureTask）、返回值（Future）
* 其中ThreadPoolExecutor是线程的创建类，它接收任务，并为任务分配线程，并且返回结果。
* CompletionService类是对ThreadPoolExecutor的封装，封装之后，通过completionService来接收任务，内部后形成一个阻塞队列，存放的是完成的任务，这样就可以通过这个列表，优先取到完成的任务
* 线程池框架是一种将线程的创建和执行分离的机制，它基于Executor和ExecutorService接口，并且提供这两个接口的实现类ThreadPoolExecutor，其中Executor接口只有一个execute方法，ExecutorService继承了Executor接口，并定义了一些其他的接口如：sumbit,invoke,shutdown等

**ThreadPoolExecutor**：

我们可以使用**ThreadPoolExecutor**类，来自定义线程池一些参数

| 参数名                   | 作用                                                         |
| ------------------------ | ------------------------------------------------------------ |
| corePoolSize             | 核心线程池大小                                               |
| maximumPoolSize          | 最大线程池大小                                               |
| keepAliveTime            | 线程池中超过corePoolSize数目的空闲线程最大存活时间；可以allowCoreThreadTimeOut(true)使得核心线程有效时间 |
| TimeUnit                 | keepAliveTime时间单位                                        |
| workQueue                | 阻塞任务队列                                                 |
| threadFactory            | 新建线程工厂                                                 |
| RejectedExecutionHandler | 当提交任务数超过maxmumPoolSize+workQueue之和时，任务会交给RejectedExecutionHandler来处理 |

**线程池管理机制**：

当一个任务被提交时，先查看线程池中存在的线程数有没有达到核心线程池大小，如果没有则直接创建一个线程，否则加入阻塞队列，如果阻塞队列已满，查看线程数有没有达到最大线程池大小，如果没有，创建临时线程，空闲的临时线程的存活时间由keepAliveTime指定；如果已经是醉倒线程池大小，则调用RejectedExecutionHandler来处理

**ExecutorService方法说明：**

| 方法        | 说明                                                         |
| ----------- | ------------------------------------------------------------ |
| submit      | submit有三种参数类型 `submit(Runnable a)` `sumbit(Callabe a)` `submit(Runnable a, Object result)`，返回的都是Future类型，submit底层统一将任务转换成FutureTask类型，如果是Runnable任务，但不带参数，则get()方法返回的是null，带result的话，就返回result对象 |
| execute     | execute只有一种参数类型 `execute(Runnable a)` ，只能接受实现了Runanble接口的任务，如FutureTask，无返回值。execute中有个addWorker方法，将任务交给线程池，在线程池了创建一个线程来运行任务，所以是异步的，submit方法中调用了execute方法，将任务加入线程池 |
| invokeAll   | invokeAll传递的是一个继承了Callable接口的集合，底层调用get方法阻塞等待任务结果，只有所有任务都完成后，才返回，是同步方法 |
| invokeAny   | invokeAny传递的是一个继承了Callable接口的集合，底层用completionService包装了线程池，使得可以得到第一个完成的任务，立马返回 |
| shutdown    | 不在接受新的线程，并且等待之前提交的线程都执行完在关闭       |
| shutdownNow | 直接关闭活跃状态的所有的线程 ， 并返回等待中的线程           |

**Runnable、Callable、Future、FutureTask 说明**

| 类型       | 说明                                  |
| ---------- | ------------------------------------- |
| Runnable   | 只有一个无参无返回值的run方法         |
| Callable   | 只有一个无参有返回值的call方法        |
| Future     | 有get、cancel、isDone、isCanceled方法 |
| FutureTask | 是Future和Runnable的实现类            |

**FutureTask**:同时实现了Runnable和Future接口，在线程池中提交的任务，不管是Runnable类型还是Callable类型，都会转换为Callable类型，如果是Runnable，不带参数，则返回null，带参则返回，该参数

**CompletionService **：我们通过CompletionService来包装 ExecutorService类，再通过其take()方法获取Future对象。

```java
ExecutorService executor = Executors.newFixedThreadPool(numThread);
List<Future<String>> futureList = new ArrayList<Future<String>>();
for(int i = 0;i<numThread;i++ ){
    Future<String> future = executor.submit(new CompletionServiceTest1.Task(i));
    futureList.add(future);
}
for(Future<String> future : futureList){
    result = future.get(0, TimeUnit.SECONDS);//这样按照顺序来取，可能会阻塞已经完成的任务
}
```

```java
//completionService封装后的
int numThread = 5;
ExecutorService executor = Executors.newFixedThreadPool(numThread);
CompletionService<String> completionService = new ExecutorCompletionService<String>(executor);
for(int i = 0;i<numThread;i++ ){
    completionService.submit(new CompletionServiceTest.Task(i));
}
for(int i = 0;i<numThread;i++ ){
    //CompletionService的实现是维护一个保存Future对象的BlockingQueue。只有当这个Future对象状态是结束的时候，才会加入到这个Queue中，take()方法其实就是Producer-Consumer中的Consumer。它会从Queue中取出Future对象，如果Queue是空的，就会阻塞在那里，直到有完成的Future对象加入到Queue中
    //所以，先完成的必定先被取出。这样就减少了不必要的等待时间
    System.out.println(completionService.take().get());
}
```

```java
//executor.submit(runnable),将runnable对象转换成FutureTask对象
public <T> Future<T> submit(Runnable task, T result) {
    if (task == null) throw new NullPointerException();
    RunnableFuture<T> ftask = newTaskFor(task, result);
    execute(ftask);
    //任务没完成也直接返回
    return ftask;
}
//FutureTask，通过RunnableAdapter将Runnable适配成了Callable对象，返回的是固定的result
static final class RunnableAdapter<T> implements Callable<T> {
    final Runnable task;
    final T result;
    RunnableAdapter(Runnable task, T result) {
        this.task = task;
        this.result = result;
    }
    public T call() {
        task.run();
        return result;
    }
}
```

```java
public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks)
    throws InterruptedException {
    if (tasks == null)
        throw new NullPointerException();
    ArrayList<Future<T>> futures = new ArrayList<Future<T>>(tasks.size());
    boolean done = false;
    try {
        for (Callable<T> t : tasks) {
            RunnableFuture<T> f = newTaskFor(t);
            futures.add(f);
            execute(f);
        }
        for (int i = 0, size = futures.size(); i < size; i++) {
            Future<T> f = futures.get(i);
            if (!f.isDone()) {
                try {
                    f.get();
                } catch (CancellationException ignore) {
                } catch (ExecutionException ignore) {
                }
            }
        }
        //所有任务都完成了才返回
        done = true;
        return futures;
    } finally {
        if (!done)
            for (int i = 0, size = futures.size(); i < size; i++)
                futures.get(i).cancel(true);
    }
}
```

## 4.4 使用线程的注意事项

* 一个系统的资源有限，线程的创建销毁需要时间与资源、线程本身占用内存（OOM），大量线程回收给GC带来压力
* 对于线程的使用必须掌握一个度，在有限的范围内，适当的增加线程可以明显的提高系统的吞吐量
* 为了节省系统在多线程并发时不断的创建与销毁线程带来的开销，需要使用线程池
* 线程池的基本功能就是进行线程的复用
* 任务完成后，不会简单的销毁线程，而是将线程放入线程池的空闲队列，等待下次使用
* 如果任务量非常大，要用有界队列，防止OOM

## 4.5 Fork/Join框架

* Fork/Join框架主要由两个概念组成：ForkJoinPoll和ForkJoinTask

* 其中ForkJoinPoll和ThreadPoolExecutor一样，也实现了ExecutorService接口，是一个特殊的线程池。它使用一个无限队列来保存需要执行的任务，线程池线程的数量通过构造函数传入，默认可用CPU数量

* ForkJoinTask是个任务模型，由ForkJoinPoll来接收它，并通过ForkJoinTask中的compute方法来实现任务的拆分与合并，用fork进行拆分，用join进行合并

  ```java
  @Override
  protected Integer compute() {
      int sum = 0;
      //如果任务足够小就计算
      boolean canCompute = (end - start) <= THREAD_HOLD;
      if(canCompute){
          for(int i=start;i<=end;i++){
              sum += i;
          }
      }else{
          int middle = (start + end) / 2;
          CountTask left = new CountTask(start,middle);
          CountTask right = new CountTask(middle+1,end);
          //执行子任务
          left.fork();
          right.fork();
          //获取子任务结果
          int lResult = left.join();//在这里采用工作窃取算法
          int rResult = right.join();
          sum = lResult + rResult;
      }
      return sum;
  }
  ```

  **注意：**

  * 使用ForkJoinPool能够使用数量有限的线程来完成非常多的具有父子关系的任务，比如使用4个线程来完成超过200万个任务。但是，使用ThreadPoolExecutor时，是不可能完成的，因为ThreadPoolExecutor中的Thread无法选择优先执行子任务，需要完成200万个具有父子关系的任务时，也需要200万个线程，显然这是不可行的 
  * ThreadPoolExecutor仅仅是为了，不频繁的创建和销毁线程
  * ForkJoinPool是为了使用固定的线程，完成具有父子关系的任务的拆分与合并

## 4.6 阻塞集合

阻塞集合内部是使用锁机制来保证线程安全的，如，LinkedBlockingDeque，CopyOnWriteXXX，SynchronizedXXX，ConcurrentHashMap ，这些在添加元素的时候会获取锁，会被阻塞

```java
//LinkedBlockingDeque
public E peekFirst() {
    final ReentrantLock lock = this.lock;
    lock.lock();
    try {
        return (first == null) ? null : first.item;
    } finally {
        lock.unlock();
    }
}
```

4.7 非阻塞集合

非阻塞集合内部使用循环CAS来保证线程安全，如，ConcurrentLinkedQueue(单向链表)，ConcurrentLinkedDeque，它从尾部添加元素，同步移除元素

- poll最关键的是头指针的值一定不为null这个条件！！
- 如果是null肯定被别的线程操作了
- 另一个关键点是casItem(item,null)是原子操作
- 当操作完成之后，item变成了null，值变了
- offer操作就是循环CAS，不成功重新获取tail指针，直到成功为止

# 5 设计模式

* **工厂模式**：将对象的创建交给工厂，有利于对象的修改，如果IOC一样
* **适配器模式**：将服务端的接口，适配成客户端需要的接口，解决了客户端接口和服务端接口的耦合
* **装饰器模式**：写一个装饰类，封装目标类，并添加装饰功能
* **代理模式**：静态代理装饰模式差不多，但和动态代理不一样，动态代理主要依赖于Proxy类和InvocationHandler接口实现的在程序运行期间动态创建类
* **访问者模式**：主要由两个对象，稳定的数据结构对象，不断变化的数据访问者对象，通过动态多分配原理**解决了数据结构和访问者之间的耦合**
* **门面模式**：将服务端的接口封装起来，给客户端调用，客户端不需要了解服务端内部的逻辑
* **观察者模式**：定义了一种一对多的依赖关系，一个主题和多个观察者，当主题发生变化时，主动通知它的所有观察者对象，解决了主题和观察者之间的耦合
* **命令模式**：为了解决命令请求者和命令实现者之间的耦合关系，可以分别对请求进行处理，和对命令进行处理，方便对命令的扩展
* **责任链模式**：有一个请求处理链，每个请求会按顺序遍历链中的各个节点，所有可以很方便的在这个链中增加和移除一些节点
* **策略模式**：策略模式定义了一系列算法，并将每一个算法封装起来，而且他们可以相互替换，让算法独立于使用它的客户而独立变化
* **单例模式**：每个类只有一个对象
* **享元模式**：类似于缓存的概念，有一个map存储已有的对象，如果客户端取得对象不在里面，则创建对象，并加入map
* **原型模式**：多例，通过克隆技术创建多例
* **建造者模式**：使用多个简单对象一步一步建造成复杂对象
* **桥接模式**：就行数据库驱动和
* **模板模式**：在一个抽象类中定义了执行它的方法或模板，它的资料可以按照需要重新方法的实现，但是只能按抽象类中的定义方式执行
* **组合模式**：将对象组合成树形结构以表示"部分-整体"的层次结构 ，文件夹与文件



# 6 JVM

## 6.1 JVM基本概念

JVM是可运行Java代码的虚拟计算机，包括一套字节码指令集、一组寄存器、一个栈、一个垃圾回收器、一个堆和一个存储方法域。JVM运行在操作系统之上，与硬件没有直接交互，所以实现了一处编译，处处运行

## 6.2 JVM内存区域

JVM内存区域分为：**方法区**、**虚拟机栈**、**堆**、**本地方法栈**、**程序计数器**

* **方法区**：主要存储已被虚拟机加载的类信息、常量、静态变量和即时编译器编译后的代码等数据。**是线程共享的**。方法区里有一个运行时常量池，用于存放即时编译产生的字面量和符号引用，具有动态性，运行时生成的常量也在这个池里，String.intern()
* 虚拟机栈：就是栈内存，每个方法在执行的时候都会创建一个栈帧，用于存放局部变量表[^2]、操作数栈[^3]、动态链接[^1]和方法出口等信息。**是线程私有的**。
* 本地方法栈：调用Native方法
* 堆：是线程共享的。对象实例以及数组 

# 7 JavaWeb

## 7.1 Session与Cookie

因为HTTP的无状态特性，引入了Session和Cookie机制，为了保持访问用户与后端服务器的交互状态，这两个技术各有利弊

* Cookie存储在客户端，字节数大，当访问量很大时，占用带宽多，也存在安全问题
* Session存储在服务器端，不容易在多台服务器之间共享

### Session产生过程

* SessionId 是客户端第一次访问时，会在服务端生成一个session，包含一个sessionId属性。由tomcat生成的sessionId叫做jsessionId
* 当浏览器不支持Cookie时，浏览器会将用户的SessionCookieName 重写到用户请求的URL参数中

## 7.2 Servlet

Servlet是sun公司提供的一门用于开发动态web资源的技术 ,通常我们把实现了servlet接口的java类称为servlet

### servlet生命周期

当客户端请求资源时，如果容器中没有servelt实例，创建相应的servlet实例，并调用init()方法初始化，之后调用service方法判断请求类型，并分发到相应的执行方法中去，每一次请求都要调用service方法

开发者只要使java类继承HttpServlet，并重新doXXX方法就可以实现一个完整的servlet类

## 7.3 listener

Listener 用于监听 java web程序中的事件，例如创建、修改、删除Session、request、context等，并触发响应的事件，我们可以针对sessin、request已经context的增删改做一下额外的扩展，如记录在线人数，初始化数据库连接池等

## 7.4 filter

我们可以通过filter对客户端的特定请求进行拦截，而是扩展功能，如权限控制，过滤敏感词汇，编解码

## 7.5 异常处理

异常处理的方式大概有3种

* 在XML里配置simpleMappingExceptionResovler类，针对不同的异常类，返回特定的错误页面，我们需要自定义一些不同的异常类，如BussinessExcetption，parmaterException，系统将会根据所抛出的异常类型返回对应的错误页面
* 自定义一个全局异常处理类实现HandlerExceptionResovler接口。这样抛出的异常最后都会抛到自定义的类中，我们可以在这个自定义的类中，针对不同的异常进行处理
* 使用@ExceptionHandler和@ControllerAdvice注解进行处理，相对于全局异常处理

# 8 tomcat

* tomcat由一个Server，多个Service组成
* Service由一个Container和多个Connector组成
* Container中包含四个容器组件：Engine/Host/Context/Wrapper，属于父子关系
* Tomcat组件的生命周期是通过Lifecycle接口来控制的
* 主要由观察者模式和责任链模式

# 9 八大排序算法

排序关键是已经给了不会改变的数组给我了，不会向里面插入值了

## 9.1 简单选择排序

```java
public class SimpleSelectSort {
    
    public static void sort(int[] a) {
        doSort(a);
    }
    
    private static void doSort(int[] a) {
        for(int i=0,iMax=a.length-1; i<iMax; i++) {
            int min = i;
            
            for(int temp=i+1,tMax=a.length; temp<tMax; temp++) {
                if(a[temp]<a[min]) {
                    min = temp;
                }
            }
            
            if(i!=min) {
                swap(a,i,min);
            }
        }
    }
}
```

- 从给定的数组中每次选择一个最小的数和第一个数交换
- 每次都要有一个基准，即外围for
- 长度为N的数组，最多交换N-1次，即选择N-1个最小的，就可以排序完
- 所以外围for范围是，i=[0,N-1)
- 内部for范围是，temp=[i+1,N)
- 外围for中要定义一个min变量，标记最小下标
- 内部for中和a[min]比较，如果有更小的，替换min = temp
- 交换基准i和min的元素
- **当数组基本有序时，效率比较高**
- 相比简单交换排序，每次交换相邻两个元素，这个效率高多了



## 9.2 插入排序

```java
public class InsertSort {
    
    //直接插入
    public static void straightSort(int[] a) {
        doSort(a,1)
    }
    
    //希尔排序
    public static void shellSort(int[] a,int[] dks) {//dk是每次排序的步长，不同dk效率相差很大
        for(int i=0,iMax=dk.length; i<iMax; i++) {
            doSort(a,dks[i]);
        }
        //int[] dk = {5,3,1};dk最后一次步长一定要是1，一般就是a.length/2
    }
    
    private static void doSort(int[] a, int dk) {
        for(int i=dk,iMax=a.length; i<iMax; i++) {
            
            if(a[i]<a[i-dk]) {//只有当待排序的值，小于已排序的最大值时，才进入排序，否则a[i]直接成了现在的最大值，不用排序了
                
            	int temp = a[i];//保留基准值
                int j = i;//把i赋给j，是为了不改变i
                
                for(;(j>dk-1)&&(temp<a[j-dk]);j=j-dk) {
                    a[j] = a[j-dk];//不断的用前面的值，覆盖后面的值，可以看出覆盖的时候，数组中只有a[i]的值被覆盖没了，其他值都保留着，而且最后一个覆盖的值有双份
                    //所以之前我们保持了temp
                }
                //现在的j是之前的j-dk了
                a[j] = temp;//因为a[j]=a[j-dk]，又temp<a[j-dk]，所以a[j-dk]=temp
            }
        }
    }
}
```

- 主要思想是，把一段数据当成已排序的数组，每次取得是待排序的
- 将前一个数直接覆盖后一个数，直到待排序的数大于后一个数为止
- 最后将待排序的数，插入对应的位置
- 直接插入排序是覆盖，而选择排序是交换，但一次交换可能抵得过多次移动带来的顺序程度
- 所以希尔排序，调大移动步长，达到交换的优点（一次移动带来的顺序程度）
- 但不同dk效率相差很大



## 9.3 归并排序

```java
public class MergeSort {
    private static int[] tempArray;
    
    public static void sort(int[] a) {
        tempArray = new int[a.length];
        doSort(a,0,a.length-1);
    }
    
    private static void doSort(int[] a, int left, int right) {
        if(left>=right) return; //递归退出条件
        
        int center = (rigth+left)/2;//找出中间索引    
        doSort(a,left,center);// 对左边数组进行递归 
        doSort(a,center+1,rigth);// 对右边边数组进行递归 	
        
        merge(a,left,center,rigth);//对递归拆分的数组，进行递归合并
    }
    
    //这是左右两边数组，各自都已经排序完成了，只需要合并两个排序完成的数组
    private static void merge(int[] a, int left, int center, int rigth) {
        
        int second = center+1;//标记右边数组第一位
        int first = left;//标记左边数组第一位
        
        System.arraycopy(a,left,tempArray,left,rigth-left+1);//复制一个a
        
        for(int k=left;k<=right;k++){
			if(first>center) a[k] = tempArray[second++];//当第一个数组使用完后，直接使用第二个数组即可
			else if(second>right) a[k] = tempArray[first++];//当第二个数组使用完后，直接使用第一个数组即可
			else if(tempArray[second]>=tempArray[first]) a[k] = tempArray[first++];//第二个数组数值大于等于第一个，则使用第一个
			else a[k] = tempArray[second++];//否则使用第二个
		}
    }
}
```

- 关键是合并两个已排序的数组
- 有一个和a数组等大小的数组，用来保存a各个阶段的数据，每次都复制，可能会效率不高
- 用两个指针遍历tempArray数组，给a数组赋值



## 9.4 交换排序

```java
public class ExchangeSort {
    
    //快排
    pubilc static void quickSort(int[] a) {
        doSort(a,0,a.length-1);
    }
    
    private static void doSort(int[] a, int left, int rigth) {
        if(left>=right) return;//递归退出条件
        
        int middle = getMiddle(a,left,rigth);//成功排完一个数，并返回该数的下标
        doSort(a,low,middle-1);//递归排这个数左边的数组
        doSort(a,middle+1,high);//递归排这个数右边的数组
    }
    
    //鸡尾酒排序
    public static void shakerSort(int[] a) {
        int left = 0, rigth = a.length-1, shift = 0, i=0;
        
        while(left < rigth) {
            for(i=left; i< rigth; i++) {//因为i<high，所以high = shift -1;不行，要不然会少判断
                if(a[i]>a[i+1]) {
                    swap(a,i,i+1);
                    shift = i;//记录最后交换的位置，i=4,4、5换了，但以后都没换了
                }
            }
            
            rigth = shift;//right后面已经有序了！！
            
            for(i=rigth-1; i>=left; i--) {
                if(a[i]>a[i+1]) {
                    swap(a,i,i+1);
                    shift = i+1;
                }
            }
            
            left = shift;//left前面已经有序了
        }
        
    }
    
    private static void getMiddle(int[] a, int left, int rigth) {
        int temp = a[left];//基准元素
        
        while(left<rigth) {
            
            while(left<rigth&&a[rigth]>temp) rigth--;//取得第一个小于temp的最右边下标
            a[left] = a[high];//将此下标的值覆盖掉基准下标
            while(left<rigth&&a[left]<=temp) left++;首先现在第一个值肯定小于temp，再取得第一个大于基准元素的下标
            a[right] = a[left];//将此下标的值覆盖掉，第一个小于temp值的下标          		   //到这里时，a[rigth]>temp，所以又开始rigth--!!
            //现在a[left]>temp，但是下次循环后肯定不是了，除非退出循环
        }
        //到这里后，left===rigth
        a[left] = temp;
        return left;
    }
    
    //选择最大几个数
    private static void doSort0() {
        if(right <= left + dk) {
            insertSort(a,low,high);//简单排序
            return;
        }

        int middle = getMiddle(a, low, high);
        doSort0(a, low, middle-1,dk);
        doSort0(a, middle+1, high,dk);
    }
    
    //三向切分
    private static int[] getMiddle0() {
        int lt = left, i=left+1, gt = right;
        int temp = a[left];//基准元素
        
        while(i<=gt) {//从基准元素下一个开始比较
            int flag = a[i] - temp;
            if(flag<0) swap(a,lt++,i++);//基准元素大，则交换
            else if(flag>0) swap(a,i,gt--);//基准元素小，则和最后一个交换，关键是i，不变，下一次比较的就是最后一个元素和temp了
            else i++;
        }
        //至此就简单的把小于temp的都移到了左边，大于temp的都移到了右边，中间的就是等于temp的
        return new int[]{lt-1,gt+1};
    }
    
    /**
	 * 三切面法快排，适用于很多重复元素的数组
	 * @param a
	 */
	public static void sort1(int[] a){
		doSort1(a, 0, a.length-1);
	}
    
    private static void doSort1(int[] a,int low,int high){
        if(low>=high) return;

        int[] middles = getMiddle0(a, low, high);
        doSort1(a, low, middles[0]);
        doSort1(a, middles[1], high);
    }
}
```

- 关键是shift的位置，每次都是选择范围更小的i
- 比如往后排时，shift = i，往前排时，shift = i+1
- 鸡尾酒排序还是相邻元素比较交换，所以效率不会太高
- 从交换规则看，快排是最左和最右交换的，所以效率很高
- 且快排没有像归并一样用到额外内存



## 9.5 堆排序

```java
public class HeapSort {
    
    public static void sort(int[] a) {
        int aMax = a.length-1;
        //先通过下沉操作建堆，不是排序，因为aMax/2往下的节点的子节点都只有一个了，所以可以用sink来进行平衡，所以可以从这个节点开始建堆
        for(int k=aMax/2; k>=1 ;K--) {
            sink(a,k,aMax);
        }
        
        while(aMax>1) {//因为只有大于1，aMax--才大于0，堆是不能考虑0下标的
            swap(a,1,aMax--);//将最大值放到数组最后，并使堆大小减1,每一个循环取得一个最大值
            sink(a,1,aMax);//建堆的时候，已经不考虑最大值了，且除了第一层，所有的已经平衡了，所以可以用sin操作，重新平衡第一层
        }
    }
    
    //sink操作如果深度超过2层，则只会有一边达到平衡，所以要保证下一层之后的节点已经平衡了
    private static void sink(int[] a, int k, int aMax) {
        while(2*k<=aMax) {
            int j = 2*k;
            if(j<N&&a[j]<a[j+1]) j++;//取得值是最大的节点下标
            if(a[k]>=a[j]) return;//如果比最大的还大，说明已经平衡了
            swap(a,k,j);//否则交换最大节点值
            k = j;//在以该节点往下平衡
        }
    }
}
```

- 建堆过程，不需要左节点大于右节点，只要父节点最大就行
- 堆不能考虑0下标，要从1开始



[^1]: 动态链接与静态链接，有一部分符号引用在解析阶段就转换成直接引用，如：static修饰的方法，final修饰的方法，private修饰的方法，构造器等，这些都不能被重写。而其它的方法，可能会被子类重写，所有在解析阶段无法转换成直接引用，只能在运行器确定是哪个类,动态链接就是将常量池中的符号引用在运行期间转化为直接引用。编译/连接(验证/准备/解析)/初始化
[^2]: 存储基本数据类型，指令地址和对象引用，局部变量所需的内存空间再编译器确定
[^3]: 存储运算的结果以及运算的操作数，通过入栈和出栈的方式实现

