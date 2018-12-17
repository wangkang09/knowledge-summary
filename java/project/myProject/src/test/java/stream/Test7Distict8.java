package stream;

import static Tool.MathTool.Person;
import static Tool.MathTool.createPersonList;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 13:46 2018/11/27
 * @Modified By:
 */
public class Test7Distict8 {
    static Random seed = new Random();
    public static void main(String[] args) {
        
        testGroupby();
        //testSort();
        
        
    }

    private static void testSort() {

        List<String> names1 = new ArrayList<String>();
        names1.add("Google ");
        names1.add("Runoob ");
        names1.add("Taobao ");
        names1.add("Baidu ");
        names1.add("Sina ");

        names1.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });

        System.out.println(names1);
        names1 = new ArrayList<String>();
        names1.add("Google ");
        names1.add("Runoob ");
        names1.add("Taobao ");
        names1.add("Baidu ");
        names1.add("Sina ");

        List nn = names1.stream().sorted((a, b) -> a.compareTo(b)).collect(Collectors.toList());
        names1.sort((a,b)->a.compareTo(b));
        System.out.println(nn);
        System.out.println(names1);

    }

    private static void testGroupby() {
        Map<Integer,List<Person>> result = new HashMap<>();
        Map<Integer,List<Person>> result8 = new HashMap<>();

        List<Person> list = createPersonList(1200000);
        long start = System.currentTimeMillis();
        for (Person person : list) {
            List<Person> temp = result.get(person.getAge());
            if(temp == null) {
                temp = new ArrayList<>();
                result.put(person.getAge(),temp);
            }
            temp.add(person);
        }
        System.out.println(result.size());
        long end = System.currentTimeMillis();
        System.out.println(end-start);
        start = end;
        //list.stream().collect(Collectors.groupingBy(Person::getAge));
        result8 = list.parallelStream().collect(Collectors.groupingBy(Person::getAge));//不能用并行，因为list/map是线程不安全的
        System.out.println(System.currentTimeMillis()-start);
    }


}
