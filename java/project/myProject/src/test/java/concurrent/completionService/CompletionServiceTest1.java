package concurrent.completionService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 9:10 2018/8/17
 * @Modified By:
 */
public class CompletionServiceTest1 {
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

    public static void main(String[] args){
        testUseFuture();
    }

    private static void testUseFuture(){
        int numThread = 5;
        ExecutorService executor = Executors.newFixedThreadPool(numThread);
        List<Future<String>> futureList = new ArrayList<Future<String>>();
        for(int i = 0;i<numThread;i++ ){
            Future<String> future = executor.submit(new CompletionServiceTest1.Task(i));
            System.out.println(future.isDone());
            future.cancel(true);
            System.out.println(future.isCancelled());
            futureList.add(future);
        }

        while(numThread > 0){
            for(Future<String> future : futureList){
                String result = null;
                try {
                    result = future.get(0, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    //超时异常直接忽略
                }
                if(null != result){
                    futureList.remove(future);
                    numThread--;
                    System.out.println(result);
                    //此处必须break，否则会抛出并发修改异常。（也可以通过将futureList声明为CopyOnWriteArrayList类型解决）
                    break;
                }
            }
        }
        executor.shutdown();
    }
}