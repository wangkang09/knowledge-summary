package test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 22:00 2018/8/22
 * @Modified By:
 */
public class ReentrantLockTest {
    ReentrantLock aa = new ReentrantLock();

    public static void main(String[] args) {
        ReentrantLockTest b = new ReentrantLockTest();

        new Thread(new Runnable() {
            @Override
            public void run() {
                b.w();
            }
        }).start();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        b.w();

    }

    public void w() {
        aa.lock();

        try {
            aa.lockInterruptibly();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            TimeUnit.SECONDS.sleep(300000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            aa.unlock();
        }
    }
}
class MyThread1 extends Thread {
    @Override
    public void run() {

    }
}
