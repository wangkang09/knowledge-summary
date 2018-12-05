package collection.safe;

import java.util.Vector;

/**
 * @Description: 所有的关键方法，都加了synchronized保证数据一致性
 * @Author: wangkang
 * @Date: Created in 16:17 2018/9/3
 * @Modified By:
 */
public class VecotorTest {
    private static volatile String str = "a";
    public static void main(String[] args) {
        Vector<Integer> v = new Vector<>();
        v.size();
        v.add(1);
        v.get(0);
        v.remove(0);
        v.iterator();
        v.lastIndexOf(1);

    }

    public static Vector<Integer> num =  new Vector<>();

    public static void test() {
        while (true) {
            for (int i = 0; i < 100; i++) {
                num.add(i);
            }

            Thread t1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < num.size(); i++) {
                        num.remove(i);
                    }
                }
            });

            Thread t2 = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < num.size(); i++) {
                        System.out.println(num.get(i));
                    }
                }
            });

            t1.start();
            t2.start();

        }
    }
}
