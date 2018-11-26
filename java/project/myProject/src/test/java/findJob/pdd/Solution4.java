package findJob.pdd;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 20:52 2018/11/13
 * @Modified By:
 */
public class Solution4 {

    public static void main(String[] args) {
        int MaxNumber = 110;
        Node4[] result = new Node4[MaxNumber];

        for (int i = 0; i < 10; i++) {
            int[] a = createArray();
            add(result,a,i);
        }
        resolve(result);
    }

    /**
     *
     * @Description: 找到此数组中最小区间覆盖所有数组下标的区间，即是最小区间
     *
     * @auther: wangkang
     * @date: 21:05 2018/11/13
     * @param: [result]
     * @return: void
     *
     */
    private static void resolve(Node4[] result) {
        int low = 0;//起始位置
        boolean enough = false;
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < result.length; i++) {
            if(!enough) enough = pushIsTotal(result[i]); //当不足全部是添加
            else {
                enough = popIsTotal(result[low]);//当足够时减去最前面的
                low++;
            }

            if(enough&&min>i-low) {
                min = i-low;
            }
        }

        System.out.println();
    }

    private static boolean popIsTotal(Node4 node4) {
        return true;
    }

    private static boolean pushIsTotal(Node4 node4) {
        return true;
    }

    /**
     *
     * @Description: 用a数组填充result数组
     *
     * @auther: wangkang
     * @date: 21:03 2018/11/13
     * @param: [result, a, i]
     * @return: void
     *
     */
    private static void add(Node4[] result, int[] a, int i) {
        for (int j = 0, jMax = a.length; j < jMax; j++) {
            if(result[a[j]]==null) {
                Set set = new HashSet();
                set.add(i);
                result[a[j]] = new Node4(set);
            } else {
                result[a[j]].set.add(i);
            }
        }
    }

    /**
     *
     * @Description:  创建数组
     *
     * @auther: wangkang
     * @date: 21:03 2018/11/13
     * @param: []
     * @return: int[]
     *
     */
    private static int[] createArray() {
        return new int[]{1,3,4};
    }
}
