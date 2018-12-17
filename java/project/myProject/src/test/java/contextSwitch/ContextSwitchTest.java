package contextSwitch;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 11:43 2018/11/28
 * @Modified By:
 */
public final class ContextSwitchTest {
    static final int RUNS = 30000;
    static final int ITERATES = 100;
    static AtomicReference turn = new AtomicReference();

    static final class WorkerThread extends Thread {
         volatile  Thread other;
         volatile int nparks;

        //other===另一个线程
        //t只可能是other或者this
        //当t==this时，挂起自己，执行一次++p(切换一次)
        //当t==other时，下一次t肯定不等于other，唤醒other
        public void run() {
           // final AtomicReference t = turn;//刚开始turn是a，这个很关键，这个赋值操作，使t和turn变成了一体了，同时改变。这是最关键的地方！！
            final Thread other = this.other;//a线程的other是b，b线程的other是a
            if (turn == null || other == null)
                throw new NullPointerException();
            int p = 0;
            for (int i = 0; i < ITERATES; ++i) {
                //如果是a先走到这，CAS失败，进入park()挂起自己
                //如果是b先走到这，bOther=a,t=a,CAS成功，t=b,upark=a；
                while (!turn.compareAndSet(other, this)) {//
                    LockSupport.park();//当b线程第一次uparkA后，a醒来
                    ++p;//每加一次，说明线程被唤醒一次
                }
                LockSupport.unpark(other);
            }
            LockSupport.unpark(other);
            nparks = p;
            System.out.println("parks: " + p);

        }
    }

    static void test() throws Exception {
        WorkerThread a = new WorkerThread();
        WorkerThread b = new WorkerThread();
        a.other = b;
        b.other = a;
        turn.set(a);
        long startTime = System.nanoTime();
        a.start();
        b.start();
        a.join();
        b.join();
        long endTime = System.nanoTime();
        int parkNum = a.nparks + b.nparks;
        System.out.println("total："+parkNum);
        System.out.println(parkNum==200);
        System.out.println("Average time: " + ((endTime - startTime) / parkNum)
                + "ns");
    }

    public static void main(String[] args) throws Exception {
        long start = System.nanoTime();
        for (int i = 0; i < RUNS; i++) {
            test();
            System.out.println("次数："+i);
        }
        long end = System.nanoTime();
        System.out.println("Total time: " + (end - start) +" ns" );
    }
}
