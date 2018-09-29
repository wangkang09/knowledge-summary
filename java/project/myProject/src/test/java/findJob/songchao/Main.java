package findJob.songchao;

import java.util.*;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 20:50 2018/9/21
 * @Modified By:
 */
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int count = sc.nextInt();
        List<Integer> listA = new ArrayList<>(count);
        List<Integer> listB = new ArrayList<>(count);
        Set<Integer> set = new HashSet<>(count);
        int temp;
        int tempB;
        int result = 0;
        for (int i = 0; i < count; i++) {
            temp = sc.nextInt();
            set.add(temp);
            listA.add(temp);
        }

        for (int i = 0; i < count; i++) {
            listB.add(sc.nextInt());
        }

        for (int i = 0; i < count; i++) {
            temp = listA.get(i);

            for (int j = 0; j < count; j++) {
                tempB = listB.get(j);
                set.remove(temp);

                if(tempB==temp) {
                    break;
                }
                if(set.contains(tempB)) result++;
            }
        }

        System.out.println(result);

    }
}
