package findJob.songchao;

import java.util.Scanner;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 10:50 2018/10/26
 * @Modified By:
 */
public class Main7 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        sc.close();
        tenTo26(n);
    }

    private static void tenTo26(int a) {
        int part_result = a,remainder,temp;
        char  ascii = 'A';
        String s = "",str;
        if(a == 0){
            System.out.println(s);
            return;
        }
        while(part_result != 0){
            temp = part_result;
            part_result = part_result / 26;
            remainder = temp % 26;
            ascii += remainder-1;
            s = ascii + s;
            ascii = 'A';
        };
        System.out.println(s);

    }
}
