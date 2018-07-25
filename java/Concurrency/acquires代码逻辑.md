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
		    if (compareAndSetTail(pred, node)) {//先将新节点原子化成尾节点，这是其他线程指向的尾节点已经自动变成了该节点，因为有个 tail 指针过渡！
			    pred.next = node;//形成双向不急
			    return node;
		    }
	    }
	    // 当有多个线程同时遇到pred==null时，进入enq(node)处理
	    enq(node);
	    return node;
    }

* **将新节点和当前线程关联并且入队列**
