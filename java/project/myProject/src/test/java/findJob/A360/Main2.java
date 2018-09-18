package findJob.A360;

import java.util.Scanner;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 19:37 2018/9/17
 * @Modified By:
 */
public class Main2 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int count = sc.nextInt();
        for (int i = 0; i < count; i++) {
            int jin = sc.nextInt();
            long l = sc.nextLong();
            long r = sc.nextLong();
            solution(jin,l,r);

        }
        sc.close();
    }

    private static void solution(int jin, long l, long r) {
        int temp = jin-1;
        long sum = temp;
        if(temp>r) {
            System.out.println(l);
            return;
        } else if(temp==r){
            System.out.println(r);
            return;
        }
        boolean flag = false;
        if(temp>=l) flag = true;
        
        for (int i = 1; ; i++) {
            long t = sum;
            sum += temp * Math.pow(jin,i);
            if(sum>r&&flag) {
                System.out.println(t);
                return;
            }else if(sum==r) {
                System.out.println(r);
                return;
            }
        }
    }
}
