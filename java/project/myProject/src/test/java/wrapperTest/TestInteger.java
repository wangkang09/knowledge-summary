package wrapperTest;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 10:56 2018/12/18
 * @Modified By:
 */
public class TestInteger {
    public static void main(String[] args) {
        Integer a = 1;
        test(a);
        System.out.println(a);
    }

    private static void test(Integer a) {
        a = 2;
    }
}
