package test;

import org.apache.tomcat.util.collections.SynchronizedQueue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 13:46 2018/8/13
 * @Modified By:
 */
public class Test {


    public static void main(String[] args) {
        LinkedBlockingQueue<Integer>  bb = new LinkedBlockingQueue<>(1);
        SynchronizedQueue b2 = new SynchronizedQueue();
        b2.poll();
        try {
            bb.put(1);
            bb.put(2);
            bb.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<String> aaa = new ArrayList<>();
        aaa.add("1");
        aaa.remove(0);
        boolean a = true;
        String aa = "a";
        aa.intern();
        while (true){
            a = false;
        }

    }
}

class SleepThread implements Runnable{


   public SleepThread(){
   }

   public void run(){

   }

}
