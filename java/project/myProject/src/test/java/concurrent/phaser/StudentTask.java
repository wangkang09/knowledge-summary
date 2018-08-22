package concurrent.phaser;

import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 11:24 2018/8/21
 * @Modified By:
 */
public class StudentTask implements Runnable {

    private Phaser phaser;

    public StudentTask(Phaser phaser) {
        this.phaser = phaser;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName()+"到达考试");
        phaser.arriveAndAwaitAdvance();

        System.out.println(Thread.currentThread().getName()+"做第1题时间...");
        doExercise1();
        System.out.println(Thread.currentThread().getName()+"做第1题完成...");
        phaser.arriveAndAwaitAdvance();

        System.out.println(Thread.currentThread().getName()+"做第2题时间...");
        doExercise2();
        System.out.println(Thread.currentThread().getName()+"做第2题完成...");
        phaser.arriveAndAwaitAdvance();

        System.out.println(Thread.currentThread().getName()+"做第3题时间...");
        doExercise3();
        System.out.println(Thread.currentThread().getName()+"做第3题完成...");
        phaser.arriveAndAwaitAdvance();
    }

    private void doExercise1() {
        long duration = (long)(Math.random()*10);
        try {
            TimeUnit.SECONDS.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void doExercise2() {
        long duration = (long)(Math.random()*10);
        try {
            TimeUnit.SECONDS.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void doExercise3() {
        long duration = (long)(Math.random()*10);
        try {
            TimeUnit.SECONDS.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
