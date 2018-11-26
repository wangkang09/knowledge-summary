package findJob.pdd;

import java.util.*;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 19:56 2018/11/13
 * @Modified By:
 */
public class Solution1 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int M = sc.nextInt();
        int N = sc.nextInt();
        sc.close();
        int[][] temp = new int[M][N];
        System.out.println(temp.length);

        for (int i = 0; i < M; i++) {
            for (int j = new Random().nextInt(N); j < N; j++) {
                temp[i][j] = 1;
            }
        }
        resovle(temp,M,N);
    }

    private static void resovle(int[][] temp, int M, int N) {
        int Max = 0;
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                if(temp[i][j]==1) {
                    if(N-j>Max) {
                        Max = N-j;
                        list = new ArrayList<>();
                        list.add(i+1);
                    } else if(N-j==Max) {
                        list.add(i+1);
                    }
                    break;
                }
            }
        }

        for(Integer a: list) {
            System.out.println("["+a+","+Max+"]");
        }
    }
}
