package findWork.chubao.xiecheng;

import java.util.*;

public class MainAli {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int len =  in.nextInt();
        int[] a = new int[len];

        for (int i = 0; i < len; i++) {
            a[i] = in.nextInt();
        }

        if(len < 3) {
            System.out.println(0);
            return;
        }

        if(len == 3) {
            if(pd(a)) System.out.println(1);
            else System.out.println(0);
            return;
        }

        pddb(a);
        pddc(a);

    }

    public static boolean pd(int[] a) {
        if(a[0]-a[1] == a[1] - a[2]) {
            return true;
        }

        if(a[0]!=0&&a[2]!=0&&a[1]!=0) {
            if(a[0]/a[1]==a[1]/a[2]) return true;
        }
        return false;
    }

    public static void pddb(int[] a) {
        List<Integer> list = new ArrayList<>();
        list.add(a[0]);
        Map<Integer,List<Integer>> map = new HashMap<>();
        map.put(a[0],list);

//        while (true) {
//            for (int i = 0; i < a.length; i++) {
//
//            }
//        }
    }

    public static void pddc(int[] a) {

        boolean total = true;
        for (int i = 0; i < a.length; i++) {
            if(i+2<a.length) {
                if(a[i]-a[i+1]!=a[i+1]-a[i+2]) {
                    total = false;
                    break;
                }
            }
        }

        if(total) {
            int count = a.length/3;
            int yu = count;

            int tt = 0;
            if(count-5<=0) {
                System.out.println(1);
                return;
            } else {
                while(count-->=0) {
                    tt += (a.length - 3*count+1)*(count-1);
                }
                tt = a.length - 5;//2个子数组
            }
        }


    }
}
