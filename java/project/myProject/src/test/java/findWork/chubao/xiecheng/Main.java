package findWork.chubao.xiecheng;



import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;


public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int count = in.nextInt();
        int flag = in.nextInt();

        Set<Integer> set = new TreeSet<>();
        for(int i=0;i<count;i++) {
            int a = in.nextInt();
            int b = in.nextInt();
            if(b<=flag) {
                int c = in.nextInt();
                if(c>=flag) {
                    set.add(a);
                }
            }else {
                in.nextInt();
            }
        }

        if(set.size()==0){
            System.out.println("null");
        }else {
            for (int i = 0,iMax = set.size(); i < iMax; i++) {
                System.out.println(((TreeSet<Integer>) set).pollFirst());
            }
        }



    }
}
