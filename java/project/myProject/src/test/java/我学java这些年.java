
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class 我学java这些年 {
    private String name;
    private Enum sex;
    private Integer age;

    public 我学java这些年(String name, Sex sex, Integer age) {
        this.name = name;
        this.sex = sex;
        this.age = age;
    }
    public String 如何踏入码农() {
        String step1 = "1. 大学光电信息工程，简单学了点 c、c++，然后就浑浑噩噩考研了\n";
        String step2 = "2. 研究生看了2个星期的 Java 视频，某天导师突然问我最近在干什么，说学 Java 就被分配到导师私人公司，开始了我的码农之旅";
        return step1 + step2;
    }
    public String 为何选择Java() {
        return "命运使然吧~";
    }
    public String 工作学习有和成长() {
        return "方向明确、目标明确、任务明确";
    }
    public String 希望有何收获() {
        String first = "1. 所有知识点都圆满吸收\n";
        String second = "2. 总结归纳出一个完整的知识体系\n";
        String third = "3. 后续可以以这门课程为树干，做各个深度上的扩展";
        return first + second + third;
    }

    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {
        我学java这些年 my = new 我学java这些年("kangfighting", Sex.Man, 26);
        Field[] field = my.getClass().getDeclaredFields();
        for (Field f : field) {
            System.out.println(f.getName() + "：" + f.get(my));
        }
        System.out.println("----------------");

        Method[] ms = my.getClass().getDeclaredMethods();
        for (Method m : ms) {
            if("main".equals(m.getName())) {
                continue;
            } else {
                System.out.println(m.getName() + "：");
                System.out.println(m.invoke(my));
            }
            System.out.println();
        }
    }
}

enum Sex {
    Man,Woman;
}
