## tryAcquire(arg) ##
    final boolean nonfairTryAcquire(int acquires) {//这里acquires是1
    	//获取当前线程
    	final Thread current = Thread.currentThread();
    	//获取state变量值
    	int c = getState();
    	if (c == 0) { //没有线程占用锁
    		if (compareAndSetState(0, acquires)) {
	    		//占用锁成功,设置独占线程为当前线程
	    		setExclusiveOwnerThread(current);
	    		return true;
    		}
	    } else if (current == getExclusiveOwnerThread()) { //	当前线程已经占用该锁
		    int nextc = c + acquires;
		    if (nextc < 0) // overflow
		    	throw new Error("Maximum lock count exceeded");
		    // 更新state值为新的重入次数
		    setState(nextc);
		    return true;
	    }
	    //获取锁失败
	    return false;
    }

* 检查state字段
	* 若为0，表示锁未被占用，尝试占用，成功返回true，获取锁，直接执行lock.lock()后的代码
	* 若不为0，检查是否是当前锁
		* 如果是，重入次数+1，更新重入次数，返回true，获取锁，直接执行lock.lock()后的代码
		* 如果不是，则返回false，获取锁失败

## addWaiter(Node.EXCLUSIVE) ##
    /**
     * 将新节点和当前线程关联并且入队列
     * @param mode 独占/共享
     * @return 新节点
     */
    private Node addWaiter(Node mode) {
	    //初始化节点,设置关联线程和模式(独占 or 共享)
	    Node node = new Node(Thread.currentThread(), mode);
	    // 获取尾节点引用
	    Node pred = tail;
	    // 尾节点不为空,说明队列已经初始化过
	    if (pred != null) {
		    node.prev = pred;//将新节点的前驱节点指向尾节点**指向的地址**
		    // 设置新节点为尾节点
		    if (compareAndSetTail(pred, node)) {//先将tail指针指向新节点，因为该开始tail和pred都指向旧的节点的，成功后，别的线程的tail和pred就不一样了，所以CAS不成功，到enq()中！
			    pred.next = node;//形成双向不急
			    return node;
		    }
	    }
	    // 当有多个线程同时遇到pred==null时，进入enq(node)处理
	    enq(node);
	    return node;
    }

* **将新节点和当前线程关联并且入队列**
* 当有多个线程同时遇到pred==null时，进入enq(node)处理  

---
    /**
     * 初始化队列并且入队新节点
     */
    private Node enq(final Node node) {
	    //开始自旋
	    for (;;) {
		    Node t = tail;
		    if (t == null) { // Must initialize
			    // 如果tail为空,则新建一个head节点,并且tail指向head
			    if (compareAndSetHead(new Node()))
			    	tail = head;
		    } else {
			    node.prev = t;
			    // tail不为空,将新节点入队
			    if (compareAndSetTail(t, node)) {
				    t.next = node;
				    return t;
		    	}
		    }
	    }
    }

![CAS队列操作](https://raw.githubusercontent.com/wangkang09/shein-note/master/distributedSystem/img/CAS%E9%98%9F%E5%88%97%E6%93%8D%E4%BD%9C.png "CAS队列操作")

* 经典的**自旋+CAS组合**来实现非阻塞的原子操作
* 当有多个线程都已经取到tail后
* 第一个开始CAS的线程成功，因为pred==tail
* 这样**tail就变了**
* **后面的线程CAS就不会成功**
* 同理head操作

## acquireQueued(final Node node, int arg) ##
    /**
     * 已经入队的线程尝试获取锁
     */
    final boolean acquireQueued(final Node node, int arg) {//node是addWaiter返回的新节点，arg为1
	    boolean failed = true; //标记是否成功获取锁
	    try {
		    boolean interrupted = false; //标记线程是否被中断过
		    for (;;) {
			    final Node p = node.predecessor(); //获取前驱节点
			    //如果前驱是head,即该结点已成老二，那么便有资格去尝试获取锁
			    if (p == head && tryAcquire(arg)) {
				    setHead(node); // 获取成功,将当前节点设置为head节点
				    p.next = null; // 原head节点出队,在某个时间点被GC回收
				    failed = false; //获取成功
				    return interrupted; //返回是否被中断过
			    }
			    // 判断获取失败后是否可以挂起,若可以则挂起
			    if (shouldParkAfterFailedAcquire(p, node) &&
			    parkAndCheckInterrupt())
				    // 线程若被中断,设置interrupted为true
				    interrupted = true;
	    	}
	    } finally {
		    if (failed)
		    	cancelAcquire(node);
		}
    }

* 自旋判断该线程的该节点的前驱节点是否是head节点
	* 若是，则可以尝试获取锁
		* 若获取成功，则设置该节点为head节点
		* 原head节点next=null，之后被GC
		* 只有前驱节点是head节点获取成功后，其他线程，的前驱节点才能变成head节点，才能就行获取锁操作
		* **所以这里是公平锁，只有没进队列的线程，才可以不公平的和head的下一个节点竞争锁**
	* 若不是，或者获取任没成功，判断是否可以挂起，若可以，则挂起

## shouldParkAfterFailedAcquire(p, node)，parkAndCheckInterrupt() ##
    /**
     * 判断当前线程获取锁失败之后是否需要挂起.
     */
    private static boolean shouldParkAfterFailedAcquire(Node pred, Node node) {//pred是节点点前驱节点，node是该节点
	    //前驱节点的状态
	    int ws = pred.waitStatus;
	    if (ws == Node.SIGNAL)
		    // 前驱节点状态为signal,返回true
		    return true;
	    // 前驱节点状态为CANCELLED
	    if (ws > 0) {
		    // 从队尾向前寻找第一个状态不为CANCELLED的节点
		    do {
		    	node.prev = pred = pred.prev;
		    } while (pred.waitStatus > 0);
		    pred.next = node;
	    } else {
		    // 将前驱节点的状态设置为SIGNAL
		    compareAndSetWaitStatus(pred, ws, Node.SIGNAL);
	    }
	    return false;
    }
    /**
     * 挂起当前线程,返回线程中断状态并重置
     */
    private final boolean parkAndCheckInterrupt() {
    	LockSupport.park(this);
    	return Thread.interrupted();
    }

* 首先看该节点的前驱节点状态是否为SIGNAL
	* 如果是，返回true
	* 如果不是，很烦的把前一个节点设置成SIGNAL
	* 即告诉前一个节点，但它获取锁并且出队后，唤醒自己
	* SIGNAL=-1，CANCELLED=1
	* 如果前驱结点的状态不是SIGNAL，那么自己就不能安心挂起，需要去找个安心的挂起点，同时可以再尝试下看有没有机会去尝试竞争锁
	* 前面若不是挂起的，前面是有问题的节点，找到第一个没有问题的节点，跟上它，抛弃有问题的节点