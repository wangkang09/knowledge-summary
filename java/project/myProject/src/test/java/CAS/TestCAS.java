package CAS;

import Tool.MathTool;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 15:01 2018/11/28
 * @Modified By:
 */
public class TestCAS {
    public static void main(String[] args) {
        AtomicReference turn = new AtomicReference();
        Person p1 = new Person("wk",12,"man");
        Person p2 = new Person("wk",13,"man");
        Person p3 = p1;
        AtomicReference cas = new AtomicReference();
        turn.set(p3);
        cas = turn;
        turn.compareAndSet(p1,p2);//这一步之后，cas和turn的内容同时变了，这很特别

        System.out.println(1);
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
