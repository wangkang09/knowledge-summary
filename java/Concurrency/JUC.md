# UNSAFE

## ArrayList

* 对于一些遍历的操作，如`iterator`，`writeObject`。都会做一个判断（mountcount，修改次数是否是期待的值，如果不是抛出异常）
* 底层维护的是一个数组
* remove操作返回的是旧值，通过System.arraycopy(elementData, index+1, elementData, index,                 numMoved);重新操作数组，最后要elementData[--size] = null;用于GC
* add同样是通过arraycopy完成



## LinkedList

* 遍历操作，同ArrayList
* 底层维护的是一个链表
* 添加删除操作大概有以下几种方式
  * 添加删除链表第一个元素：O(1)
  * 添加删除链表的最后一个元素：O(1)
  * 添加删除指定下标的元素：O(n)



## Deque与Queue

* Deque是双向链表，Queue是单向链表
* Deque继承了Queue，LinkedList继承了Deque：`LinkedList->Deque->Queue`
* Deque的实现类：ArrayDeque，内部维护一个循环数组，有一个head和tail标志，方法和LinkedList差不多
* Queue的实现类：PriorityQueue，内部维护一个数组，主要有[poll/peek/remove/add](./cite/PriorityQueue源码.md)方法
  * PriorityQueue源码有点难，以后处理



## HashMap

* get(key)：对key做hash(先取key的hash值，在用取出来的hash值的高16位与低16位异或得到)，如果table[hash]有值，再判断key的地址，和key的值（key的地址相同就不用判断key的值了！），hash不同肯定没有，key值也必须相同。如果key值不相同，再看看有没有形成链表或树，如果有，遍历链表或树，判断hash和key
* put(key,value)：同样是对key做hash，看对应的**hash桶**有没有值，如果没有直接插入，如果有，**判断key的地址和值**是否相等，相同则覆盖旧值，不相等，再做链表或树的操作。作链表是尾插法，作树是红黑树；返回的是旧值，如果没有相等的返回null，如果有新值插入，插入后要判断是否resize
* containKey(key)：就是get(key)的值是否为null
* containsValue(value)：外出for循环遍历table数组，内层for循环遍历table[i]链表，仅仅是e.next，可能即使是红黑数，也对链表进行了维护，只是get的时候走的是红黑树
* entrySet与keySet内部维护了一个entrySet和keySet
* remove(key)：和put一样的找法，如果找到了应该是指针的移动
* remove(key,value)：同上，应该仅仅是多了个value的比较
* size()：内部维护了一个size变量
* resize()：将内部维护的table数组指向一个新的数组，大小时旧数组的2倍。再遍历旧数组table[i]，将其中的节点通过高位是否为1组合成两个链表，高位是1的链表放入table[i+oldLength]，是0的放入table[i]



## TreeMap

* 内部维护了一颗红黑树
* entrySet与keySet同理hashMap
* 既然是树，就没有resize操作



# SAFE

## Vecotor

* 内部维护了一个elementData数组

* 所有的关键方法，都加了synchronized保证数据一致性。
* iterator就加了锁



## CopyOnWriteArrayList

* 底层维护一个volatile修饰的array数组
* add(key)：用ReentrantLock锁住对象，最后用一个原子性操作是array指向新的数组
* get(index)：不加锁，安全
* remove(index)：加锁，同add
* remove(object)：加锁，先通过indexOf找到下标
* indexOf：不加锁，从前往后遍历旧的list，返回第一个下标；lastIndexOf不加锁从后往前遍历
* iterator：操作的是快照，快照指向的是旧的地址，而其他线程不会对旧地址作变化，只是将array数组指向性的地址，所以仅支持遍历查询操作



## ConcurrentHashMap

* **put(key,value)：**也是先对key进行hash，再通过hash值找到对应的table[i]，如果table[i]为null，用CAS操作将新节点插入table[i]。如果不为null或CAS操作失败，则进入链表或树的处理流程，进入之前加了synchronize的锁，保证线程安全。
* **remove：**加synchronized锁，进入后找到key对应的节点，将其上个节点指向它的下一个节点，这样就删除了这一节点了
* **initTable**：初始化的时候先判断sizeCtl是否小于0，如果是表示已有线程在初始化，则放弃初始化。如果大于0，则CAS设置sizeCtl为-1，并用其旧值初始化，再设置0.75的阈值
* **transfer**
* get方法没有加锁，所以数据不是精确的，但是因为尾插法使得不会出现循环链表的情况



## ArrayBlockingQueue

* 底层维护了一个固定大小的数组
* put和take方法都使用了lockInterruptibly来加锁，且内部用了await方法来起到生产者和消费者模式
* 用一个putIndx和takeIndex来做循环插入和删除
* 其他的插入和删除方法是非阻塞的，内部也加了lock.lock锁，但是如果数组已满或者为空，则会对应的返回false
* 其他所有的操作都加了ReentrantLock锁：size,iterator,clear,remove,peek...
* 使用一个普通全局变量保存元素个数，所以取个数的时候要加锁



## LinkedBlockingQueue

* 底层维护了一个固定大小的单向链表，如果不带参，就是Integer.Max大小
* **插入和删除分别用了不同的锁**，这样实现了读写分离，且用了一个AtomicInteger count变量实现了插入和删除的关联
* 其他的读写操作使用了相同的锁机制，但没有取阻塞，将阻塞状态变为返回false
* size方法返回count.get()
* 其他遍历操作（iterator,contains(o),remove(o)），同时获取了两个锁



## PriorityBlockingQueue

* 底层维护了一个可变数组，通过system.arrayCopy来扩展数组
* 同ArrayBlockingQueue一样使用了一个锁
* 操作和ArrayBlockingQueue基本相同，只是多了个排序，和数组的扩展
* 有个问题就是这个数组只会扩展和不会缩小，浪费空间