## lock与lockInterruptibly

* aa.lock操作不会响应中断，最终都会获取到锁
* aa.lockInterruptibly会响应中断，当第一次获取锁时和从park中唤醒时，如果被中断会抛出interrupteException异常，所以要用try/catch/finally块包围，且在finally中释放锁时，要判断当前线程是否持有锁
* aa.lock虽然不会响应中断，但是被中断后，仍然有中断状态，获取锁后，如果调用sleep或wait，都会抛出异常

```java
/**
 * @Description:很关键！使用lockInterruptibly当一个线程已经是中断状态时，尝试获取锁会发生IllegalMonitorStateException
 *当线程被park后，被中断，此线程被唤醒时，会抛出InterruptedException，并从队列里清楚该节点，最后被最外层catch到
 * 但是如果线程在第一次判断线程中断状态之后和还没有被park之前，就获取到了锁，这期间即使被别的线程中断也没影响
 * @Author: wangkang
 * @Date: Created in 22:00 2018/8/22
 * @Modified By:
 */
public class ReentrantLockTest {
    ReentrantLock aa = new ReentrantLock();
   static Thread thread = null;
   static Thread thread1 = null;

    public static void main(String[] args) {
        ReentrantLockTest b = new ReentrantLockTest();

       thread =   new Thread(new Runnable() {
            @Override
            public void run() {
                b.w();
            }
        });
      thread.start();

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread1 =   new Thread(new Runnable() {
            @Override
            public void run() {
                b.w();
            }
        });
        thread1.start();

    }

    public void w() {
            aa.lock();//这里如果用Lock的话，不会产生异常，即使获取锁期间被中断了，不影响获取锁，但是如果被中断了，后面在调用sleep的话，直接抛出异常
/*        try {
            aa.lockInterruptibly();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        try {
            TimeUnit.SECONDS.sleep(30);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            System.out.println(Thread.currentThread()==thread);
            thread1.interrupt();
            aa.unlock();//不管是lock还是lockInterruptibly都要在finally块中解锁
            //如果是lockInterruptibly，要加
            //if(aa.isHeldByCurrentThread()) {
            //    aa.unlock();
            //}
        }
    }
}
```

