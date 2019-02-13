package extendTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 15:22 2019/1/16
 * @Modified By:
 */
public class ExtendTest {
    public static void main(String[] args) {

        womanStduent w = new womanStduent();
        w.setName("woman");
        w.setWoman(true);

        student ss = new student();
        ss.setName("a");

        List<womanStduent> l = new ArrayList<>();
        l.add(w);

        List<student> ls = new ArrayList<>();
        ls.add(ss);

        test(l);
    }

    public  static void test(List<? extends student> list) {
        if (list.get(0) instanceof womanStduent) {
            System.out.println(1);
        }

        if (list.get(0) instanceof student) {
            System.out.println(0);
        }

        Map<String,List<student>> s = list.stream().collect(Collectors.groupingBy(x->x.getName()));

        test0(s);
    }

    private static void test0(Map<String,List<student>> map) {

        for (Map.Entry<String, List<student>> stringListEntry : map.entrySet()) {
            System.out.println(stringListEntry.getValue().get(0).toString());
        }
    }
}

class student {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "student{" +
                "name='" + name + '\'' +
                '}';
    }
}

class womanStduent extends student {
    private Boolean woman;

    public Boolean getWoman() {
        return woman;
    }

    public void setWoman(Boolean woman) {
        this.woman = woman;
    }

    @Override
    public String toString() {
        return super.toString() + "womanStduent{" +
                "woman=" + woman +
                '}';
    }

}