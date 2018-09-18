package findJob.xunlei;

import java.util.Scanner;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 19:15 2018/9/12
 * @Modified By:
 */
public class Main {
    static int count = 0;
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
//        int N = sc.nextInt();
//        gouGu(N);
        int red = sc.nextInt();
        int black = sc.nextInt();
        sc.close();
        System.out.println( maxSum(red,black));
        //System.out.println(count);
    }

    private static int maxSum(int red, int black) {
        //中间的3个是红的
        int maxX = -1;
        int result = 0;
        for (int x = -3; x < 4; x++) {
            if((3+x)*red+(4-x)*black<0) {
                maxX = x;
            }
        }
        if(maxX<0) {
            result = 2*((3+maxX)*red+(4-maxX)*black) + (3+maxX)*red - maxX*black;
        } else {
            result = 2*((3+maxX)*red+(4-maxX)*black) + 3*red;
        }

        return result;

    }

    private static void gouGu(int n) {
        for (int a = 1; a <= n; a++) {
            for (int b = a+1; b <= n; b++) {
                for (int c = b+1; c <= n; c++) {
                    if(a*a+b*b==c*c) {
                        if(isHuZhi3(a,b,c)) count++;
                    }
                }
            }
        }
    }

    private static boolean isHuZhi3(int a, int b, int c) {
        if(isHuZhi2(a,b)&&isHuZhi2(a,c)&&isHuZhi2(b,c)) return true;
        return false;
    }

    private static boolean isHuZhi2(int a, int b) {
        if(a < b) {
            int tmp = a;
            a = b;
            b = tmp;
        }
        int c;
        while((c = a % b) != 0) {
            a = b;
            b = c;
        }
        if(b==1) return true;
        else return false;
    }
}
