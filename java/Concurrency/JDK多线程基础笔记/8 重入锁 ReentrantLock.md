## 重入锁 ReentrantLock

### 重入锁`ReentrantLock` 和 内部锁`synchronized`

1. 重入锁`ReentrantLock` 相对于 内部锁`synchronized`，功能更加强大，使用较为复杂
2. 重入锁`ReentrantLock` 提供可中断、可定时、公平锁、非公平锁（通过构造方法实现，性能更好）等功能，其实通过内部锁`synchronized`实现的锁也不是绝对公平
3. 内部锁`synchronized` 自动释放，重入锁`ReentrantLock` 必须手动释放锁

```java
public class ReentrantLockTest {

    void test1() {
        DataObject data = new DataObject();
        new Thread(new InThreadTask(data), "thread0").start();
        new Thread(new InThreadTask(data), "thread1").start();
    }

    /**
     * 任务线程
     */
    class InThreadTask implements Runnable {

        private DataObject data;

        public InThreadTask(DataObject data) {
            this.data = data;
        }

        @Override
        public void run() {
            data.get("abcdefghijklmnopqrstuvwxyz");
        }
    }

    /**
     * 业务对象
     */
    class DataObject {

        /**
         * 定义锁对象
         */
        Lock lock = new ReentrantLock();

        /**
         * Lock实现
         *
         * @param name
         */
        void get(String name) {

            int length = name.length();
            try {
                // 获得锁，如果锁已经占用，则等待
                lock.lock();
                for (int i = 0; i < length; i++) {
                    System.out.print(name.charAt(i));
                }
                System.out.println();
            } finally {
                // 需要手动解锁，一定要注意
                lock.unlock();
            }
        }
    }
}
```

### 重入锁 `ReentrantLock` 方法详解

示例主线程代码

```java
static final Lock lock = new ReentrantLock();

public static void main(String[] args) throws InterruptedException {
    new Thread(createTask(), "thread_1 ").start();
    Thread thread_2 = new Thread(createTask(), "thread_2 ");
    thread_2.start();

    Thread.sleep(600);
    // 中断
    // 单独调用interrupt()方法不能中断正在运行过程中的线程，只能中断阻塞过程中的线程
    thread_2.interrupt();
}
```

#### 获取锁的方法

1. `lock`：获得锁，如果锁已经占用，则等待，等待的过程中不会响应中断

```java
public static Runnable createTask() {
        return new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        lock.lock(); //获取锁，会阻塞等待，等待的过程中不会响应中断
                        try {
                            System.out.println("locked by " + Thread.currentThread().getName());
                            Thread.sleep(1000L);
                        } finally {
                            // 需要手动解锁，一定要注意
                            lock.unlock();
                            System.out.println("unlocked by " + Thread.currentThread().getName());
                        }

                        // 正常一个线程在获得锁、等待1s、释放锁，然后会break循环
                        break;
                    } catch (InterruptedException e) {
                        System.out.println(Thread.currentThread().getName() + "is Interrupted");
                    }

                }
            }
        };
    }
    
输出：

locked by thread_1
unlocked by thread_1
locked by thread_2          // 等待获得锁，一直等待。等待的过程中不会响应线程中断
unlocked by thread_2        
thread_2 is Interrupted     // 释放锁后,原本应该是break，但是响应了中断
locked by thread_2
unlocked by thread_2
```

1. `lockInterruptibly`：获得锁，与`lock` 最大的不同在于锁等待的过程中优先会响应中断

```java
public static Runnable createTask() {
        return new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        lock.lockInterruptibly(); // 等待获得锁的过程中，响应了中断

                        try {
                            System.out.println("locked by " + Thread.currentThread().getName());
                            Thread.sleep(1000L);
                        } finally {
                            // 需要手动解锁，一定要注意
                            lock.unlock();
                            System.out.println("unlocked by " + Thread.currentThread().getName());
                        }

                        // 正常一个线程在获得锁、等待1s、释放锁，然后会break循环
                        break;
                    } catch (InterruptedException e) {
                        System.out.println(Thread.currentThread().getName() + "is Interrupted");
                    }

                }
            }
        };
    }
    
lock.lockInterruptibly() 输出：

locked by thread_1
thread_2 is Interrupted     // 等待获得锁的过程中，响应了中断
unlocked by thread_1
locked by thread_2
unlocked by thread_2
```

1. `tryLock`：尝试获得锁，如果成功，返回true；失败返回false；该方法不会等待，立即返回

```java
public static Runnable createTask() {
        return new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        if (lock.tryLock(500, TimeUnit.MILLISECONDS)) {
                            try {
                                System.out.println("locked by " + Thread.currentThread().getName());
                                Thread.sleep(1000L);
                            } finally {
                                // 需要手动解锁，一定要注意
                                lock.unlock();
                                System.out.println("unlocked by " + Thread.currentThread().getName());
                            }

                            // 正常一个线程在获得锁、等待1s、释放锁，然后会break循环
                            break;

                            // else 这段代码 lock.lock(); lock.lockInterruptibly(); 不需要
                        } else {
                            System.out.println("unable to lock " + Thread.currentThread().getName());
                        }


                    } catch (InterruptedException e) {
                        System.out.println(Thread.currentThread().getName() + "is Interrupted");
                    }

                }
            }
        };
    }
    
lock.tryLock(500, TimeUnit.MILLISECONDS) 输出：

locked by thread_1
unable to lock thread_2 // 在规定时间内等待锁
thread_2 is Interrupted // 中断
unlocked by thread_1
locked by thread_2 // 中断结束后，继续尝试获得锁
unlocked by thread_2
```

1. `tryLock(long timeout, TimeUnit unit)`：在给定时间内尝试获得锁，如果成功，返回true；失败返回false

```java
public static Runnable createTask() {
        return new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        if (lock.tryLock()) {
                            try {
                                System.out.println("locked by " + Thread.currentThread().getName());
                                Thread.sleep(1000L);
                            } finally {
                                // 需要手动解锁，一定要注意
                                lock.unlock();
                                System.out.println("unlocked by " + Thread.currentThread().getName());
                            }

                            // 正常一个线程在获得锁、等待1s、释放锁，然后会break循环
                            break;

                            // else 这段代码 lock.lock(); lock.lockInterruptibly(); 不需要
                        } else {
                            System.out.println("unable to lock " + Thread.currentThread().getName());
                        }
                    } catch (InterruptedException e) {
                        System.out.println(Thread.currentThread().getName() + "is Interrupted");
                    }

                }
            }
        };
    }
    
lock.tryLock() 输出
locked by thread_1
unable to lock thread_2
.......................     // 一堆获取不到锁的信息, 因为锁被thread_1 占着。tryLock 不会等待，立即返回
unable to lock thread_2
unlocked by thread_1        // 解锁后，另一个线程 thread_2 才能得到锁
locked by thread_2          // 得到锁
unlocked by thread_2
thread_2 is Interrupted     // 在释放锁后被中断
locked by thread_2          // 由于被中断，跳过了break，继续请求锁
unlocked by thread_2
```

1. `unlock()`：释放锁，需要手动调用。使用 Lock 必须 在try{}catch{} 块中进行，然后在 `finally`中释放锁

#### 其他方法

1. `newCondition`：见 `Condition` 博文，下同
2. `isFair`：是否公平，如果此锁的公平设置为 true，则返回 true
3. `isLocked`：是否已经获取锁
4. `getHoldCount`：查询当前线程保持锁定的个数，也就是调用lock()方法的次数
5. `getQueueLength`：返回正等待获取此锁定线程数，如果一共开启了5个线程，一个线程执行了await()方法，那么在调用此方法是，返回4，说明此时正有4个线程在等待锁的释放
6. `getWaitQueueLength`：返回同一个Condition类的等待线程数。如果有同时开启了5个线程，都调用了await()方法，并且它们是用的同一个Condition类，那么在调用该方法是，返回值为5