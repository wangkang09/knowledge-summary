package concurrent;

import java.util.concurrent.Semaphore;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 11:40 2018/9/13
 * @Modified By:
 */
public class SemopherTest {
    public static void main(String[] args) {
        Semaphore se = new Semaphore(3);

        try {
            se.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            se.release();
        }
    }
}
