package leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 19:29 2018/9/10
 * @Modified By:
 */
public class L401 {
    public static void main(String[] args) {
        List<String> l = new Solution401().readBinaryWatch(2);
        System.out.println(1);

    }
}

class Solution401 {
    List<String> result = new ArrayList<>();
    int count;
    List<String> target = Arrays.asList("1:00", "2:00", "4:00", "8:00", "0:01", "0:02", "0:04", "0:08", "0:16", "0:32");

    public List<String> readBinaryWatch(int num) {
        if(num==0) return Arrays.asList("0:00");
        count = num;
        resolve(0,0,new ArrayList<>());
        Collections.sort(result);
        return result;
    }

    private void resolve(int index,int cur,List<String> list) {
        if(cur==count) {
            result.add(sum(list));
            return;
        } else {
            for (int i = index; i < target.size(); i++) {
                list.add(target.get(i));
                resolve(i+1,cur+1,list);
                list.remove(list.size()-1);
            }
        }
    }

    private String sum(List<String> list) {
        String str = list.get(0);
        for (int i = 1; i < list.size(); i++) {
            str = sum0(str,list.get(i));
        }
        return str;
    }

    private String sum0(String str, String s) {
        String[] str1 = str.split(":");
        String[] str2 = s.split(":");

        int hour = Integer.valueOf(str1[0]) + Integer.valueOf(str2[0]);
        int min10 = Integer.valueOf(str1[1].substring(0,1)) + Integer.valueOf(str2[1].substring(0,1));
        int min0 = Integer.valueOf(str1[1].substring(1,2)) + Integer.valueOf(str2[1].substring(1,2));

        if(min0>=10) {
            min0 -= 10;
            min10 += 1;
        }

        if(min10>=6){
            min10 -= 6;
            hour += 1;
        }

        return hour + ":" + min10  + min0;
     }


}