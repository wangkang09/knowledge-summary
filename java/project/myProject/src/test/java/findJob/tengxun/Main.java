package findJob.tengxun;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 10:56 2018/9/16
 * @Modified By:
 */
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int count = sc.nextInt();
        for (int i = 0; i < count; i++) {
            int a = sc.nextInt();
            int b = sc.nextInt();
            int c = sc.nextInt();
            solution(a,b,c);
        }
        sc.close();

    }

    private static void solution(int a, int b, int c) {
        if(a<b) {
            int temp = a;
            a = b;
            b = temp;

        }
        if(c==0) {
            System.out.println("YES");
            return;
        }
        if(a%b==0) {
            if(c%b==0) {
                System.out.println("YES");
            }else {
                System.out.println("NO");
            }
            return;
        } else {
            int aa = a%b;
            int bb = b - aa;

            if(aa<bb) {
                int temp = aa;
                aa = bb;
                bb = temp;
            }

            if(aa%bb!=0) {
                System.out.println("YES");
                return;
            }else {
                if(c%bb==0) {
                    System.out.println("YES");
                }else {
                    System.out.println("NO");
                }
                return;
            }

        }
    }
}
