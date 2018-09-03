package concurrent.consumerAndProduct;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 0:23 2018/9/1
 * @Modified By:
 */
public class Test {
    public static void main(String[] args) {
         LinkedBlockingQueue<Integer> queue = new LinkedBlockingQueue<>(4);

         Test t ;
         while(true) {
             System.out.println(1);
          t =   new Test();
         }



//        try {
//           // queue.take();
//            System.out.println("唤醒了！");
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

    }
}
