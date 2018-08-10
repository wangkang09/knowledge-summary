## Java 内存模型

### 主内存与线程工作内存

1. 主内存：所有线程共享，存放共享变量
2. 线程工作内存：每一个线程有一块工作内存区，存放着共享变量的拷贝。当线程执行时，它在自己的工作内存中操作这些变量，其他线程不可见

### 工作内存与主内存交互

1. 一个线程可以执行的操作有使用（use）、赋值（assign）、装载（load）、存储（store）、锁定（lock）、解锁（unlock）。主内存可以执行的操作有读（read）、写（write）、锁定（lock）、解锁（unlock），每一个操作都是原子的
2. 主内存和工作内存间的数据传送并不满足原子性：需要两个步骤
   1. 主内存复制到工作内存 
      1. 由主内存执行的读（read）操作
      2. 由工作内存执行的装载（load）操作
   2. 工作内存拷贝到主内存 
      1. 工作内存执行存储（store）操作
      2. 主内存执行写（write）操作
3. 由于主内存和工作内存间传输数据需要时间，且该时间不确定。因此从另一个线程的角度看，可能会存在值不同步的现象
   1. 两个线程把不同的值或者对象引用存储到同一个主内存中的共享变量中，该变量那么是这个线程的，那么是那个线程
   2. 但是 long、double 这种类型的变量，有可能会是两个线程组合而成
4. 使用 Volatile 关键字迫使所有线程均读写主内存中对应的变量，从而使得 Volatile 变量在多线程间可见

### Volatile 使用方法

1. 帮助理解的例子

```
public class VolatileUse {

    private volatile boolean isExit;

    public void tryExit() {
        // 成立就退出
        //第一步从主存中取到isExit，并将!isExit保存到寄存器中
        //第二步再从主存中取到isExit，并与寄存器中的值比较！
        //所以会进入if语句！！
        if (isExit == !isExit) {
            System.exit(0);
        }
    }

    public void swapValue() {
        isExit = !isExit;
    }

    public static void main(String[] args) throws InterruptedException {
        final VolatileUse volatileUse = new VolatileUse();
        Thread mainThread = new Thread() {
            @Override
            public void run() {
                System.out.println("Main Thread start");
                while (true) {
                    // 不停的尝试时候可以退出
                    volatileUse.tryExit();
                }
            }
        };

        mainThread.setName("Main Thread");
        mainThread.start();

        Thread swapThread = new Thread() {
            @Override
            public void run() {
                System.out.println("Swap Thread start");
                while (true) {
                    // 不停的修改 isExit 的值
                    volatileUse.swapValue();
                }
            }
        };

        swapThread.setName("Swap Thread");
        swapThread.start();
        Thread.sleep(10000L);
    }
}
```

1. 常用方法:

- 线程因为 stop 变量改变而退出
- `volatile` 的意义在于在主线程中停止 InThread 线程，这 InThread 会立即发现 stop 状态的改变，从而停止

```
public class VolatileThread {

    public static void main(String[] args) throws InterruptedException {
        InThread inThread = new InThread();
        new Thread(inThread, "Volatile Thread ").start();
        Thread.sleep(1000);
        inThread.stop();
        Thread.sleep(1000);
    }


    static class InThread implements Runnable {

        /**
         * 确保 stop 变量在多线程中可见
         */
        private volatile boolean stop = false;

        /**
         * 在其他线程中调用，停止本线程
         */
        public void stop() {
            this.stop = true;
        }

        @Override
        public void run() {
            int i = 0;
            while (!stop) {
                i++;
            }
            System.out.println("Stop Thread " + i);
        }
    }
}
```

1. 结论：
   当多个线程同时访问一个共享变量时，可以使用 `volatile`，而当访问的变量已在 synchronized 代码块中时，不必使用