package concurrent.blockQueue;

import java.util.concurrent.*;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 21:42 2018/8/30
 * @Modified By:
 */
public class ArrayBlockingQueueTest {

    public static void main(String[] args) {

        //ExecutorService executor = new ThreadPoolExecutor(2,3,1,TimeUnit.HOURS,new ArrayBlockingQueue(3));
        ExecutorService executor = Executors.newFixedThreadPool(5);

        ArrayBlockingQueue queue = new ArrayBlockingQueue(3);
        LinkedBlockingQueue linkQ = new LinkedBlockingQueue(3);
        try {
            queue.put(1);
            linkQ.put(2);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            queue.take();
            linkQ.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
