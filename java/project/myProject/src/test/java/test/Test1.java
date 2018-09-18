package test;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 10:48 2018/8/22
 * @Modified By:
 */
public class Test1 {
    public static void main(String[] args) {
        String str = "54321";
        System.out.println(str.substring(0,str.length()-4));
        System.out.println(str.substring(str.length()-4,str.length()));

    }
    public void mm() {
        synchronized (this) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void m1() {
        ReentrantLock r1 = new ReentrantLock();
        r1.lock();
        try {
        }finally {
            r1.unlock();
        }
    }

}
