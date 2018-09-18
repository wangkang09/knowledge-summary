package otherAlgorithm;

import java.util.Scanner;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 16:04 2018/9/13
 * @Modified By:
 */
public class feibonaqie {
    public static int f1(int n){                     //方法一：递归算法，自底向上
        if(n<=2)return 1;                            //如果是求前两项，直接返回就可
        else return f1(n-1)+f1(n-2);
    }
    public static int f2(int n){                     //方法二：循环算法，自上而下
        if(n<=2)return 1;                            //如果是求前两项，直接返回就可
        int a1=1,a2=1,a3;
        for(int i=3;i<=n;i++)
        {
            a3=a1+a2;
            a1=a2;
            a2=a3;
        }
        return a2;
    }
    public static int[][] f3(int n){                 //方法三：矩阵链相乘算法，采用递归实现
        int a[][]={{1,1},{1,0}};                     //定义基矩阵
        int b[][];                                   //存储子方法的结果
        int c[][]=new int[2][2];                     //存储最后计算结果
        int d[][]=new int[2][2];                     //存储中间计算结果
        if((n)<=1)return  a;                         //如果次方小等于1直接返回
        else if((n) %2==1)
        {b=f3((n-1)/2);

            d[0][0]=b[0][0]*b[0][0]+b[0][1]*b[1][0];
            d[0][1]=b[0][0]*b[0][1]+b[0][1]*b[1][1];
            d[1][0]=b[1][0]*b[0][0]+b[1][1]*b[1][0];
            d[1][1]=b[1][0]*b[0][1]+b[1][1]*b[1][1];

            c[0][0]=d[0][0]*a[0][0]+d[0][1]*a[1][0];
            c[0][1]=d[0][0]*a[0][1]+d[0][1]*a[1][1];
            c[1][0]=d[1][0]*a[0][0]+d[1][1]*a[1][0];
            c[1][1]=d[1][0]*a[0][1]+d[1][1]*a[1][1];

        }
        else  {
            b=f3((n)/2);

            c[0][0]=b[0][0]*b[0][0]+b[0][1]*b[1][0];
            c[0][1]=b[0][0]*b[0][1]+b[0][1]*b[1][1];
            c[1][0]=b[1][0]*b[0][0]+b[1][1]*b[1][0];
            c[1][1]=b[1][0]*b[0][1]+b[1][1]*b[1][1];
        }
        return c;
    }
    public static void main(String[] args) {
        // TODO 自动生成的方法存根
        Scanner scan=new Scanner(System.in);
        int n=scan.nextInt();
        System.out.println("方法一："+f1(n));
        System.out.println("方法二："+f2(n));
        int a[][]=f3(n-1);                            //因为是要求矩阵{{1,0},{1,0}}的n-1次方
        System.out.println("方法三："+a[0][0]);

    }
}
