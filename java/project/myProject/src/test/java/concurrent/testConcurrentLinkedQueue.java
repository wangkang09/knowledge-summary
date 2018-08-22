package concurrent;

import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 15:12 2018/8/10
 * @Modified By:
 */
public class testConcurrentLinkedQueue {

    public static void main(String[] args) {
        ConcurrentLinkedQueue c = new ConcurrentLinkedQueue();
        ConcurrentLinkedDeque d = new ConcurrentLinkedDeque();
        d.pollFirst();
        c.add(1);
        c.add(2);
        c.add(1);
        c.add(3);
        c.add(3);
        c.add(4);
        c.poll();
        c.poll();
        c.poll();
        c.poll();
    }
}
