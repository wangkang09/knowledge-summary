package findJob.zhaoshang;

import java.util.Scanner;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 20:14 2018/9/16
 * @Modified By:
 */
public class Main {
   static int[] array0 = {2,5,6,9};
   static int[] array1 = {0,1,8};
   static int[] array2 = {0,1,2,5,6,8,9};

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int target = sc.nextInt();
        sc.close();

        System.out.println(total(target) - duoyu(target));

    }

    private static int duoyu(int target) {
        return 0;
    }

    private static int total(int target) {
        if(target<10) {
            int count = 0;
            for (int i = 0; i < array1.length; i++) {
                if(array1[i]>target) break;
                else count++;
            }
            return count;
        } else {
            String str = Integer.toString(target);
            int len = str.length();
            int ten = 1;
            for (int i = 0; i < len-1; i++) {
                ten *= 10;
            }
            int zheng = target/ten;
            int yu = target%ten;

            int totalCount = 0;
            for (int i = 0; i < array2.length; i++) {
                if(array2[i]>target) break;
                else totalCount++;
            }

            int yuCount = 0;
            for (int i = 0; i < array1.length; i++) {
                if(array1[i]>target) break;
                else totalCount++;
            }

            for (int i = 0; i < str.length()-1; i++) {
                totalCount *= 7;
                yuCount *= 3;
            }



        }
        return 0;
    }
}
