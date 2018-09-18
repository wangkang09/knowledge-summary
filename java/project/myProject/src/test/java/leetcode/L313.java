package leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 20:27 2018/9/10
 * @Modified By:
 */
public class L313 {
    public static void main(String[] args) {
        List l = new Solution313().partition("aabb");
        System.out.println(1);
    }
}

class Solution313 {
    List<List<String>> result = new ArrayList<>();
    String target;
    public List<List<String>> partition(String s) {
        target = s;
        if(isHuiWen(s)) {
            result.add(Arrays.asList(s));
        }

        resolve(0,new ArrayList<>());
        return  result;
    }

    private void resolve(int begin , List<String> list) {
        if(begin==target.length()) {
            result.add(new ArrayList<>(list));
        } else {
            for (int i = begin; i < target.length(); i++) {
                if(isHuiWen(target.substring(begin,i+1))) {
                    list.add(target.substring(begin,i+1));
                    resolve(i+1,list);
                    list.remove(list.size()-1);
                }
            }
        }

    }



    private boolean isHuiWen(String substring) {
        for (int i = 0; i < substring.length()/2; i++) {
            if(substring.charAt(i)!=substring.charAt(substring.length()-i-1)) return false;
        }
        return true;
    }
}