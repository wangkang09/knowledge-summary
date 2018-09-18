package findJob.A360;

import java.util.Scanner;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 19:24 2018/9/17
 * @Modified By:
 */
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int count = sc.nextInt();
        if(count<=2) {
            System.out.println(-1);
            return;
        }
        int max = 0;
        int sum = 0;
        int temp = 0;
        for (int i = 0; i < 3; i++) {
            temp = sc.nextInt();
            if(temp>max) max = temp;
            sum += temp;
        }

        if(sum-max>max) {
            System.out.println(3);
            return;
        }

        for (int i = 3; i < count; i++) {
            temp = sc.nextInt();
            if(temp>max) {
                if(sum>temp) {
                    System.out.println(i+1);
                    return;
                }
                sum += temp;
                max = temp;
            } else if(temp==max) {
                System.out.println(i+1);
                return;
            }else {
                sum -= max;
                sum += temp;

                if(sum>max) {
                    System.out.println(i+1);
                    return;
                }

                sum += max;
            }
        }

        System.out.println(-1);

    }

}


