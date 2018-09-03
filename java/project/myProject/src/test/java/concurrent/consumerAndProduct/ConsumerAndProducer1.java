package concurrent.consumerAndProduct;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 21:51 2018/8/31
 * @Modified By:
 */
public class ConsumerAndProducer1 {


    public static void main(String[] args) {
        ArrayBlockingQueue<Integer> queue = new ArrayBlockingQueue<>(10);
        ExecutorService productPool = new ThreadPoolExecutor(3,5,30,TimeUnit.SECONDS,new ArrayBlockingQueue<>(30));
        ExecutorService consumerPool = new ThreadPoolExecutor(3,5,30,TimeUnit.SECONDS,new ArrayBlockingQueue<>(30));

        System.out.println("start");

        for (int i = 0; i < 20 ; i++) {
            productPool.submit(new ProductThread(i,queue));
            consumerPool.submit(new ConsumerThread(queue));
        }

        productPool.shutdown();
        consumerPool.shutdown();

        try {
            productPool.awaitTermination(1,TimeUnit.DAYS);
            consumerPool.awaitTermination(1,TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

class ProductThread implements Runnable {

    private int taskNum;
    private ArrayBlockingQueue queue;

    public ProductThread(int taskNum, ArrayBlockingQueue queue) {
        this.taskNum = taskNum;
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(5000);
            System.out.println("生产开始");
            queue.add(taskNum);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class ConsumerThread implements Runnable {
    private ArrayBlockingQueue queue;

    public ConsumerThread(ArrayBlockingQueue queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        System.out.println("准备消费");
        int taskNum;
        try {
            taskNum = (int) queue.take();
            System.out.println("消费了"+taskNum);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}