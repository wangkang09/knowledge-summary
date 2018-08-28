package sortAlgorithm;

import java.util.Random;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 14:12 2018/8/9
 * @Modified By:
 */
public class BigNNumbers {

    public static void sink(int[] a,int k,int aMax) {
        while(2*k<=aMax) {
            int j = 2*k;
            if(j<aMax&&a[j]>a[j+1]) j++;//取得值是最大的节点下标
            if(a[k]<=a[j]) return;//如果比最大的还大，说明已经平衡了
            swap(a,k,j);//否则交换最大节点值
            k = j;//在以该节点往下平衡
        }
    }

    public static void swap(int[] a, int i, int j) {
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }
    public static void main(String[] args) {
        int N = 10;
        int count = 3000000;
        int max = 100000;
        Random random = new Random();

        int[] a = new int[N];
        for(int i=0;i<N;i++) {
            a[i] = Integer.MIN_VALUE;
        }
        int aMax = N - 1;
        //循环建堆
        for(int i=0;i<count;i++) {
            int temp = random.nextInt(max);
            if(temp>a[1]) {
                a[1] = temp;
                sink(a,1,aMax);
            }
        }

        while(aMax>1) {
            swap(a,1,aMax--);//将最小的放到最后
            sink(a,1,aMax);
        }

        for(int i=0;i<N;i++){
            System.out.print(a[i] + " ");
        }
        System.out.println();
    }
}