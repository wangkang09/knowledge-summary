package findJob.wangyi;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 15:33 2018/9/28
 * @Modified By:
 */
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int k = sc.nextInt();
        sc.nextLine();
        String str = sc.nextLine();


        node[] array = new node[26];
        for (int i = 0; i < 26; i++) {
            array[i] = new node();
        }
        for (int i = 0, iMax = str.length(); i < iMax; i++) {
            array[str.charAt(i) - 'A'].count++;
        }

        Arrays.sort(array, new Comparator<node>() {
            @Override
            public int compare(node o1, node o2) {
                return o2.count - o1.count;
            }
        });

        int score = 0;
        int count;
        for (int i = 0; i < 26; i++) {
            count = array[i].count;
            if (count < k) {
                score += count * count;
                k -= count;
            } else {
                score += k * k;
                break;
            }

        }
        System.out.println(score);
    }
    public static class node {
        private int count = 0;
    }
}
