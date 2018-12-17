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
        //testAddAll();
        //testCollectionSort();
        testIntYingYong();
    }

    //测试引用
    private static void testIntYingYong() {
        int[] count = new int[1];
        count[0] = 1;
        countTest(count);
        System.out.println(count[0]);
    }

    private static void countTest(int[] count) {
        count[0] = 2;
    }

    private static void testCollectionSort() {
        List<Integer> l = new ArrayList<>();
        l.add(1);
        l.add(-1);
        l.add(5);
        l.add(-9);
        Collections.sort(l);
        System.out.println(1);
    }

    public static void testAddAll() {
        List<Integer> l = new ArrayList<>();
        l.add(1);
        l.add(null);
        List<Integer> ll = new ArrayList<>();

        ll.addAll(l);

        System.out.println(ll.get(1));
        System.out.println(l.get(1));



    }
}
/*
    List<Integer> l = new ArrayList<>();
        l.add(1);
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

        List<Integer> ll = new LinkedList<>();
        ll.add(1);*/
