package thinkInJava.test;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 10:55 2018/10/2
 * @Modified By:
 */
public class TestLable {
    public static void main(String[] args) {
        lable1 :
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                System.out.printf("%d,%d ",i,j);
                if(i==2&&j==1) {
                    System.out.println();
                    continue lable1;
                }
                if(i==5&&j==4) {
                    System.out.println();
                    break lable1;
                }
            }
            System.out.println();
        }

    }
}
