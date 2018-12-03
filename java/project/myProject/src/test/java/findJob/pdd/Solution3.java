package findJob.pdd;

import java.util.Scanner;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 20:26 2018/11/13
 * @Modified By:
 */
public class Solution3 {
    static int count = 0;
    static int[][] temp;
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int M = sc.nextInt();
        int N = sc.nextInt();
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                temp[i][j] = sc.nextInt();
            }
        }
        sc.close();
        resolve(new Node(0,0));
        System.out.println(count);
    }

    private static void resolve(Node node) {
        if(node.move(0)) {
            count++;
            resolve(new Node(node.x+1,node.y));
        }
        if(node.move(1)) {
            count++;
            resolve(new Node(node.x-1,node.y));
        }
        if(node.move(2)) {
            count++;
            resolve(new Node(node.x,node.y+1));
        }
        if(node.move(3)) {
            count++;
            resolve(new Node(node.x+1,node.y-1));
        }
    }


    public static class Node {
        int x;
        int y;
        public Node(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public boolean move(int direction) {
            switch (direction) {
                case 0:
                    if(x+1>=temp[0].length) {
                        return false;
                    }
                    if(temp[x+1][y]==0) {
                        return false;
                    }
                    return true;
                case 1:
                    if(x-1<0) {
                        return false;
                    }
                    if(temp[x-1][y]==0) {
                        return false;
                    }
                    return true;
                case 2:
                    if(y+1>=temp.length) {
                        return false;
                    }
                    if(temp[x][y+1]==0) {
                        return false;
                    }
                    return true;
                case 3:
                    if(y-1>=temp.length) {
                        return false;
                    }
                    if(temp[x][y-1]==0) {
                        return false;
                    }
                    return true;
            }
            return true;
        }
    }
}
