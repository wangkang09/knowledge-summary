package test;

import java.util.Scanner;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 16:05 2018/11/22
 * @Modified By:
 */
public class Test00 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        double h = sc.nextDouble();
        double b = sc.nextDouble();
        double c = sc.nextDouble();
        double d = sc.nextDouble();

//        sc.close();
//        double bd = b - d;
//        double acac = h*h - bd*bd;
//        double ac = Math.sqrt(acac);
//
//        System.out.println(c+ac);

//        double a = sc.nextDouble();
//        double b = sc.nextDouble();
//        double c = sc.nextDouble();
//
//        sc.close();
//        System.out.println((a-c)/c);
//        System.out.println((b-c)/c);

        double aa = h -c;
        double bb = b -d ;
double cc = aa*aa + bb*bb;
        System.out.println(Math.sqrt(cc));
    }
}
