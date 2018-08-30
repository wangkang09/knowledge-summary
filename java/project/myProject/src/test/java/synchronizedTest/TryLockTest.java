package synchronizedTest;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 15:40 2018/8/28
 * @Modified By:
 */
public class TryLockTest {
    ReentrantLock aa = new ReentrantLock();
    static Thread thread1 = null;
    static Thread thread2 = null;

    public static void main(String[] args) {
        TryLockTest b = new TryLockTest();

        thread1 =   new Thread(new Runnable() {
            @Override
            public void run() {
                b.w();
            }
        });
        thread1.start();

        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread2 =   new Thread(new Runnable() {
            @Override
            public void run() {
                b.w();
            }
        });
        thread2.start();

    }

    public void w() {
        try {
            aa.tryLock(2,TimeUnit.HOURS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            TimeUnit.SECONDS.sleep(31);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            System.out.println(Thread.currentThread()==thread1);
            thread2.interrupt();
            aa.unlock();
        }
    }
}
