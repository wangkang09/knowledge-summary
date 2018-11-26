package collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 17:24 2018/11/26
 * @Modified By:
 */
public class TestComparable {

    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(-1);
//        list.add(1);
//        list.add(21);
//        list.add(-3);
        list.addAll(Arrays.asList(1,21,-3));

        list.sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1-o2>0?1:o1.equals(o2)?0:-1;
//                if(o1.equals(o2)) return 0;
//                if(o1>o2) return 1;
//                return -1;
            }
        });

        for (Integer integer : list) {
            System.out.println(integer);
        }


        point[] points = new point[5];
        points[0] = new point(1);
        points[1] = new point(-1);
        points[2] = new point(11);
        points[3] = new point(5);
        points[4] = new point(-9);

        Arrays.sort(points, new Comparator<point>() {
            @Override
            public int compare(point o1, point o2) {
                return o1.x-o2.x>0?1:o1.x==o2.x?0:-1;
            }
        });

        for (point point : points) {
            System.out.println(point);
        }


    }

    public static class point {
        private int x;

        public point(int x) {
            this.x = x;
        }

        @Override
        public String toString() {
            return "point{" +
                    "x=" + x +
                    '}';
        }
    }
}
