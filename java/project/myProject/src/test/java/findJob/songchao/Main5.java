package findJob.songchao;

import java.util.Scanner;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 20:01 2018/9/29
 * @Modified By:
 */
public class Main5 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();
        int[] target = new int[N];
        for (int i = 0; i < N; i++) {
            target[i] = sc.nextInt();
        }
        new Solution().solution(target,N-1);

    }
}

class Solution {
    int end;
    int[] target;
    boolean flag;
    public void solution(int[] target,int end) {
        this.end = end;
        this.target = target;
        resolve(2,0);
    }

    private void resolve( int index,int count) {
        if(index>=end) {
            System.out.println(count);
            flag = true;
            return;
        }
        for (int i = index; i <= end; i++) {
            int temp = target[index];
            int max = index + temp;
            int tempIndex = index;
            for (int j = 1; j <= temp; j++) {
                if(index + j >=end)  {
                    System.out.println(++count);
                    flag = true;
                    return;
                }
                if(target[j+index]+j>=max) {
                    max = target[j+index]-j;
                    tempIndex = index+j;
                }
            }

            resolve(tempIndex,++count);
            if(flag) return;
        }
    }
}
