package sortAlgorithm;

import java.util.Scanner;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 21:40 2018/9/12
 * @Modified By:
 */
/*
3 7
2 5 3 77 5 23 11
 */
public class kKuaiPai {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int k = sc.nextInt();
        int count = sc.nextInt();
        int[] src = new int[count];
        for (int i = 0; i < count; i++) {
            src[i] = sc.nextInt();
        }
        sc.close();
        int middle = new SolutionKKuaiPai().solution(k,src);
        System.out.println(src[middle]);

    }
}

class SolutionKKuaiPai {
    int[] target;
    int k;
    public int solution(int k,int[] a) {
        target = a;
        this.k = k;
        return resolve(0,target.length-1);
    }

    private int resolve(int begin,int end) {
        int middle = getMiddle(begin,end);
        if(middle==k-1) return middle;
        else if(middle<k-1) {
            return resolve(middle+1,end);
        } else {
            return resolve(begin,middle-1);
        }
    }

    private int getMiddle(int begin, int end) {
        int temp = target[begin];
        while (begin<end) {
            while(begin<end&&temp<=target[end]) end--;
            target[begin] = target[end];
            while (begin<end&&temp>=target[begin]) begin++;
            target[end] = target[begin];
        }
        target[begin] = temp;
        return begin;
    }
}
