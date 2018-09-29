package multiDynamic;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 13:17 2018/9/29
 * @Modified By:
 */
public class TestShape {
    public static void main(String[] args) {
        TestShape t = new TestShape();
        triangle tri = new triangle();
        Shape cir = new circle();
        t.doSomething(tri);
        t.doSomething(cir);

    }

    public void doSomething(Shape shape) {
        shape.draw();
    }
}
