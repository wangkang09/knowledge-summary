package test;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 20:57 2018/8/22
 * @Modified By:
 */
public class Test2 {
    public static void main(String[] args) {
        MyThread thread = new MyThread();
        thread.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread.interrupt();
        //Thread.currentThread().interrupt();
        System.out.println("是否停止1？="+Thread.interrupted());//false
        System.out.println("是否停止2？="+thread.interrupted());//false main线程没有被中断!!!
    }
}
class MyThread extends Thread {
    @Override
    public void run() {
               super.run();
                for (int i = 0; i < 500000; i++) {
                       System.out.println("i=" + (i + 1));
                    }
                    this.interrupted();
             }
}