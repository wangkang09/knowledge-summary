package findJob.aiqiyi;

import java.util.Scanner;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 10:39 2018/9/15
 * @Modified By:
 */
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String str = sc.nextLine();
        sc.close();

        int first3 = 0;
        for (int i = 0; i < 3; i++) {
            first3 += Integer.valueOf(str.charAt(i));
        }
        int last3 = 0;
        for (int i = 3; i < 6; i++) {
            last3 += Integer.valueOf(str.charAt(i));
        }

        int cha = first3-last3;

        String first = str.substring(0,3);
        String last = str.substring(3,6);

        if(cha==0) {
            System.out.println(0);
            return;
        }

        if(cha<0) {
            cha = -cha;
            first = str.substring(3,6);
            last =  str.substring(0,3);
        }

        if(cha>0&&cha<10) {
            int count = solution(first,last,cha);
            System.out.println(count);
        }else {
            int count = 0;
            String temp = first;
            while(cha>=10) {
                count++;
                int max = 0;
                for (int i = 0; i < temp.length(); i++) {
                    if(temp.charAt(max)<temp.charAt(i)) {
                        max = i;
                    }
                }
                temp = temp.substring(0,max) + temp.substring(max+1,temp.length());
                cha -= temp.charAt(max)-'0';
            }

            count += solution(temp,last,cha);

            System.out.println(count);
            return;

        }
    }

    private static int solution(String first, String last, int cha) {
        if(hasCha(first,cha)||jiaChaXiaoYu9(last,cha)){
            return 1;
        }else {
            return 2;
        }
    }

    private static boolean jiaChaXiaoYu9(String sub, int cha) {
        for (int i = 0; i < sub.length(); i++) {
            if(sub.charAt(i)-'0'+cha<=9) return true;
        }
        return false;
    }

    private static boolean hasCha(String sub, int cha) {
        for (int i = 0; i < sub.length(); i++) {
            if(sub.charAt(i)-'0'==cha) return true;
        }
        return true;
    }
}
