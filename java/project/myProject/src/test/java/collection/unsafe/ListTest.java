package collection.unsafe;

import java.util.*;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 16:25 2018/9/3
 * @Modified By:
 */
public class ListTest {
    public static void main(String[] args) {
        List<Integer> l = new ArrayList<>();
//        l.iterator();
//        l.remove(1);
//        l.add(1);

        List<Integer> link = new LinkedList<>();
        link.lastIndexOf(1);
//        link.iterator();
//        link.add(1);
//        link.remove(1);
//        ((LinkedList<Integer>) link).removeFirst();
//        ((LinkedList<Integer>) link).addFirst(1);

        Queue<Integer> q = new PriorityQueue<>();
        Deque<Integer> d = new ArrayDeque<>();

        q.offer(0);
        q.offer(2);
        q.offer(1);
        q.offer(-1);
        q.offer(0);
        q.offer(-10);
        q.offer(2);


    }
}
