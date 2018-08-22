package concurrent;

import java.util.concurrent.*;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 17:16 2018/8/16
 * @Modified By:
 */
public class CountDownLatchFirst {

    public static void main(String[] args) throws InterruptedException {
        CountDownLatchFirst count = new CountDownLatchFirst();
        count.test1();
    }


    void test1() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(2);

        ExecutorService executor = Executors.newCachedThreadPool();
        executor.execute(new MyThread("Main", countDownLatch));
        //executor.execute(new MyThread("Sub", countDownLatch));
        //executor.execute(new MyThread("Sub4", countDownLatch));

        // main方法线程等待上面的两个线程完成后才会执行
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                    countDownLatch.await();
                } catch (Exception e) {

                }
                System.out.println("await1");
            }
        }).start();

        System.out.println("~~~ 可以执行了 ~~~");
        //executor.execute(new MyThread("Sub1", countDownLatch));

        countDownLatch.await();
        System.out.println("await2");
        executor.shutdownNow();


    }
    class CustomRejectedExecutionHandler implements RejectedExecutionHandler {

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            try {
                // 核心改造点，由blockingqueue的offer改成put阻塞方法
                executor.getQueue().put(r);//一直阻塞到成功加入阻塞队列为止
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    class MyThread implements Runnable {

        private final String name;
        private final CountDownLatch latch;

        public MyThread(final String name, final CountDownLatch latch) {
            this.name = name;
            this.latch = latch;
        }

        @Override
        public void run() {
            try {
                Long a = 2000L;
                System.out.println(a);
                Thread.sleep( a);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("线程 " + name + " 完成工作");

            latch.countDown();
        }
    }
}
