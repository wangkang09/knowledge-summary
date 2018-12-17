package cache;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 15:51 2018/11/28
 * @Modified By:
 */
public class L1CacheMiss {
    private static final int RUNS = 10;
    private static final int DIMENSION_1 = 100;
    private static final int DIMENSION_2 = 62;
    static long time0;
    static long time1;

    private static long[][] longs;

    public static void main(String[] args) throws Exception {
        //testCacheMiss();
        //test2DTime();
        for (int i = 0; i < 100; i++) {
            test2DTime();
        }
        System.out.println(time0-time1);
    }

    private static void test2DTime() {
        long start0 = System.nanoTime();


        longs = new long[DIMENSION_1][DIMENSION_2];
        for (int i = 0; i < DIMENSION_1; i++) {
            //longs[i] = new long[DIMENSION_2];
            for (int j = 0; j < DIMENSION_2; j++) {
                longs[i][j] = 0L;
            }
        }
         time0 += System.nanoTime() - start0;

        long start1 = System.nanoTime();
        longs = new long[DIMENSION_1][];
        for (int i = 0; i < DIMENSION_1; i++) {
            longs[i] = new long[DIMENSION_2];
            for (int j = 0; j < DIMENSION_2; j++) {
                longs[i][j] = 0L;
            }
        }
        time1 += System.nanoTime() - start1;
    }

    private static void testCacheMiss() {
        longs = new long[DIMENSION_1][DIMENSION_2];
        long start0 = System.nanoTime();
        for (int i = 0; i < DIMENSION_1; i++) {
            //longs[i] = new long[DIMENSION_2];
            for (int j = 0; j < DIMENSION_2; j++) {
                longs[i][j] = 0L;
            }
        }
        System.out.println(System.nanoTime()-start0);
        System.out.println("starting....");

        final long start = System.nanoTime();
        long sum = 0L;
        //先遍历2维，再遍历1维，13s
        for (int r = 0; r < RUNS; r++) {
            for (int j = 0; j < DIMENSION_2; j++) {
                for (int i = 0; i < DIMENSION_1; i++) {
                    sum += longs[i][j];
                }
            }

            //先遍历1维，再遍历2维，1s
//            for (int i = 0; i < DIMENSION_1; i++) {
//                for (int j = 0; j < DIMENSION_2; j++) {
//                    sum += longs[i][j];
//                }
//            }
        }
        System.out.println("duration = " + (System.nanoTime() - start));
    }
}