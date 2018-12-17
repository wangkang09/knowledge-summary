package stream;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import static java.util.concurrent.ThreadLocalRandom.current;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @Description: Java 8流式编程测试
 * @Author: wangkang
 * @Date: Created in 9:30 2018/11/27
 * @Modified By:
 */
public class TestStream {

    public static void main(String[] args) {

//        testForeach();
//        testReduce();
//        testFlatMap();
//        testMap();
//        testGenerateStream();
//        testIterator();
//        testFlatMap0();
//        testGroupBy();
//        testPartitionBy();

        testSorted();

    }

    private static void testSorted() {
        List<String> sort = new ArrayList<>();
        sort.add("aw124");
        sort.add("0123ad");
        sort.add("zwzsg");
        sort.add("zwzsga");

        List list = sort.stream().sorted().collect(Collectors.toList());

        System.out.println(1);
    }

    private static void testPartitionBy() {

    }

    private static void testGroupBy() {
        //------------------------------groupingBy
        //groupby key
        Map<Integer,List<Person>> pMapList = Stream.generate(new PersonSupplier()).limit(100).collect(Collectors.groupingBy(p->p.age));

        //groupby key后，自定义value
        Map<Integer,Integer> pMap = Stream.generate(new PersonSupplier()).limit(100).collect(Collectors.groupingBy(Person::getAge,Collectors.summingInt(p->p.age)));

        Map<Integer,Optional<Person>> pMap1 = Stream.generate(new PersonSupplier()).limit(100).collect(Collectors.groupingBy(Person::getAge,Collectors.maxBy(Comparator.comparing(p->p.age))));

        Arrays.asList(1,2,4,5,6).stream().collect(Collectors.groupingBy(p->p*p));
    }

    private static void testFlatMap0() {
        //------------------------------一种流转换成另一种流 String -> int; flatMapToInt，flatMap
        //将Person流转换为Person中的age数组
        int[] PersonToInts = Stream.generate(new PersonSupplier()).limit(10).flatMapToInt(p->IntStream.of(p.age)).toArray();
        Arrays.stream(PersonToInts).forEach(n-> System.out.print(n+ " "));

        List<String> intToString = Stream.generate(()->current().nextInt(100)).limit(10).flatMap(n->Stream.of("wk"+n)).collect(Collectors.toList());
        System.out.println(intToString);
    }

    private static void testIterator() {
        //------------------------------生成等差数列 iterate
        int[] intarray = Stream.iterate(0,n->n+2).limit(10).mapToInt(n->n).toArray();
        Arrays.stream(intarray).forEach(n->{
            System.out.print(n+ " ");
        });
    }

    private static void testGenerateStream() {
        //------------------------------自己生成测试流
        Stream.generate(()-> current().nextInt(40)).limit(10).forEach(n->{
            System.out.print(n+" ");
        });
        System.out.println();
        IntStream.generate(()->current().nextInt(100)).limit(10).forEach(n-> System.out.print(n+" "));

        //这里的boxed可能比较耗资源
        List<Integer> list = IntStream.generate(()->current().nextInt(100)).limit(10).boxed().collect(Collectors.toList());
        int[] ints = IntStream.generate(()->current().nextInt(100)).limit(10).toArray();

        List<Person> listP = Stream.generate(new PersonSupplier()).limit(10).collect(Collectors.toList());

        for (Person p : listP) {
            System.out.println(p);
        }

        listP.stream().forEach(System.out::println);
    }

    private static void testMap() {
        //------------------------------Map
        List<Integer> mapList = Arrays.asList(1,3,5,6,7).stream().map(n->-n).collect(Collectors.toList());
    }

    private static void testFlatMap() {
        //------------------------------flatMap
        String file = "lines";
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            List<String> words = br.lines().
                    flatMap(line -> Stream.of(line.split(" "))).
                    filter(word -> word.length() > 0).
                    map(String::toLowerCase).
                    distinct().
                    sorted().
                    collect(Collectors.toList());
            System.out.println(words);
            System.out.println(words.get(1).length());//4
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void testReduce() {
        //------------------------------REDUCE
        String concat = Stream.of("A", "B", "C", "D").reduce("wk ", String::concat);
        System.out.println(concat);
    }

    private static void testForeach() {
        //-----------------------------FOREACH
        //IntStream.of(new int[]{1, 2, 3}).forEach(System.out::print);
        IntStream.of(new int[]{1, 2, 3}).forEach(p->{
            System.out.print(p+" ");
        });
        System.out.println();
        IntStream.range(1, 3).forEach(System.out::print);
        System.out.println();
        IntStream.rangeClosed(1, 3).forEach(System.out::print);
        System.out.println();
    }

    private static class Person {
        private Integer age;
        private String name;

        public Person(Integer age, String name) {
            this.age = age;
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "age=" + age +
                    ", name='" + name + '\'' +
                    '}';
        }
    }

    private static class PersonSupplier implements Supplier<Person> {
        private int age = 18;
        private Random seed = new Random();

        @Override
        public Person get() {
            return new Person(age++,"wk"+seed.nextInt(100));
        }
    }
}
