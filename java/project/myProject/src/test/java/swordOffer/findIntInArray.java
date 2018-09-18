package swordOffer;

import java.util.Scanner;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 16:48 2018/9/9
 * @Modified By:
 */
public class findIntInArray {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int target = sc.nextInt();
        int row = sc.nextInt();
        int col = sc.nextInt();
        int[][] array  = new int[row][col];

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                array[i][j] = sc.nextInt();
            }
        }

        sc.close();

        Find(target,array);

    }

    public static  boolean Find(int target, int [][] array) {

        int row = 0;
        int col = array[0].length - 1;

        int rowMax = array.length-1;

        while(true) {
            int des = array[row][col];

            if(target>des) {
                row ++;
            }else if(target<des) {
                col --;
            } else return true;

            if(row>rowMax) return false;
            if(col<0) return false;
        }

    }
}
