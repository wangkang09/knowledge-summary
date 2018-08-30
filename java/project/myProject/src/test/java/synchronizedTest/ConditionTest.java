package synchronizedTest;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 16:22 2018/8/28
 * @Modified By:
 */
public class ConditionTest {
    ReentrantLock aa = new ReentrantLock();
    Condition condition = aa.newCondition();
    static Thread thread = null;
    static Thread thread1 = null;

    public static void main(String[] args) {
        ConditionTest b = new ConditionTest();

        thread =   new Thread(new Runnable() {
            @Override
            public void run() {
                b.w();
            }
        });
        thread.start();

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread1 =   new Thread(new Runnable() {
            @Override
            public void run() {
                b.w();
            }
        });
        thread1.start();

    }

    public void w() {
        aa.lock();

        try {
            condition.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            TimeUnit.SECONDS.sleep(32);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            System.out.println(Thread.currentThread()==thread);
            thread1.interrupt();
            aa.unlock();//不管是lock还是lockInterruptibly都要在finally块中解锁
            //如果是lockInterruptibly，要加
            //if(aa.isHeldByCurrentThread()) {
            //    aa.unlock();
            //}
        }
    }
}
