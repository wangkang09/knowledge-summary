## 线程范围内的数据共享

### 表述

1. 是指一个线程中变量数据被多个对象（实例）方法访问，在多个对象间共享
2. 线程变量数据在线程间隔离，在方法或类间共享
3. 不是指多线程之间共享变量数据的问题，每个线程中的数据是没有关系的，不存在数据同步的问题

### 简单实现

1. 验证单个线程在访问不同对象实例时是否共享数据，对象实例(A / B) 在同一个线程（InThread）范围内的数据是一致的
2. 可以使用 `Map<Thread, Object>` 实现
3. 代码如下：

```java
public class ThreadScopeShareData {

    /**
     * 传递数据的容器，map的key为线程对象
     */
    private static Map<Thread, Integer> threadData = new HashMap<Thread, Integer>();

    /**
     * 有几个线程就会有几份数据：
     * 多个相对访问同一个线程的数据是一样的
     * 不同线程的数据是不同的
     *
     * @param args
     */
    public static void main(String[] args) {
        new Thread(new InThread(), "InThread0: ").start();
        new Thread(new InThread(), "InThread1: ").start();

    }


    static class InThread implements Runnable {

        @Override
        public void run() {
            int data = new Random().nextInt();
            //把数据放入MAP
            threadData.put(Thread.currentThread(), data);

            System.out.println(Thread.currentThread().getName() + " has put data :" + data);
            // 线程去访问 A
            new ObjectA().get();
            // 线程去访问 B
            new ObjectB().get();
        }
    }


    /**
     * 模块A
     */
    static class ObjectA {
        public void get() {
            //从线程获取数据
            int data = threadData.get(Thread.currentThread());
            System.out.println("ObjectA from " + Thread.currentThread().getName() + " get data :" + data);
        }
    }

    /**
     * 模块B
     */
    static class ObjectB {
        public void get() {
            //从线程获取数据
            int data = threadData.get(Thread.currentThread());
            System.out.println("ObjectB from " + Thread.currentThread().getName() + " get data :" + data);
        }
    }
}
```

### ThreadLocal 实现

1. 简单数据变量共享

   ```java
   public class ThreadLocalShareData {
       /**
        * 传递数据的容器，map的key为线程对象
        * 使用 ThreadLocal 代替 Map
        */
       private static ThreadLocal<Integer> threadLocal = new ThreadLocal<Integer>();
   
       /**
        * 有几个线程就会有几份数据：
        * 多个相对访问同一个线程的数据是一样的
        * 不同线程的数据是不同的
        *
        * @param args
        */
       public static void main(String[] args) {
           new Thread(new InThread(), "InThread0: ").start();
           new Thread(new InThread(), "InThread1: ").start();
       }
       
       static class InThread implements Runnable {
           @Override
           public void run() {
               int data = new Random().nextInt();
               //把数据放入MAP
               threadLocal.set(data);
   
               System.out.println(Thread.currentThread().getName() + " has put data :" + data);
   
               // 说ThreadLocal使得各线程能够保持各自独立的一个对象，并不是通过ThreadLocal.set()来实现的，而是通过每个线程中的new 对象 的操作来创建的对象
               new ObjectA().get();
               new ObjectB().get();
               // 显示调用 ThreadLocalMap.remove方法清除线程共享变量
               threadLocal.remove();
           }
       }
   
       /**
        * 模块A
        */
       static class ObjectA {
           public void get() {
               //从线程获取数据
               int data = threadLocal.get();
               System.out.println("ObjectA from " + Thread.currentThread().getName() + " get data :" + data);
           }
       }
   
       /**
        * 模块B
        */
       static class ObjectB {
           public void get() {
               //从线程获取数据
               int data = threadLocal.get();
               System.out.println("ObjectB from " + Thread.currentThread().getName() + " get data :" + data);
           }
       }
   }
   ```

2. 线程范围内的复杂对象数据共享

```java
public class ThreadLocalObjectData {

    /**
     * @param args
     */
    public static void main(String[] args) {
        new Thread(new FirstThread()).start();
        new Thread(new FirstThread()).start();
    }

    static class A {
        public void get() {
            MyNewData myNewData = MyNewData.getThreadInstance();
            System.out.println("A from " + Thread.currentThread().getName() + " get data :" + myNewData);
        }
    }

    static class B {
        public void get() {
            MyNewData myNewData = MyNewData.getThreadInstance();
            System.out.println("B from " + Thread.currentThread().getName() + " get data :" + myNewData);
        }
    }

    /**
     * 线程一
     */
    static class FirstThread implements Runnable {

        @Override
        public void run() {
            int data = new Random().nextInt();
            MyNewData myNewData = MyNewData.getThreadInstance();
            myNewData.setAge(data);
            myNewData.setName("name-" + data);

            new A().get();
            new B().get();

            myNewData.remove();
        }
    }


    /**
     * ClassName: CustomThread
     * Description: 单例数据 交给数据类的对象
     *
     * <p>
     * Date: 2014-6-29 8:00 【需求编号】
     *
     * @author Sam Sho
     * @version V1.0.0
     */
    static class MyNewData {

        private MyNewData() {
        }

        /**
         * 改造单例，可以不加互斥
         */
        private static ThreadLocal<MyNewData> map = new ThreadLocal<>();

        public static MyNewData getThreadInstance() {
            // ThreadLocal先拿，没有就创建
            MyNewData instance = map.get();
            if (instance == null) {
                instance = new MyNewData();
                // 创建完，就放入ThreadLocal
                map.set(instance);
            }
            return instance;
        }

        public void remove() {
            map.remove();
        }


        private String name;
        private int age;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        @Override
        public String toString() {
            return "MyData [age=" + age + ", name=" + name + "]";
        }

    }
}
```

## ThreadLocal 源码分析

### 源码

#### 主要的类

1. ThreadLocal
2. ThreadLocalMap：ThreadLocal内部的容器类，真正保存数据的地方
3. Thread：内部维护 ThreadLocalMap 对象。Thread - ThreadLocalMap<ThreadLocal, Value> 这样的关系
4. 可以看出，每个线程会有自己的 `ThreadLocalMap` 对象保存着数据，不会被其他线程访问或者共享

#### 主要方法

1. 设置数据的方法

```java
public void set(T value) {
    Thread t = Thread.currentThread();
    ThreadLocalMap map = getMap(t);
    if (map != null)
        map.set(this, value); // key 为 this（ThreadLocal），不是thread
    else
        createMap(t, value);
}
```

1. `createMap` 创建一个 `ThreadLocalMap` 对象

- 内部保存是 key 是 `ThreadLocal`, value 是我们保存的数据
- `ThreadLocalMap` 被维护在 `Thread` 中，`Thread.currentThread()`

```java
void createMap(Thread t, T firstValue) {
    t.threadLocals = new ThreadLocalMap(this, firstValue); // `ThreadLocalMap` 被维护在 `Thread` 中，`Thread.currentThread()`
}
```

```java
ThreadLocalMap(ThreadLocal<?> firstKey, Object firstValue) {
    table = new Entry[INITIAL_CAPACITY];
    int i = firstKey.threadLocalHashCode & (INITIAL_CAPACITY - 1);
    table[i] = new Entry(firstKey, firstValue);
    size = 1;
    setThreshold(INITIAL_CAPACITY);
}
```

1. 获取数据的方法

```java
public T get() {
    Thread t = Thread.currentThread();
    ThreadLocalMap map = getMap(t);
    if (map != null) {
        ThreadLocalMap.Entry e = map.getEntry(this);
        if (e != null) {
            @SuppressWarnings("unchecked")
            T result = (T)e.value;
            return result;
        }
    }
    return setInitialValue();
}
```

### 注意

1. 通常被 `private static` 修饰
2. 最后显式调用 `ThreadLocal.remove()` 清除数据，防止内存泄露

## ThreadLocal 应用场景

### Spring，Hibernate等框架中对于多线程的处理

1. Spring 中对于 `HttpServletRequest` 整个请求域管理，这边涉及有无状态的Bean，见Spring相关博文

```java
//①使用ThreadLocal保存Connection变量  
private static ThreadLocal<Connection> connThreadLocal = new ThreadLocal<Connection>();  
public static Connection getConnection(){  
           
//②如果connThreadLocal没有本线程对应的Connection创建一个新的Connection，  
//并将其保存到线程本地变量中。  
if (connThreadLocal.get() == null) {  
            Connection conn = ConnectionManager.getConnection();  
            connThreadLocal.set(conn);  
              return conn;  
        }else{  
              //③直接返回线程本地变量  
            return connThreadLocal.get();  
        }  
    }  
    public void addTopic() {  
  
        //④从 ThreadLocal 中获取线程对应的  
         Statement stat = getConnection().createStatement();  
    }  
}
```

1. hibernate中典型的ThreadLocal的应用

```java
private static final ThreadLocal threadSession = new ThreadLocal();

public static Session getSession() throws InfrastructureException {
    Session s = (Session) threadSession.get();
    try {
        if (s == null) {
            s = getSessionFactory().openSession();
            threadSession.set(s);
        }
    } catch (HibernateException ex) {
        throw new InfrastructureException(ex);
    }
    return s;
}
```

### 开源的 Mybatis 分页类库 [Mybatis-PageHelper](https://github.com/pagehelper/Mybatis-PageHelper/blob/master/README_zh.md)

1. PageHelper 方法使用了静态的 ThreadLocal 参数，分页参数和线程是绑定的
2. PageHelper 在 finally 代码段中自动清除了 ThreadLocal 存储的对象

```java
public abstract class PageMethod { // PageHelper 是其子类
    protected static final ThreadLocal<Page> LOCAL_PAGE = new ThreadLocal<Page>();
    
        /**
     * 开始分页
     *
     * @param pageNum      页码
     * @param pageSize     每页显示数量
     * @param count        是否进行count查询
     * @param reasonable   分页合理化,null时用默认配置
     * @param pageSizeZero true且pageSize=0时返回全部结果，false时分页,null时用默认配置
     */
    public static <E> Page<E> startPage(int pageNum, int pageSize, boolean count, Boolean reasonable, Boolean pageSizeZero) {
        Page<E> page = new Page<E>(pageNum, pageSize, count);
        page.setReasonable(reasonable);
        page.setPageSizeZero(pageSizeZero);
        //当已经执行过orderBy的时候
        Page<E> oldPage = getLocalPage(); // 获取 ThreadLocal
        if (oldPage != null && oldPage.isOrderByOnly()) {
            page.setOrderBy(oldPage.getOrderBy());
        }
        setLocalPage(page); // 保存 ThreadLocal 
        return page;
    }
    
    public static <T> Page<T> getLocalPage() {
        return LOCAL_PAGE.get();
    }
    
    protected static void setLocalPage(Page page) {
        LOCAL_PAGE.set(page);
    }
}
```

