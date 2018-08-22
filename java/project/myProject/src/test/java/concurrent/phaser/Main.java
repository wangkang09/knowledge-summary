package concurrent.phaser;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 11:25 2018/8/21
 * @Modified By:
 */
public class Main {

    public static void main(String[] args) {
        MyPhaser phaser = new MyPhaser();
        StudentTask[] studentTask = new StudentTask[5];
        for (int i = 0; i < studentTask.length; i++) {
            studentTask[i] = new StudentTask(phaser);
        }

        for (int i = 0; i <studentTask.length+1 ; i++) {
            phaser.register();
        }
        Thread[] threads = new Thread[studentTask.length];
        for (int i = 0; i < studentTask.length; i++) {
            threads[i] = new Thread(studentTask[i], "Student "+i);
            threads[i].start();
        }

        //等待所有线程执行结束
        for (int i = 0; i < studentTask.length; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Phaser has finished:"+phaser.isTerminated());
    }
}