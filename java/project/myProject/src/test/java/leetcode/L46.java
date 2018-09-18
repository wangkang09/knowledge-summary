package leetcode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 15:42 2018/9/10
 * @Modified By:
 */
public class L46 {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String str = sc.nextLine();
        String[] strs = str.split(",");
        strs[0] = strs[0].substring(1);
        strs[strs.length-1] = strs[strs.length-1].substring(0,strs[strs.length-1].length()-1);

        int[] target = new int[strs.length];
        for (int i = 0; i < target.length; i++) {
            target[i] = Integer.valueOf(strs[i]);
        }
        List a = new Solution().permute(target);
        System.out.println(1);

    }

}

class Solution {
    List<List<Integer>> reslut = new ArrayList<>();
    int[] target;
    public List<List<Integer>> permute(int[] nums) {
        target = nums;
        boolean[] flag = new boolean[nums.length];
        resolve(0,flag,new ArrayList());

        return reslut;
    }

    private void resolve(int count, boolean[] flag, List list) {
        if(count==target.length) {
            List<Integer> temp = new ArrayList<>(list);
            reslut.add(temp);
        } else {
            for (int i = 0; i < flag.length; i++) {
                if(flag[i] == false) {
                    flag[i] = true;
                    list.add(target[i]);
                    resolve(count+1,flag,list);
                    list.remove(list.size()-1);
                    flag[i] = false;
                }
            }
        }

    }
}