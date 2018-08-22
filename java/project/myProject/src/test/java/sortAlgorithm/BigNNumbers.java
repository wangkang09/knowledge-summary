package sortAlgorithm;

import java.util.Random;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 14:12 2018/8/9
 * @Modified By:
 */
public class BigNNumbers {

    public static void insert(int[] a,int temp) {
        int flag = -1;
        for(int i=0;i<a.length;i++) {
            if(temp>a[i]) flag = i;
            else break;
        }

        if(flag!=-1) {
            System.arraycopy(a,1,a,0,flag);
            a[flag] = temp;
        }

    }

    public static void main(String[] args) {
        int N = 10;
        int count = 30;
        int max = 100000;
        Random random = new Random();

        int[] a = new int[N];
        for(int i=0;i<N;i++) {
            a[i] = Integer.MIN_VALUE;
        }

        for(int i=0;i<count;i++) {
            insert(a,random.nextInt(max));
        }

        for(int i=0;i<N;i++){
            System.out.print(a[i] + " ");
        }
        System.out.println();
    }
}

