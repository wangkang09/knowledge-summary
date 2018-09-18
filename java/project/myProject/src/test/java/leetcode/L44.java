package leetcode;

import java.util.Scanner;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 20:51 2018/9/11
 * @Modified By:
 */
public class L44 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine();
        String p = sc.nextLine();
        sc.close();
        new Solution44().isMatch(s,p);
    }
}

class Solution44 {

    public boolean isMatch(String s, String p) {

        if(s==null||p==null) return false;
        if(s.length()==0 && p.length()==0) return true;
        if(s.length()!=0&&p.length()==0) return false;
        if(s.length()==0&&p.length()!=0) {

            for (int i = 0; i < p.length(); i++) {
                if(p.charAt(i)!='*') {
                    return false;
                }
            }
            return true;
        }

        int maxLen = s.length();
        int sCount = 0;
        for (int i = 0; i < p.length(); i++) {
            if(sCount==maxLen-1) {
                if(s.charAt(sCount)==p.charAt(i)) {
                    for (int j = i+1; j < p.length(); j++) {
                        if(p.charAt(j)!='*') return false;
                    }
                    return true;
                }
            }
            char p0 = p.charAt(i);
            if(p0=='?') sCount++;
            else if(p0!='*') {
                if(s.charAt(sCount)!=p0) return false;
            } else if (p0=='*') {

            }
        }
        if(sCount<maxLen-1)
        return false;
        else return true;
    }
}