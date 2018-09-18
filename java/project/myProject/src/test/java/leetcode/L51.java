package leetcode;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 10:31 2018/9/10
 * @Modified By:
 */
public class L51 {
    static List<char[][]> result = new ArrayList<>();
    static int n;
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        n = sc.nextInt();

        boolean[][] flag = new boolean[n][n];

        char[][] target = new char[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                target[i][j] = '.';
            }
        }

        solution(0,flag,target);

        System.out.println();

        for (int i = 0; i < result.size(); i++) {
            char[][] temp = result.get(i);
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    System.out.print(temp[j][k]);
                }
                System.out.println();
            }
        }

    }

    private static void solution(int i, boolean[][] flag, char[][] target) {
        if(i==n) {

            char[][] temp = new char[n][n];
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    temp[j][k] = target[j][k];
                }
            }
            result.add(temp);
            return;
        }

        for (int k = 0; k < flag.length; k++) {
            if(flag[i][k]==false) {
                target[i][k] = 'Q';
                List<Integer> temp = setFlag(i,k,flag,true);
                solution(i+1,flag,target);
                target[i][k] = '.';
                resetFlag(temp,flag,false);
            }
        }
    }

    private static void resetFlag(List<Integer> temp, boolean[][] flag, boolean b) {
        for (int i = 0; i < temp.size(); i+=2) {
            flag[temp.get(i)][temp.get(i+1)] = b;
        }
    }

    private static List<Integer> setFlag(int j, int k, boolean[][] flag,boolean bo) {
        List<Integer> list = new ArrayList<>();
        //设置节点的左边为true
        for (int i = k; i >= 0; i--) {
            if(flag[j][i]==false) {
                list.add(j);
                list.add(i);
                flag[j][i] = bo;
            }
        }
        //设置节点的右边为true
        for (int i = k; i < n; i++) {
            if(flag[j][i]==false) {
                list.add(j);
                list.add(i);
                flag[j][i] = bo;
            }
        }
        //设置节点的下边为true
        for (int i = j; i < n; i++) {
            if(flag[i][k]==false) {
                list.add(i);
                list.add(k);
                flag[i][k] = bo;
            }
        }
        //设置节点的上边为true
        for (int i = j; i >=0 ; i--) {
            if(flag[i][k]==false) {
                list.add(i);
                list.add(k);
                flag[i][k] = bo;
            }
        }

        //左上
        int jj = j;
        int kk = k;
        while (j>=0&&k>=0) {
            if(flag[j][k]==false) {
                list.add(j);
                list.add(k);
                flag[j][k] = bo;
            }
            j--;
            k--;
        }

        j = jj;
        k = kk;
        //右下
        while (j<=n-1&&k<=n-1) {
            if(flag[j][k]==false) {
                list.add(j);
                list.add(k);
                flag[j][k] = bo;
            }
            j++;
            k++;
        }

        j = jj;
        k = kk;
        while(j<=n-1&&k>=0) {
            if(flag[j][k]==false) {
                list.add(j);
                list.add(k);
                flag[j][k] = bo;
            }
            j++;
            k--;
        }

        j = jj;
        k = kk;
        while(k<=n-1&&j>=0) {
            if(flag[j][k]==false) {
                list.add(j);
                list.add(k);
                flag[j][k] = bo;
            }
            j--;
            k++;
        }
        return list;
    }

}
