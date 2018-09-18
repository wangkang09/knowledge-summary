package leetcode;

import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 17:18 2018/9/9
 * @Modified By:
 */
public class L39 {
    static int[] candidates;
    static int target;
    static List<List<Integer>> result = new ArrayList<>();
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int count = sc.nextInt();
        target = sc.nextInt();

        candidates = new int[count];
        for (int i = 0; i < count; i++) {
            candidates[i] = sc.nextInt();
        }
        sc.close();
        ForkJoinPool pool = ForkJoinPool.commonPool();
        pool.invoke(new SumTask(0,0,Collections.EMPTY_LIST));
        pool.shutdown();
        try {
            pool.awaitTermination(1,TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(1);
        System.out.println(result.toString());


    }


  static  class SumTask extends RecursiveAction {

        private int sum;
        private int index;
        private List<Integer> list;
        private List<SumTask> tasks = new ArrayList<>();

        public SumTask(int sum, int index,List<Integer> list) {
            this.sum = sum;
            this.index = index;
            this.list = list;
        }

        @Override
        protected void compute() {
            for (int i = index; i < candidates.length; i++) {
                int sumTemp = sum+candidates[i];
                if(sumTemp==target) {
                    List<Integer> temp = new ArrayList<>(list);
                    temp.add(candidates[i]);
                    result.add(temp);
                    break;
                }else if(sumTemp<target) {
                    List<Integer> temp = new ArrayList<>(list);
                    temp.add(candidates[i]);
                    tasks.add(new SumTask(sumTemp,i,temp));
                } else break;
            }
            if(tasks.size()>0)
            invokeAll(tasks);
        }
    }


}
