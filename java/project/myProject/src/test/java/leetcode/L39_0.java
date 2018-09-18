package leetcode;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 22:39 2018/9/9
 * @Modified By:
 */
public class L39_0 {
    static int[] candidates;
    static int target;
    static List<List<Integer>> result = new ArrayList<>();

    public static void main(String[] args) {
            Scanner sc = new Scanner(System.in);
            int count = sc.nextInt();
            target = sc.nextInt();

            candidates = new int[count];
            for (int i = 0; i < count; i++) {
                candidates[i] = sc.nextInt();
            }
            sc.close();

            solution(0,0,new ArrayList());
        System.out.println(result.toString());
    }

    static void solution(int index, int sum, List list) {
        if(sum==7) {
            List<Integer> temp = new ArrayList<>(list);
            result.add(temp);
        }
        if(sum<7) {
            for (int i = index; i < candidates.length; i++) {
                list.add(candidates[i]);
                solution(i,sum+candidates[i],list);
                list.remove(list.size()-1);
            }
        }
    }


}
