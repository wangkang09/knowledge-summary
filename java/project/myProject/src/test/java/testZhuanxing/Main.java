package testZhuanxing;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 21:07 2019/1/20
 * @Modified By:
 */
public class Main {
    public static void main(String[] args) {
        test t = new test();
        sub sub = new sub();

        test sub0 = sub;

        sub sub1 = (sub)t;

        sub1.sub2();
    }
}
