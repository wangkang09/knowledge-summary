package thinkInJava.test;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 17:13 2018/10/2
 * @Modified By:
 */
public class TestCast {
    public static void main(String[] args) {
        TestCast t = new TestCast();
        int temp = 1;
        byte aa = 1;
        t.test1(aa);
    }

//    public void test1(byte a) {
//        System.out.println("byte");
//    }


    public void test1(char a) {
        System.out.println("char");
    }
    public void test1(int a) {
        System.out.println("int");
    }
}
