package leetcode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 22:53 2018/9/9
 * @Modified By:
 */
public class L78 {
    static int count;
    static int[] src;
    static List<List<Integer>> result = new ArrayList<>();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        count = sc.nextInt();
        src = new int[count];
        for (int i = 0; i < count; i++) {
            src[i] = sc.nextInt();
        }
        sc.close();

        new L78().subsets();
        result.add(Collections.EMPTY_LIST);
        System.out.println(result.toString());
    }
    public List<List<Integer>> subsets() {
        solution(-1,new ArrayList<>());
        return result;
    }

    private void  solution(int index,List<Integer> list) {
        if(index==count) {
            List temp = new ArrayList(list);
            result.add(temp);
        }

        if(index<count) {
            for (int i = index+1; i < count; i++) {
                list.add(src[i]);//这个肯定要在前面的
                List temp = new ArrayList(list);
                result.add(temp);
                solution(i,list);
                list.remove(list.size()-1);
            }
        }
    }
}
