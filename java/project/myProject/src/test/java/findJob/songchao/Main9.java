package findJob.songchao;

import java.util.Scanner;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 20:27 2018/11/8
 * @Modified By:
 */
public class Main9 {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String str1 = sc.nextLine();
        String str2 = sc.nextLine();
        sc.close();
        str1 = str1.replace('.',',');
        str2 = str2.replace('.',',');

        String[] version1 = str1.split(",");
        String[] version2 = str2.split(",");

        for (int i = 0; i < version1.length&&i<version2.length; i++) {
            if(Integer.valueOf(version1[i])-Integer.valueOf(version2[i])>0) {
                System.out.println(1);
                return;
            }else if (Integer.valueOf(version1[i])-Integer.valueOf(version2[i])<0){
                System.out.println(-1);
                return;
            }
        }

        if(version1.length>version2.length) {
            System.out.println(1);
            return;
        } else if (version1.length<version2.length) {
            System.out.println(-1);
            return;
        } else System.out.println(0);

    }
}
