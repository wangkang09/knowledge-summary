package otherAlgorithm;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 10:37 2018/9/14
 * @Modified By:
 */
public class Singleton {
    private Singleton(){};
    private static volatile Instance instance;
    public static Instance getInstance() {
        if(instance == null) {
            synchronized (Singleton.class) {
                if(instance ==null) {
                    instance = new Instance();
                }
            }
        }
        return instance;
    }

    public static void main(String[] args) {
        //枚举类型验证
        Instance instance = EmumSingleton.INSTANCE.getInstance();
        Instance instance1 = EmumSingleton.INSTANCE.getInstance();
        assert instance==instance1;

        //double check 验证
        Instance instance2 = Singleton.getInstance();
        Instance instance3 = Singleton.getInstance();
        assert instance2==instance3;


    }
}

enum EmumSingleton {
    INSTANCE("1号", 12);
    private String name;
    private int age;
    private Instance instance;

    private EmumSingleton(String name, int age) {
        this.name = name;
        this.age = age;
        instance = new Instance();
    }

    public Instance getInstance(){
        return instance;
    }

}
class LazySingletonInnerClass {

    private LazySingletonInnerClass(){};

    private static class SingletonHolder{
        private static Instance instance = new Instance();
    }
    public static Instance getInstance(){
        return SingletonHolder.instance;
    }
}

class EagerSingleton {

    private static Instance instance = new Instance();//在类加载的时候，instance赋的是null值，当都已经分配内存了，只有当第一次调用getInstance来初始化EagerSingleton类时，才被赋予了真正对象

    private static final Instance instance1 = new Instance();//在类加载的时候，就赋了真正对象了，不加final的话更节省资源，相当于单线程中的懒汉模式了吧！

    private EagerSingleton(){};//私有构造器，是为了在不让程序员显示的创建这一类对象，只有在调用此类的静态成员（除static final，反射）时，虚拟机才去主动的创建一个此对象。所以保证了整个程序生命过程中只有一个这个对象。
    //因为这个类，相当于一个工具类（单例工厂类），用来产生单例，所以整个过程中只需要有一个工厂就行了，通过new 来创建工厂毫无意义。
    //最好是用private修饰，这样以后看着类时，很直观的知道，这个类不应该去new它

    public static Instance getInstance(){
        return instance;
    }

}


class Instance {

}