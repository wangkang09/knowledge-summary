## 读写锁 ReadWriteLock

### 简介

1. 读写分离锁是减少锁粒度的一种特殊情况，是对系统功能点的分割，其分割了读与写两种操作
2. 适合场景：读多写少的场合。交互逻辑如下：

- 读锁之间是相容的，读锁在读读操作是不需要相互等待，提升多线程读数据的性能
- 写锁的占用是独占的

|      | 读锁     | 写锁     |
| ---- | -------- | -------- |
| 读锁 | 可访问   | 不可访问 |
| 写锁 | 不可访问 | 不可访问 |

### 简单实现

```java
public class ReadWriteLockTest {


    public static void main(String[] args) {

        Queue tQueue = new Queue();
        new Thread(new ReadThread(tQueue), "read_1").start();
        new Thread(new ReadThread(tQueue), "read_2").start();

        new Thread(new WriteThread(tQueue), "write_1").start();
        new Thread(new WriteThread(tQueue), "write_2").start();
    }


    /**
     * 读的线程
     */
    static class ReadThread implements Runnable {

        private Queue tQueue;

        public ReadThread(Queue tQueue) {
            this.tQueue = tQueue;
        }

        @Override
        public void run() {
            for (int i = 0; i < 5; i++) {
                tQueue.read();
            }

        }
    }

    /**
     * 写的线程
     */
    static class WriteThread implements Runnable {

        private Queue tQueue;

        public WriteThread(Queue tQueue) {
            this.tQueue = tQueue;
        }

        @Override
        public void run() {
            for (int i = 3; i < 7; i++) {
                tQueue.write("data_ " + i);
            }

        }
    }

    /**
     * @author SAM-SHO
     * @title 具体业务类
     * @description
     * @Date 2014-8-23
     */
    static class Queue {
        /**
         * 共享数据，只能有一个线程能写该数据，但可以多个线程同时读数据
         */
        private Object data = null;

        /**
         * 使用读写锁
         */
        ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

        /**
         * 读数据的方法
         */
        public void read() {

            readWriteLock.readLock().lock();//上锁
            try {

                System.out.println("开始读数据....." + Thread.currentThread().getName());
                Thread.sleep((long) (Math.random() * 1000));
                System.out.println(Thread.currentThread().getName() + "已经读到数据： " + data);

            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                readWriteLock.readLock().unlock();//解锁
            }

        }

        /**
         * 写数据的方法
         *
         * @param tData
         */
        public void write(Object tData) {

            readWriteLock.writeLock().lock();//上锁

            try {
                System.out.println("准备写数据....." + Thread.currentThread().getName());
                Thread.sleep((long) (Math.random() * 1000));
                this.data = tData;
                System.out.println(Thread.currentThread().getName() + "已经写完数据： " + data);

            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                readWriteLock.writeLock().unlock();//解锁
            }

        }

    }

}
```