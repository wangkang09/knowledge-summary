package Tool;

import java.util.List;
import static java.util.concurrent.ThreadLocalRandom.current;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 13:47 2018/11/27
 * @Modified By:
 */
public class MathTool {



    /**
     *
     * @Description: 生产大小在[0,bound]内，长度为len的list
     *
     * @auther: wangkang
     * @date: 15:02 2018/11/27
     * @param: [bound, len]
     * @return: java.util.List<java.lang.Integer>
     *
     */
    public  static  List<Integer> createListInteger(int bound, int len) {
        return Stream.generate(()->current().nextInt(bound)).limit(len).collect(Collectors.toList());
    }

    /**
     *
     * @Description: 生产大小在[0,bound]内，长度为len的int[]
     *
     * @auther: wangkang
     * @date: 15:02 2018/11/27
     * @param: [bound, len]
     * @return: int[]
     *
     */
    public static int[] createIntArray(int bound, int len) {
        return IntStream.generate(()->current().nextInt(bound)).limit(len).toArray();
    }

    /**
     *
     * @Description: 通过传入的Supplier和len产生关于该Supplier的list
     *
     * @auther: wangkang
     * @date: 14:58 2018/11/27
     * @param: [len, supplier]
     * @return: java.util.List<T>
     *
     */
    public static <T> List<T> createInfoList(int len,Supplier<T> supplier) {
        return Stream.generate(supplier).limit(len).collect(Collectors.toList());
    }

    /**
     *
     * @Description: 生产长度为len的person数组
     *
     * @auther: wangkang
     * @date: 15:03 2018/11/27
     * @param: [len]
     * @return: Tool.MathTool.Person[]
     *
     */
    public static Person[] createPersonArray(int len) {
        return (Person[]) Stream.generate(new PersonSupplier()).limit(len).toArray();
    }

    /**
     *
     * @Description: 生产长度为len的list<person>
     *
     * @auther: wangkang
     * @date: 15:03 2018/11/27
     * @param: [len]
     * @return: java.util.List<Tool.MathTool.Person>
     *
     */
    public static List<Person> createPersonList(int len) {
        return Stream.generate(new PersonSupplier()).limit(len).collect(Collectors.toList());
    }

    /**
     *
     * @Description: 该方法产生的List中的所有元素是同一元素
     *
     * @auther: wangkang
     * @date: 14:36 2018/11/27
     * @param: [bound, len, obj]
     * @return: java.util.List<T>
     *
     */
    public static <T> List<T> createListInfo(int bound, int len, T obj) {
        InfoSupplier<T> infoSupplier = new InfoSupplier<>(obj);
        return Stream.generate(infoSupplier).limit(len).collect(Collectors.toList());
    }


    public static void main(String[] args) {
    }


    /**
     *
     * @Description: 这个InfoSupplier中的get方法，因为直接返回obj，所以Stream流generate得到的无限个类返回的是同一个类！
     *
     * @auther: wangkang
     * @date: 14:33 2018/11/27
     * @param:
     * @return:
     *
     */
    @Deprecated
    private  static  class InfoSupplier<T> implements Supplier<T> {
        T obj;

        public InfoSupplier(T obj) {
            this.obj = obj;
        }

        @Override
        public T get() {
            return obj;
        }
    }

    private static class PersonSupplier implements Supplier<Person> {


        @Override
        public Person get() {
            int age = 10 + current().nextInt(1000);
            String name = "" + (char)(current().nextInt(26)+'a') + (char)(current().nextInt(26)+'a') + (char)(current().nextInt(26)+'a') ;
            String sex = (age&1)==0?"woman":"man";
            return new Person(name,age,sex);
        }
    }
    public static class Person {
        private String name;
        private Integer age;
        private String sex;


        public Person(String name, Integer age, String sex) {
            this.name = name;
            this.age = age;
            this.sex = sex;
        }

        @Override
        public String toString() {
            return "Info{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    ", sex='" + sex + '\'' +
                    '}';
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }
    }

}
