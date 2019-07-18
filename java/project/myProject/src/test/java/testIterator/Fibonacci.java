package testIterator;


/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 9:42 2019/1/23
 * @Modified By:
 */
public class Fibonacci  {
    private int count = 0;

    public int next() {
        return fib(count++);
    }

    private int fib(int i) {
        if (i<2) return 1;
        return fib(i-2 ) + fib(i - 1);
    }

    public static void main(String[] args) {
        Fibonacci gen = new Fibonacci();
        for (int i = 0; i < 18; i++) {
            System.out.println(gen.next() + " ");
        }
    }

}
