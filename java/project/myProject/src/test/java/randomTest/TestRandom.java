package randomTest;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 17:20 2018/11/27
 * @Modified By:
 */
public class TestRandom {
    static Random seed = new Random();
    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            createInt();

        }

    }

    private static void createInt() {
        System.out.println(ThreadLocalRandom.current().nextInt(20,100));
    }
}
