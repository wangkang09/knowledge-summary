package findJob.liudui;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 19:37 2018/12/13
 * @Modified By:
 */
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int count = sc.nextInt();
        for (int i = 0; i < count; i++) {
            int rs = sc.nextInt();
            int pj = sc.nextInt();
            int[] re = new int[rs];
            int sum = 0;
            for (int j = 0; j < rs; j++) {
                re[j] = sc.nextInt();
                sum += re[j];
            }
            resolve(pj,re,sum);
        }
    }

    private static void resolve(int pj, int[] re,int sum) {
        Arrays.sort(re);
        int total = pj*re.length;
        int count = 0;
        while (sum<total) {
            sum += 100 - re[count];
            count++;
        }
        System.out.println(count);
    }
}
