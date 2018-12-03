package findJob.songchao;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 20:10 2018/11/8
 * @Modified By:
 */
public class Main8 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String str = sc.nextLine();
        sc.close();

        int[] result = new int[26];
        for (int i = 0; i < str.length(); i++) {
            result[str.charAt(i)-'a']++;
        }


        Arrays.sort(result);
        int a1 = result[25];
        int a2 = result[24];
        int count = (a1+a2)*(a1+a2);
        int i=23;
        for (; i >=0&&result[i]!=0; i--) {
            count += result[i]*result[i];
        }

        System.out.println(count);

    }
}
