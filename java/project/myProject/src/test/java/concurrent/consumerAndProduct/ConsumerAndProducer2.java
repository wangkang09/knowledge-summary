package concurrent.consumerAndProduct;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 21:51 2018/8/31
 * @Modified By:
 */
public class ConsumerAndProducer2 {
    private ReentrantLock producLock = new ReentrantLock();
    private ReentrantLock consumerLock = new ReentrantLock();
    private volatile AtomicInteger count = new AtomicInteger(0);

    private Condition notFull = producLock.newCondition();
    private Condition notEmpty = consumerLock.newCondition();

    Queue<Integer> queue = new PriorityQueue<>();

    public static void main(String[] args) {
        ExecutorService productPool = new ThreadPoolExecutor(3, 5, 30, TimeUnit.SECONDS, new ArrayBlockingQueue<>(30));
        ExecutorService consumerPool = new ThreadPoolExecutor(3, 5, 30, TimeUnit.SECONDS, new ArrayBlockingQueue<>(30));

        System.out.println("start");

        ConsumerAndProducer2 c2 = new ConsumerAndProducer2();

        ConsumerThread2 consumerThread2 = c2.new ConsumerThread2();
        ProductThread2 productThread2 = c2.new ProductThread2();

        for (int i = 0; i < 20; i++) {
            productPool.submit(consumerThread2);
            consumerPool.submit(productThread2);
        }

        productPool.shutdown();
        consumerPool.shutdown();

        try {
            productPool.awaitTermination(1, TimeUnit.DAYS);
            consumerPool.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    class ConsumerThread2 implements Runnable {

        @Override
        public void run() {
            System.out.println("准备消费");
            int taskNum;
            int c = -1;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            consumerLock.lock();
            try {
                while (count.get() == 0) {
                    System.out.println("队列长度：" + queue.size() + " 不能消费了！");
                    try {
                        notEmpty.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    taskNum = (int) queue.poll();
                    System.out.println("消费了" + taskNum);

                }catch (Exception e){
                    e.printStackTrace();
                }

                c = count.getAndDecrement();

                if (c > 1) {
                    notEmpty.signal();
                }

            } catch (Exception e){
                e.printStackTrace();
            }finally {
                consumerLock.unlock();
            }

            if (c == 4) {//当原始count是4，生产者才可能使阻塞状态，所以才需要唤醒
                producLock.lock();
                try {
                    notFull.signal();
                } finally {
                    producLock.unlock();
                }
            }
        }
    }

    class ProductThread2 implements Runnable {


        @Override
        public void run() {

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int c = -1;
            producLock.lock();
            try {
                //Thread.sleep(10000);
                System.out.println("生产开始");
                Thread.sleep(1000);
                System.out.println("到这里");

                System.out.println("到这里啦啦啦");
                while (count.get() == 4) {
                    System.out.println("队列长度：" + queue.size() + " 不能生产了！");
                    notFull.await();
                }
                System.out.println("到这里2");
                queue.offer(1);
                System.out.println("队列长度：" + queue.size());

                c = count.getAndIncrement();
                if (c + 1 < 4) {//要加1，因为当前线程加了一个元素了
                    notFull.signal();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                producLock.unlock();
            }

            System.out.println("dsgga");
            if (c == 0) {
                consumerLock.lock();
                try {
                    notEmpty.signal();
                } finally {
                    consumerLock.unlock();
                }
            }
        }

    }

}



