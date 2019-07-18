/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 16:59 2019/1/23
 * @Modified By:
 */
public class TestGeneraca<T> {
    T obj;

    int b;
    public TestGeneraca(T obj) {
        this.obj = obj;
    }

    public <U> boolean  f(Class<U> c) {

        c.isInstance(b);
        String.class.isInstance(b);
        return obj instanceof TestGeneraca;
    }

    public static void main(String[] args) {
        TestGeneraca<String> t = new TestGeneraca<String>("1");
        System.out.println(t.f(Integer.class));
    }
}
