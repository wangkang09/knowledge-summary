package test;

import java.util.Random;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 22:09 2018/9/12
 * @Modified By:
 */
public class TopKHeap {
    public static void main(String[] args) {
        new SolutionTopKHeap().solution();
    }
}

class SolutionTopKHeap {
    public void solution() {
        int[] target = new int[21];
        for (int i = 0; i < target.length; i++) {
            target[i] = Integer.MIN_VALUE;
        }
        Random random = new Random();
        for (int i = 0; i < 30; i++) {
            insert(target,random.nextInt(300));
        }

        int N = target.length-1;
        while(N>1) {
            swap(target,1,N--);
            sink(target,1,N);
        }


        for (int i = 0; i < target.length; i++) {
            System.out.print(target[i] + " ");
        }
    }

    private void insert(int[] target, int i) {
        if(i>target[1]) {
            target[1] = i;
            sink(target,1,target.length-1);
        }
    }

    private void sink(int[] target, int i, int max) {
        while(2*i<=max) {
            int tar = target[i];
            int j = 2*i;
            if(j<max&&target[j]>target[j+1]) j = j+1;//大于最小的
            if(tar>target[j]) swap(target,i,j);
            i = j;
        }
    }

    private void swap(int[] target, int i, int j) {
        int temp = target[i];
        target[i] = target[j];
        target[j] = temp;
    }
}
