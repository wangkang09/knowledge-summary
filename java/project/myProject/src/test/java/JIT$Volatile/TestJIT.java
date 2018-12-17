package JIT$Volatile;

import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 13:46 2018/12/3
 * @Modified By:
 */
public class TestJIT {
    static class MyTask implements Runnable {
        private final int loopTimes;
        private boolean running = true;
        boolean stopped = false;
        public MyTask(int loopTimes) {
            this.loopTimes = loopTimes;
        }
        @Override
        public void run() {
            try {
                while (running) {
                    longCalculation();
                }
            } finally {
                stopped = true;
                System.out.println("Thread：stopped==true");
            }
        }
        private void longCalculation() {
             System.out.println("Thread：runnig="+running);

        }
    }
    public static void main(String[] args) throws InterruptedException {
        //int loopTimes = Integer.parseInt(args[0]);
        int loopTimes = 3;
        MyTask task = new MyTask(loopTimes);
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
        TimeUnit.MILLISECONDS.sleep(500);
        task.running = false;
        System.out.println("Main：running==false");
        for (int i = 0; i <= 200; i++) {
            //TimeUnit.MILLISECONDS.sleep(20);
            System.out.println("Main：stopped = " + task.stopped);
            if (task.stopped)
                break;
        }
    }
}
