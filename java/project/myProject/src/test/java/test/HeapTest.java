package test;

import javax.sound.midi.Soundbank;
import java.util.Scanner;


/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 22:11 2018/9/12
 * @Modified By:
6
3 55 23 67 -34 1
 */
public class HeapTest {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int count = sc.nextInt();
        int[] src = new int[count+1];
        src[0] = Integer.MIN_VALUE;
        for (int i = 1; i < src.length; i++) {
            src[i] = sc.nextInt();
        }
        sc.close();

        new SolutionHeap().solution(src);

    }
}

class SolutionHeap {
    public void solution(int[] target) {
        for (int i = (target.length-1)/2; i >= 1 ; i--) {
            sink(target,i,target.length-1);//建堆
        }
        int N = target.length-1;
        while(N>1) {
            swap(target,1,N--);
            sink(target,1,N);
        }
        for (int i = 1; i < target.length -1; i++) {
            System.out.print(target[i] + " ");
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