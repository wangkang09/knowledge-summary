package concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 10:08 2018/8/17
 * @Modified By:
 */
public class FutureTaskTest {
    static class Task implements Callable<String> {
        private int i;

        public Task(int i){
            this.i = i;
        }

        @Override
        public String call() throws Exception {
            Thread.sleep(2000);
            return Thread.currentThread().getName() + "执行完任务：" + i;
        }
    }

    static class Task1 implements Runnable {
        private int i;

        public Task1(int i){
            this.i = i;
        }

        @Override
        public void run() {
            System.out.println("进入Runnable");
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        int numThread = 5;
        ExecutorService executor = Executors.newFixedThreadPool(numThread);
        Future runn = executor.submit(new Task1(1),1);
        Future calla = executor.submit(new Task(2));
        List<Task> s = new ArrayList<>();
        s.add(new Task(22));
        s.add(new Task(33));
        String a =  executor.invokeAny(s);
        List<Future<String>> b =  executor.invokeAll(s);

        System.out.println(runn.get());
        System.out.println(calla.get());
        FutureTask c = new FutureTask(new Task(2));
        c.run();
        executor.shutdown();
        executor.shutdownNow();

    }

    private static void testUseFuture(){

        List<Future<String>> futureList = new ArrayList<Future<String>>();



    }
}
