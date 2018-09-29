package test;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 10:48 2018/8/22
 * @Modified By:
 */
public class Test1 {
    public static void main(String[] args) {
        Map<Integer,Integer> map = new LinkedHashMap<>();
        map.put(0,1);
        map.put(-1,3);

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
