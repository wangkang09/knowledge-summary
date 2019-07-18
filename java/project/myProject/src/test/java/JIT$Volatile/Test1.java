package JIT$Volatile;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 17:08 2018/12/27
 * @Modified By:
 */
class test extends Thread
{
    private volatile boolean[] isRunning = new boolean[10];

    public void setRunning(boolean isRunning)
    {
        this.isRunning[0]=isRunning;

    }

    public void run()
    {
        System.out.println("进入run了");
        while (isRunning[0]== true){};
        System.out.println("线程被停止了");
    }
}

public class Test1 {
    public static void main(String[] args)
    {
        try
        {
            test t = new test();
            t.setRunning(true);
            t.start();
            Thread.sleep(1000);
            t.setRunning(false);
            System.out.println("已赋值为false");
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}


