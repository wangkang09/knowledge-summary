package concurrent.cookbook.c5_4;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 11:39 2018/8/13
 * @Modified By:
 */
public class Main {
    public static void main(String[] args) {
        ForkJoinPool pool = new ForkJoinPool();

        FolderProcessor system = new FolderProcessor("C;\\Windows","log");
        FolderProcessor apps = new FolderProcessor("C;\\program Files","log");
        FolderProcessor documents = new FolderProcessor("C;\\Documents And Settings","log");
        pool.execute(system);
        pool.execute(apps);
        pool.execute(documents);

        do {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (!system.isDone()||!apps.isDone()||!documents.isDone());

        pool.shutdown();

        system.join();
        apps.join();
        documents.join();

    }
}
