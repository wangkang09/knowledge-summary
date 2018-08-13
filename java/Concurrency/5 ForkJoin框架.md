# Fork/Join框架

[TOC]

## 定义

* 将ForkJoinPool类看作一个特殊的Executor执行器类型
* 分解操作(Fork)：首先Fork/Join框架需要把大的任务分割成足够小的子任务，如果子任务比较大的话还要对子任务进行继续分割 
* 合并操作(Join)：分割的子任务分别放到双端队列里，然后几个启动线程分别从双端队列里获取任务执行。子任务执行完的结果都放在另外一个队列里，启动一个线程从队列里取数据，然后合并这些数据 

1. ForkJoinTask:我们要使用Fork/Join框架，首先需要创建一个ForkJoin任务。该类提供了在任务中执行fork和join的机制。通常情况下我们不需要直接集成ForkJoinTask类，只需要继承它的子类，Fork/Join框架提供了两个子类： 
   1. RecursiveAction：用于没有返回结果的任务 
   2. RecursiveTask:用于有返回结果的任务 

## Fork/Join模式优缺点及应用场景 

* 注意：分拆的对象过多时，小心一下子把内存撑满了，等待线程的CPU资源释放了，但是线程对象等待时，不会被垃圾机制回收 
* 场景：对于树形结构类型的数据的处理和遍历非常适合



## 典型用法

```java
Result solve(Problem problem) {
 
    if (problem is small) {
 
        directly solve problem
 
    } else {
 
        split problem into independent parts
        fork new subtasks to solve each part
        join all subtasks;//合并所有的任务，如果有必要的话
        compose result from subresults
    }
}
```



### 同步方法

**举例:**我们要对一个静态资源服务器的图片文件目录进行遍历和分析的时候，我们需要递归的统计每个目录下的文件数量，最后汇总，非常适合用分叉／结合框架来处理 

* **将一个任务分成多个子任务，要用一个列表来接收，最后invokeAll(列表)**

```java
public class demo {

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        Integer count = new ForkJoinPool().invoke(new CountingTask(Paths.get("D://fish")));
        
        System.out.printf("D:盘fish下面总文件数量:" + count);
        
        // end
    }
}

// 处理单个目录的任务
class CountingTask extends RecursiveTask<Integer> {

    private Path mDir;
    public CountingTask (Path dir) {
        mDir = dir;
    }


    @Override
    protected Integer compute() {
        
        int count = 0;
        List<CountingTask> subTasks = new ArrayList<>();
        
        // 读取目录dir的子路径。
        try {
            
            DirectoryStream<Path> ds = Files.newDirectoryStream(dir);
            
            for (Path subPath : ds) {
                
                if (Files.isDirectory(subPath, LinkOption.NOFOLLOW_LINKS)) {
                    
                    // 对每个子目录都新建一个子任务
                    subTasks.add(new CountingTask(subPath));
                } else {
                    
                    // 遇到文件,则计数器增加1
                    count ++;
                }
            }
            
            if (!subTasks.isEmpty()) {
                // 在当前的ForkJoinPool上调度所有的子任务
                for (CountingTask subTask : invokeAll(subTasks)) {
                    
                    count += subTask.join();
                }
            }
            
        } catch (IOException ex) {
            return 0;
        }
    }
}
```

**举例：**使用Fork/Join框架首先要考虑到的是如何分割任务，如果希望每个子任务最多执行两个数的相加，那么我们设置分割的阈值是2，由于是4个数字相加，所以Fork/Join框架会把这个任务fork成两个子任务，子任务一负责计算1+2，子任务二负责计算3+4，然后再join两个子任务的结果。因为是有结果的任务，所以必须继承RecursiveTask 

* **一个任务分成两个任务，不需要用列表存储了，直接invokeAll(t1,t2)**

```java
public class CountTask extends RecursiveTask<Integer>{

    private static final int THREAD_HOLD = 2;

    private int start;
    private int end;

    public CountTask(int start,int end){
        this.start = start;
        this.end = end;
    }

    @Override
    protected Integer compute() {
        int sum = 0;
        //如果任务足够小就计算
        boolean canCompute = (end - start) <= THREAD_HOLD;
        if(canCompute){
            for(int i=start;i<=end;i++){
                sum += i;
            }
        }else{
            int middle = (start + end) / 2;
            CountTask left = new CountTask(start,middle);
            CountTask right = new CountTask(middle+1,end);
            //执行子任务
            left.fork();
            right.fork();
            //获取子任务结果
            int lResult = left.join();
            int rResult = right.join();
            sum = lResult + rResult;
        }
        return sum;
    }

    public static void main(String[] args){
        ForkJoinPool pool = new ForkJoinPool();
        CountTask task = new CountTask(1,4);
        Future<Integer> result = pool.submit(task);
        try {
            System.out.println(result.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
```

```java
class Fib extends FJTask {
    static final int threshold = 13;
    volatile int number; // arg/result
 
    Fib(int n) {
        number = n;
    }
 
    int getAnswer() {
        if (!isDone())
            throw new IllegalStateException();
        return number;
    }
 
    public void run() {
        int n = number;
        //可以考虑在这里加个hashMap，如果有值直接返回！
        //if((Integer a = map.get(n))!=null) return a;
        if (n <= threshold) // granularity ctl
            number = seqFib(n);
        else {
            Fib f1 = new Fib(n - 1);//这样fib(13)和fib(12)重复算了好多次！！
            Fib f2 = new Fib(n - 2);
            coInvoke(f1, f2);
            number = f1.number + f2.number;
            //map.put(f1+1,number);
        }
    }
 
    public static void main(String[] args) {
        try {
            int groupSize = 2; // for example
            FJTaskRunnerGroup group = new FJTaskRunnerGroup(groupSize);
            Fib f = new Fib(35); // for example
            group.invoke(f);
            int result = f.getAnswer();
            System.out.println("Answer: " + result);
        } catch (InterruptedException ex) {
        }
    }
 
    int seqFib(int n) {
        if (n <= 1) return n;
        else return seqFib(n − 1) + seqFib(n − 2);
    }
}
```

### 异步方法

```java
public class FolderProcessor extends RecursiveTask<List<String>> {
    private static final long serialVersionUID = 1L;
    private String path;
    private String extension;

    public FolderProcessor(String path, String extension) {
        this.path = path;
        this.extension = extension;
    }

    @Override
    protected List<String> compute() {
        List<String> list = new ArrayList<>();
        List<FolderProcessor> tasks = new ArrayList<>();

        File file = new File(path);
        File[] content = file.listFiles();

        if(content != null) {
            for (int i = 0; i < content.length; i++) {
                if(content[i].isDirectory()) {
                    FolderProcessor task = new FolderProcessor(content[i].getAbsolutePath(),extension);

                    task.fork();//异步
                    tasks.add(task);
                } else {
                    if(checkFile(content[i].getName())) {
                        list.add(content[i].getAbsolutePath());//这里直接插入当前线程的列表
                    }
                }
            }
        }

        addResultsFromTasks(list,tasks);

        return list;
    }

    private boolean checkFile(String name) {
        return name.endsWith(extension);
    }

    private void addResultsFromTasks(List<String> list, List<FolderProcessor> tasks) {
        for (FolderProcessor item : tasks) {
            //这里，要将所有字任务的返回的列表，全部加进父任务中！！
            list.addAll(item.join());//这里线程等待，就可以使用工作窃取算法了
        }
    }

}
```

```java
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
```



## 应用

| 程序                     | 描述                                                         |
| ------------------------ | ------------------------------------------------------------ |
| `Fib`（菲波那契数列）    | 如第2节所描述的`Fibonnaci`程序，其中参数值为47阀值为13       |
| `Integrate`（求积分）    | 使用递归高斯求积对公式 [![(2 \cdot i - 1) \cdot x ^ {(2 \cdot i - 1)}](https://camo.githubusercontent.com/9344b9219ef6e077f9ac9f7d423be256107d06da/687474703a2f2f63686172742e676f6f676c65617069732e636f6d2f63686172743f6368743d7478266368663d62672c732c30303030303030302663686c3d253238322b25354363646f742b692b2d2b312532392b25354363646f742b782b2535452b253742253238322b25354363646f742b692b2d2b31253239253744)](https://camo.githubusercontent.com/9344b9219ef6e077f9ac9f7d423be256107d06da/687474703a2f2f63686172742e676f6f676c65617069732e636f6d2f63686172743f6368743d7478266368663d62672c732c30303030303030302663686c3d253238322b25354363646f742b692b2d2b312532392b25354363646f742b782b2535452b253742253238322b25354363646f742b692b2d2b31253239253744) 求-47到48的积分，**i** 为1到5之间的偶数 |
| `Micro`（求微分）        | 对一种棋盘游戏寻找最好的移动策略，每次计算出后面四次移动     |
| `Sort`（排序）           | 使用合并/快速排序算法对1亿数字进行排序（基于`Cilk`算法）     |
| `MM`（矩阵相乘）         | 2048 X 2048的`double`类型的矩阵进行相乘                      |
| `LU`（矩阵分解）         | 4096 X 4096的`double`类型的矩阵进行分解                      |
| `Jacobi`（雅克比迭代法） | 对一个4096 X 4096的`double`矩阵使用迭代方法进行矩阵松弛，迭代次数上限为100 |

## 同步和异步用法

* 采用同步方法时，发送给Fork/Join线程池的方法直到任务执行完成后才返回结果
* 采用异步方法时，发送任务给执行器的方法将立即返回结果，但是任务仍能够继续执行
* 采用同步方法（如：invoke,invokeAll）时，任务被挂起，直到任务被发送到Fork/Join线程池中执行完成，这种方式允许ForkJoinPool类采用**工作窃取算法**来**分配一个新的任务给在执行休眠任务的工作者线程**
* 而采用异步操作（fork）时，任务将继续进行，这是因为**当前线程还没有休眠**，所以不能采用工作窃取算法，**只有当当前线程调用join或get方法来等待任务结束时**，又可以使用工作窃取算法了
* execute(Runnabletask)：这个方法发送一个Runnable任务给ForkJoinPool类，使用Runnable对象时，ForkJoinPool类就不能使用工作窃取算法了，ForkJoinPool仅在使用ForkJoinTask类时，采用工作窃取算法



