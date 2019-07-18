package testIterator;

import java.util.Iterator;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 9:46 2019/1/23
 * @Modified By:
 */
public class IteratorFibonacci extends Fibonacci implements Iterable<Integer> {
    private int n;
    public IteratorFibonacci(int count) {
        n = count;
    }

    public static void main(String[] args) {
        for (Integer integer : new IteratorFibonacci(18)) {
            System.out.println(integer + "");
        }
    }

    @Override
    public Iterator<Integer> iterator() {

        return new Iterator<Integer>() {
            @Override
            public boolean hasNext() {
                return n > 0;
            }

            @Override
            public Integer next() {
                n--;
                return IteratorFibonacci.this.next();
            }
        };
    }
}
