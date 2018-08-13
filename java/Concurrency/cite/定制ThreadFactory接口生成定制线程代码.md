# 定制ThreadFactory接口生成定制线程
```java
public class MyThread extends Thread {
    private Date creationDate;
    private Date startDate;
    private Date finishDate;
    
    public MyThread(Runnable target, String name) {
        super(target,name);
        setCreationDate();
    }
    
    @Override
    public void run() {
        setStartDate();
        super.run();
        setFinishDate();
    }
    
    public void setStartDate() {
        startDate = new Date();
    }
    public void setCreationDate() {
        creationDate = new Date();
    }    
    public void setFinishDate() {
        finishDate = new Date();
    }
    
    public long getExecutionTime() {
        return finishDate.getTime() - startDate.getTime();
    }
    
    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append(getName());
        buffer.append(": ");
        buffer.append(" Creation Date: ");
        buffer.append(creationDate);
        buffer.append(" : Running time: ");
        buffer.append(getExecutionTime());
        buffer.append(" Milliseconds.");
        return buffer.toString();
    }
}

public class MyThreadFactory implements ThreadFactory {
    private int counter;
    private String prefix;
    
    public MyThreadFactory(String prefix) {
        this.prefix = prefix;
        counter = 1;
    }
    
    @Override
    public Thread newThread(Runnable r) {
        MyThread myThread = new MyThread(r,prefix+"-"+counter);
        counter++;
        return myThread;
    }
}

public class MyTask implements Runnable {
    @Override
    public void run() {
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
    }
}

public class Main {
    public static void main(String[] args) throws Exception {
        MyThreadFactory myFactory = new MyThreadFactory("MyThreadFactory");
        MyTask task = new MyTask();
        Thread thread = myFactory.newThread(task);
        
        thread.start();
        thread.join();
        
        System.out.printf("Main:Thread information.\n");
        System.out.printf("%s\n",thread);
        System.out.printf("Main:End of the example.\n");
    }
}
```