```java
aa.lock();
public void lock() {
    sync.lock();
}
//继承了Sync
static final class NonfairSync extends Sync {
    final void lock() {
        //就是因为有这个CAS方法获取锁，才使这个为非公平锁
        if (compareAndSetState(0, 1))           setExclusiveOwnerThread(Thread.currentThread());//为了重入机制设置
        else
            acquire(1);
    }
    protected final boolean tryAcquire(int acquires) {
        return nonfairTryAcquire(acquires);
    }
}
//非公平获取锁
final boolean nonfairTryAcquire(int acquires) {
    final Thread current = Thread.currentThread();
    int c = getState();
    if (c == 0) {
        if (compareAndSetState(0, acquires)) {
            setExclusiveOwnerThread(current);
            return true;
        }
    }
    else if (current == getExclusiveOwnerThread()) {
        int nextc = c + acquires;
        if (nextc < 0) // overflow
            throw new Error("Maximum lock count exceeded");
        setState(nextc);
        return true;
    }
    return false;
}
//CAS失败进入
public final void acquire(int arg) {
    //非公平锁的tryAcquire直接调用nonfairTryAcquire再一次尝试获取锁，这里包含了重入概念
    //再次获取失败后，把自己加入等待队列，然后acquireQueued
    if (!tryAcquire(arg) &&
        acquireQueued(addWaiter(Node.EXCLUSIVE), arg))
		//只有线程被标记为中断状态，acquireQueue才返回true，才到这里来
        //既然走到这一步了，肯定是获取到锁了,且线程被标记为中断状态，
        selfInterrupt();//设置自己为中断状态，自己重写设置为中断状态
}

//这个方法很关键
//当第二次获取锁失败，其已经把自己加入阻塞队列后
//进入for循环，首先第三次获取锁，失败后shouldParkAfterFailedAcquire
//失败后shouldParkAfterFailedAcquire成功后，将自己挂起，等待其他线程唤醒
//唤醒后，进入下次for循环的if中获取锁，当线程是被别人唤醒之后再获取到锁的，返回ture,获取成功进入业务代码
final boolean acquireQueued(final Node node, int arg) {
    boolean failed = true;
    try {
        boolean interrupted = false;     
        for (;;) {
            final Node p = node.predecessor();
            //只有head节点的下一个节点才有资格获取锁
            if (p == head && tryAcquire(arg)) {
                setHead(node);//关键！这样的话头结点一定是null
                p.next = null; // help GC
                failed = false;
                return interrupted;//如果线程没有被标记为中断状态，返回false
            }
            //死循环，直到线程挂起停在parkAndCheckInterrupt方法中，获取获取锁后的线程才继续往下执行
            if (shouldParkAfterFailedAcquire(p, node) &&
                parkAndCheckInterrupt())//如果线程是中断状态，清除中断状态，并进入if条件
                interrupted = true;//到了这里说明该线程已经被标记为中断状态
        }
    } finally {
        if (failed)
            //只有发生异常时会到这里来，应该是线程阻塞状态，被其他线程中断
            //从阻塞队列中移除中断的线程
            cancelAcquire(node);
    }
}
private void setHead(Node node) {
    head = node;
    node.thread = null;
    node.prev = null;
}
//如果当前节点的前驱节点状态是-1，则可以安全挂起，否则删除前驱节点，直到前驱节点状态<0
private static boolean shouldParkAfterFailedAcquire(Node pred, Node node) {
    int ws = pred.waitStatus;
    if (ws == Node.SIGNAL)
        return true;//只有它前面节点是-1时，它才能加入队列，如果前面节点>0，删除；=0，修改为-1
    if (ws > 0) {
        do {
            node.prev = pred = pred.prev;
        } while (pred.waitStatus > 0);
        pred.next = node;
    } else {
        compareAndSetWaitStatus(pred, ws, Node.SIGNAL);
    }
    return false;
}
//挂起当前线程，直到被其他线程唤醒时，返回当前线程状态，并继续往下走
private final boolean parkAndCheckInterrupt() {
    LockSupport.park(this);
    return Thread.interrupted();
}

//同样是继承了Sync
static final class FairSync extends Sync {
    //lock是没有cas
    final void lock() {
        acquire(1);//qcquire方法一样，只是方法里的tryAcquire重写了
    }
    protected final boolean tryAcquire(int acquires) {
        final Thread current = Thread.currentThread();
        int c = getState();
        if (c == 0) {
            //第二个区别
            if (!hasQueuedPredecessors() &&
                compareAndSetState(0, acquires)) {
                setExclusiveOwnerThread(current);
                return true;
            }
        }
        else if (current == getExclusiveOwnerThread()) {
            int nextc = c + acquires;
            if (nextc < 0)
                throw new Error("Maximum lock count exceeded");
            setState(nextc);
            return true;
        }
        return false;
    }
}
//只有队列为空，或者队列第一个线程是当前线程，才返回false，这样这个线程才可以CAS
public final boolean hasQueuedPredecessors() {
    Node t = tail; 
    Node h = head;
    Node s;
    return h != t &&
        ((s = h.next) == null || s.thread != Thread.currentThread());
}
```

```java
//Sync方法
abstract void lock();
final boolean nonfairTryAcquire(int acquires) {...}
protected final boolean tryRelease(int releases) {...}
final ConditionObject newCondition() {...}

//NonfairSync extends Sync 
final void lock() {...};//重写了lock方法
//实现了tryAcquire方法，其中之间调用了父类方法
protected final boolean tryAcquire(int acquires) {
    return nonfairTryAcquire(acquires);
}

//FairSync extends Sync
final void lock() {...};//重写了lock方法
//重写实现了nonfairTryAcquire方法，有一点点区别
protected final boolean tryAcquire(int acquires) {...}

//从阻塞队列中移除中断的线程
private void cancelAcquire(Node node) {
    if (node == null)
        return;
    node.thread = null;//将当前节点赋null
    Node pred = node.prev;
    //关键！移除之前操作不成功，没弄从阻塞队列中删除的节点
    while (pred.waitStatus > 0)
        node.prev = pred = pred.prev;
    Node predNext = pred.next;
    node.waitStatus = Node.CANCELLED;//这是最关键的，即使下面都失败也没关系，下个线程进入是会删除它之前的状态为CANCELLED的节点
    if (node == tail && compareAndSetTail(node, pred)) {
        compareAndSetNext(pred, predNext, null);//失败了证明不是tail了，所以没关系
    } else {
        int ws;
        //if里面功能就是将当前node移除
        if (pred != head &&
            ((ws = pred.waitStatus) == Node.SIGNAL ||
             (ws <= 0 && compareAndSetWaitStatus(pred, ws, Node.SIGNAL))) &&
            pred.thread != null) {
            Node next = node.next;
            if (next != null && next.waitStatus <= 0)
                compareAndSetNext(pred, predNext, next);
        } else {
            unparkSuccessor(node);//移除不成功进入
        }
        node.next = node; // help GC
    }
}
private void unparkSuccessor(Node node) {
    int ws = node.waitStatus;//如果是上面代码进来的肯定是CANCELLED(1)
    if (ws < 0)
        compareAndSetWaitStatus(node, ws, 0);//
    Node s = node.next;
    if (s == null || s.waitStatus > 0) {
        s = null;//只是把s的指针指向null
        //从尾节点开始，遍历到当前节点的下一个节点，使s指向最后一个状态小于0的节点
        for (Node t = tail; t != null && t != node; t = t.prev)
            if (t.waitStatus <= 0)
                s = t;
    }
    if (s != null)//这里s应该是不会为null，只有node是尾节点时，才为null
        LockSupport.unpark(s.thread);//唤醒当前节点后的第一个Signal状态的节点
}
```

```java
public final boolean release(int arg) {
    if (tryRelease(arg)) {//先尝试释放锁，使getState变为0，并且清除独占状态
        Node h = head;
        if (h != null && h.waitStatus != 0)
            unparkSuccessor(h);//如果阻塞队列里有线程，且head状态不是0，则唤醒head的下一个线程
        return true;
    }
    return false;
}
//tryRelease
protected final boolean tryRelease(int releases) {
    int c = getState() - releases;
    //看占有锁的线程是不是当前线程，如果不是抛出异常
    if (Thread.currentThread() != getExclusiveOwnerThread())
        throw new IllegalMonitorStateException();
    boolean free = false;
    if (c == 0) {
        free = true;
        setExclusiveOwnerThread(null);
    }
    setState(c);
    return free;
}



```

