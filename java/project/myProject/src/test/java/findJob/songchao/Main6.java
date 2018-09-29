package findJob.songchao;
import java.util.Scanner;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 20:37 2018/9/29
 * @Modified By:
 */
public class Main6 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();
        int[] array = new int[N];

        for (int i = 0; i < N; i++) {
            array[i] = sc.nextInt();
        }
        sort(array);
        boolean flag = false;
        int gap = 0;
        for (int i = 0; i < N; i+=2) {
            if(i==N-1) {
                solution1(gap,array[i]);
                flag = true;
                return;
            } else {
                if(array[i]==array[i+1]) continue;
                gap = solution0(gap,array[i],array[i+1]);
                if(gap == 0) return;
            }
        }
        if(flag==false)
        System.out.println(gap);

    }

    private static int solution0(int gap, int i, int i1) {
        int temp = i1-i;
        if(temp >= 2*gap) return gap;
        else {
            temp = gap + i - i1;
            if(temp>=0) return temp;
            else return -temp;
        }
    }

    private static void solution1(int gap, int i) {
        int temp = i;
        if(temp >= 2*gap) {
            System.out.println(gap);
        } else {
            temp = gap - temp;
            if(temp>=0) {
                System.out.println(temp);
            } else {
                System.out.println(-temp);
            }

        }

    }



    public static void sort(int[] a){
        doSort(a,0,a.length-1);
    }

    private static void doSort(int[] a,int low,int high){
        if(high<=low) return;
        int middle = getMiddle(a, low, high);
        doSort(a, low, middle-1);
        doSort(a, middle+1, high);
    }

    private static int getMiddle(int[] a,int low,int high){
        int temp = a[low];
        while(low<high){
            while(low<high&&a[high]>=temp) high--;
            a[low] = a[high];
            while(low<high&&a[low]<=temp) low++;
            a[high] = a[low];
        }
        a[low] = temp;
        return low;
    }
}
