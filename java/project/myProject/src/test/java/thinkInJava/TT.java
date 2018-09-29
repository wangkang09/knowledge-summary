package thinkInJava;

import thinkInJava.test.TestPrivate;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 16:44 2018/9/28
 * @Modified By:
 */
public class TT extends TestPrivate {
    public static void main(String[] args) {
        TestPrivate t = new TestPrivate();
        TT tt = new TT();
        tt.protec++;
    }
}
