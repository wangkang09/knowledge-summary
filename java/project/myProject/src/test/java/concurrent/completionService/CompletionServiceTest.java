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
public class CompletionServiceTest {

    static class Task implements Callable<String>{
        private int i;

        public Task(int i){
            this.i = i;
        }

        @Override
        public String call() throws Exception {
            Thread.sleep(10000);
            return Thread.currentThread().getName() + "执行完任务：" + i;
        }
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException{
        testExecutorCompletionService();
    }

    private static void testExecutorCompletionService() throws InterruptedException, ExecutionException{
        int numThread = 5;
        ExecutorService executor = Executors.newFixedThreadPool(numThread);
        CompletionService<String> completionService = new ExecutorCompletionService<String>(executor);
        for(int i = 0;i<numThread;i++ ){
            completionService.submit(new CompletionServiceTest.Task(i));
        }
        for(int i = 0;i<numThread;i++ ){
            System.out.println(completionService.take().get());
        }
    }

}