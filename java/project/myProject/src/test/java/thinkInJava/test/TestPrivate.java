package thinkInJava.test;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 15:18 2018/9/28
 * @Modified By:
 */
public class TestPrivate {
    private int pri;
    public int publ;
    protected int protec;
    int defaul;
    private void pp() {
        System.out.println(1);
    }

    public static void main(String[] args) {
        TestPrivate t = new TestPrivate();
        t.pp();
    }
}

class Prv {
    public static void main(String[] args) {
        TestPrivate tt = new TestPrivate();
        //不能用tt.a和tt.pp();

    }
}

class Pri extends TestPrivate{
    public static void main(String[] args) {
        Pri p = new Pri();

    }
}
