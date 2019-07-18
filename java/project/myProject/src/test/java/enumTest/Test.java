package enumTest;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 10:04 2019/1/16
 * @Modified By:
 */
public class Test {
    public static void main(String[] args) {

        TTE tt = new TTE();
        System.out.println(tt.tt);
        TT t = null;
        if (TT.UPDATE == t) {
            System.out.println(1);
        }
        System.out.println(2);
    }
}
