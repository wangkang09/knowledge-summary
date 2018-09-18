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
        LinkedBlockingQueue linkQ = new LinkedBlockingQueue();
        PriorityBlockingQueue pri = new PriorityBlockingQueue(2);

        queue.size();
        queue.add(1);
        queue.peek();
        queue.iterator();
        queue.contains(1);
        queue.clear();;
        queue.remove(1);
        queue.peek();


        pri.put(1);
        try {
            queue.take();
            queue.put(1);
            pri.take();
            pri.put(1);
            pri.add(1);
            pri.clear();
            pri.contains(1);
            pri.offer(1);
            pri.iterator();
            pri.peek();
            pri.poll();
            pri.remove(1);
            pri.size();



            linkQ.put(1);
            linkQ.take();
            linkQ.clear();
            linkQ.contains(1);
            linkQ.offer(1);
            linkQ.offer(1);
            linkQ.iterator();
            linkQ.poll();
            linkQ.peek();
            linkQ.remove(1);
            linkQ.size();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            linkQ.put(2);
            queue.put(1);


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
