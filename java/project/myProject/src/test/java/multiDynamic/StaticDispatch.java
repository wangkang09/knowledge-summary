package multiDynamic;

/**
 * @Description: 仅仅使方法的重载
 * @Author: wangkang
 * @Date: Created in 13:24 2018/9/29
 * @Modified By:
 */
public class StaticDispatch {
    static abstract class Human {
        void say() {
            System.out.println("human");
        }
    }
    static class Man extends Human{
        @Override
        void say() {
            System.out.println("man");
        }
    }
    static class Woman extends Human{
        @Override
        void say() {
            System.out.println("woman");
        }
    }
    public void sayHello(Human guy) {
        guy.say();
        System.out.println("hello,guy");
    }
    public void sayHello(Man guy) {
        guy.say();
        System.out.println("hello,Man");
    }
    public void sayHello(Woman guy) {
        guy.say();
        System.out.println("hello,Woman");
    }

    public static void main(String[] args) {
        Human man = new Man();
        Human woman = new Woman();
        Woman wo = new Woman();
        StaticDispatch st = new StaticDispatch();
        st.sayHello(man);
        st.sayHello(woman);
        st.sayHello(wo);
    }
}
