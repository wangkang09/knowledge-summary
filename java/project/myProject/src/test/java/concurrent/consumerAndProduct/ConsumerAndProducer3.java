package concurrent.consumerAndProduct;

import java.util.Random;
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
public class ConsumerAndProducer3 {
    private ReentrantLock producLock = new ReentrantLock();
    private ReentrantLock consumerLock = new ReentrantLock();
    private volatile AtomicInteger count = new AtomicInteger(0);

    private Condition notFull = producLock.newCondition();
    private Condition notEmpty = consumerLock.newCondition();

    Object[] target =  new Object[10];
    int takeIndex;
    int putIndex;
    Random r = new Random();


    public static void main(String[] args) {
        ExecutorService productPool = new ThreadPoolExecutor(3, 5, 30, TimeUnit.SECONDS, new ArrayBlockingQueue<>(30));
        ExecutorService consumerPool = new ThreadPoolExecutor(3, 5, 30, TimeUnit.SECONDS, new ArrayBlockingQueue<>(30));

        System.out.println("start");

        ConsumerAndProducer3 c2 = new ConsumerAndProducer3();

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
            System.out.println("start consume!");
            int c = -1;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            consumerLock.lock();
            try {
                while (count.get() == 0) {
                    System.out.println("array is empty, can not consume!");
                    try {
                        notEmpty.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //消费
                int cc = (int)target[takeIndex];
                System.out.println("consume " + cc);
                target[takeIndex] = null;
                if(++takeIndex==target.length) takeIndex = 0;

                c = count.getAndDecrement();//获取旧的count值，并递减

                if (c > 1) {//当旧的count值大于1，则获取一次后肯定大于0，所以可以唤醒消费者
                    notEmpty.signal();
                }

            } catch (Exception e){
                e.printStackTrace();
            }finally {
                consumerLock.unlock();
            }

            if (c == target.length) {//当原始count是10，生产者才可能使阻塞状态，所以才需要唤醒
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
                System.out.println("start produce!");
                Thread.sleep(1000);

                while (count.get() == target.length) {
                    System.out.println("array is full, can not produce!");
                    notFull.await();
                }
               //生产
                int ou = r.nextInt(300);
                target[putIndex] = ou;
                System.out.println("produce " + ou);
                if(++putIndex==target.length) putIndex = 0;
                c = count.getAndIncrement();
                if (c + 1 < target.length) {//要加1，因为当前线程加了一个元素了
                    notFull.signal();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                producLock.unlock();
            }

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



