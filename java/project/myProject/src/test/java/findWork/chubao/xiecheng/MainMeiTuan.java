package findWork.chubao.xiecheng;

import java.util.*;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 20:40 2018/9/6
 * @Modified By:
 */
public class MainMeiTuan {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int length = in.nextInt();
        int k = in.nextInt();
        int t = in.nextInt();
        int[] a = new int[length];
        for(int i=0;i<length;i++) {
            a[i] = in.nextInt();
        }

        int count = 0; //第几次进入
        int index0 = 0;//走了几次

        int countA=0;
        Map<Integer,Integer> map = new HashMap<>();
        while(true) {
            if(count==0) {
                if(!map.containsKey(a[index0])) {
                    map.put(a[index0],0);
                } else {
                    int ss = map.get(a[index0]);
                    map.put(a[index0],ss+1);
                }
                index0++;
                if(index0%k==0) {
                    boolean flag = pd(map,t);
                    if(flag) countA++;
                    index0 = 0;
                }
                //添加第count+k个元素
                if(count+k>length) {
                    break;
                }
                if(index0>=length) break;

            } else {
                index0++;
                int ss = map.get(a[count]);
                map.put(a[count],ss-1);//移除第一次

                if(!map.containsKey(a[count+k])) {
                    map.put(a[count+k],0);
                } else {
                    int ss0 = map.get(a[count+k]);
                    map.put(a[count+k],ss0+1);
                }

                if(index0%k==0) {
                    count++;//下一个开始位置
                    boolean flag = pd(map,t);
                    if(flag) countA++;
                    index0 = 0;
                }

                //添加第count+k个元素
                if(count+k>length) {
                    break;
                }
                if(index0>=length) break;

            }

        }
        System.out.println(countA);

    }

    public static boolean pd(Map<Integer,Integer> map,int t) {

        Iterator iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            int val = (int)entry.getValue();
            if(val>=t) return true;
        }
        return false;
    }
}
