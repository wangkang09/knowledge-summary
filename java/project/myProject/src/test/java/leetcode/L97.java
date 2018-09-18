package leetcode;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 16:24 2018/9/13
 * @Modified By:
 */
public class L97 {
    public static void main(String[] args) {
        List re = new Solution97().restoreIpAddresses("255220121");
        System.out.println(re.toString());
        System.out.println(1);

    }
}

class Solution97 {
    List<String> result = new ArrayList<>();
    String target;
    public List<String> restoreIpAddresses(String s) {
        target = s;
        resolve(0,0,"");
        return result;
    }

    private void resolve(int index, int count, String string) {
        if(index==target.length()-1&&count==4) {
            result.add(string.substring(0,string.length()-2));
        } else {
            for (int i = index; i < target.length(); i++) {
                String temp = target.substring(index,i+1);
                if(temp.charAt(0)=='0'&&temp.length()>=2) return;
                else if(Integer.valueOf(temp)>=256) return;
                resolve(i+1,count+1,string+temp+".");
            }
        }
    }
}