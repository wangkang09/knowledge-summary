package findJob.songchao;

import java.util.Scanner;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 19:49 2018/9/29
 * @Modified By:
 */
public class Main4 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();
        int count = 0;
        int result = -1;
        int temp;
        for (int i = 0; i < N; i++) {
            temp = sc.nextInt();
            if(temp == result) {
                count++;
            } else {
                if(count==0) {
                    result = temp;
                    count = 1;
                } else {
                    count --;
                }
            }
        }

        System.out.println(result);
    }
}
