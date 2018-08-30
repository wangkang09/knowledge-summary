## CountDownLatch测试

```java
public class CountDownLatchFirst {
    public static void main(String[] args) throws InterruptedException {
        CountDownLatchFirst count = new CountDownLatchFirst();
        count.test1();
    }
    void test1() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(2);

        ExecutorService executor = Executors.newCachedThreadPool();
        executor.execute(new MyThread("Main", countDownLatch));
        executor.execute(new MyThread("Sub", countDownLatch));
        //executor.execute(new MyThread("Sub4", countDownLatch));
    
        // main方法线程等待上面的两个线程完成后才会执行
        new Thread(new Runnable() {
    
            @Override
            public void run() {
                try {
                    Thread.sleep(10000);
                    countDownLatch.await();
    
                } catch (Exception e) {
    
                }
                System.out.println("await1");
            }
        }).start();
    
        System.out.println("~~~ 可以执行了 ~~~");
        //executor.execute(new MyThread("Sub1", countDownLatch));
    
        Thread.sleep(15000);
        countDownLatch.await();
        System.out.println("await2");
        executor.shutdownNow();
        executor.awaitTermination(1,TimeUnit.DAYS);


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
                Long a = 1000L;
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
```