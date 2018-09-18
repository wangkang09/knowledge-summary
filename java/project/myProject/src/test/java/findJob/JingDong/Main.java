package findJob.JingDong;

import java.util.*;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 18:58 2018/9/9
 * @Modified By:
 */
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        //solution1(sc);
        solution2(sc);


    }

    private static void solution2(Scanner sc) {
        int count = sc.nextInt();
        sc.close();
        for (int i = 0; i < count; i++) {
            Random r = new Random();
            int a = r.nextInt(4);
            if(a==0) {
                System.out.println("Yes");
            }else System.out.println("No");
        }
    }

    private static void solution1(Scanner sc) {
        int count = sc.nextInt();
        List<Node> yuan = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            yuan.add(new Node(sc.nextInt(),sc.nextInt(),sc.nextInt()));
        }
        sc.close();
        int sum = 0;
        for (int i = 0; i <count ; i++) {
            Node node = yuan.get(i);
            for (int j = 0; j < count; j++) {
                if(j==i) continue;
                if(yuan.get(j).a>node.a&&yuan.get(j).b>node.b&&yuan.get(j).c>node.c) {
                    sum ++;
                    break;
                }
            }
        }

        System.out.println(sum);
    }


    static class Node{
        private int a;
        private int b;
        private int c;


        public Node(int a, int b, int c) {
            this.a = a;
            this.b = b;
            this.c = c;
        }
    }
}
