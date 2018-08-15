## 并发集合1

### 并发 List

### 简介

1. `Vector` 和 `CopyOnWriteArrayList` 是两个线程安全的 List 实现，还有集合线程安全包装类 `SynchronizedList = Collections.synchronizedList()` 进行包装后的线程安全集合
2. 建议：

- `SynchronizedList` 实现效率比较慢，不建议使用；
- `CopyOnWriteArrayList` 的实现原理是减少锁竞争，读方法没有任何锁操作，所以效率很高（牺牲了写）。适合读多写少的环境。
- `Vector` 写的效率比 `CopyOnWriteArrayList` 好，写多读少的情况可以考虑
- vector是队列形式，`CopyOnWriteArrayList` 是列表形式

#### CopyOnWriteArrayList

1. `CopyOnWriteArrayList` 实现机制

- 进行写操作时，复制内部容器，写入拷贝；读操作，直接返回结果。读写操作的不同的数组容器
- 利用了对象的不变性，在没有对对象进行写操作前，由于对象没有发生改变，不需要加锁
- 试图改变对象时（写），总是先获取对象的一个副本，然后对副本进行修改，最后写回。读的是容器对象（数组对象），写的时候操作拷贝对象。
- 减少锁竞争，提高读的性能，牺牲了写的性能（写要加锁，不然没办法保证副本线程安全）

1. 问题：

- 内存占用问题。因为`CopyOnWrite`的写时复制机制,所以在进行写操作的时候，内存里会同时驻扎两份对象的内存。
- 数据一致性问题。`CopyOnWrite` 容器只能保证数据的最终一致性，不能保证数据的实时一致性。所以如果你希望写入的的数据，马上能读到，请不要使用CopyOnWrite容器

## 并发 Set

1. `CopyOnWriteArraySet` 内部依赖 `CopyOnWriteArrayList` 。特性一致，适合读多写少的并发场景
2. `CopyOnWriteArrayList`和`CopyOnWriteArraySet` 这种`CopyOnWrite`类型的容器，更加重要的是要了解其设计实现：

- 读写分离
- 最终一致性
- 使用另外开辟空间的思路，来解决并发冲突

## 并发 Map

### 简介

1. 包装类：`Collections.synchronizedList(list);`
2. 并发类：`ConcurrentHashMap`，性能更好，读是无锁的，写操作的锁粒度是比较小的（经典的减小锁粒度设计），整体性能要较好

### JDK7设计: 使用`Segment`分段锁，减小锁粒度

1. 一个典型的 `HashMap`，如果 `get` 和 `add` 进行同步，如果锁对象为整个`HashMap`，那么没有两个线程是可以真正的并发
2. 使用`Segment`分段锁。`ConcurrentHashMap`，采用拆分锁对象的方式提高吞吐量。其将整个 `HashMap` 拆分成若干个段 `segment`，每段都是一个子的`HashMap`
3. 如果需要在 `ConcurrentHashMap` 中增加一个新的数据，并不是将整个`hashmap`加锁，而是先根据`hashcode`得到该数据项应该被存到哪个段里面，然后对该段加锁。多个线程同时进行`put`操作，只要被加入的数据项不是同一个段`segment`中，可以真正的并行
4. 缺点问题：系统需要全局锁时，其消耗的资源比较大。如 ConcurrentHashMap 的 size 方法，有可能需要每段加锁加以统计（因为在统计的时候，有可能会有写入）。JDK7的实现思路：先采用不加锁的方式，连续计算元素的个数，最多计算3次

- 如果前后两次计算结果相同，则说明计算出来的元素个数是准确的
- 如果前后两次计算结果都不同，则给每个Segment进行加锁，再计算一次元素的个数

### JDK8设计

1. 取消了`Segment`分段锁，数组+链表（红黑树）的结构，对于锁的粒度，调整为对每个数组元素加锁（Node）
2. 定位节点的`hash`算法被简化了，这样带来的弊端是Hash冲突会加剧。因此在链表节点数量大于8时，会将链表转化为红黑树进行存储。这样一来，查询的时间复杂度就会由原先的O(n)变为O(logN)
3. 使用 `synchronized` 关键字，这一点也说明 `synchronized` 在JDK8中优化的程度和`ReentrantLock`差不多了

1. `get`方法：没有加锁，所以在多线程操作的过程中，并不能完全的保证一致性

- 首先定位到table[]中的i
- 若table[i]存在，则继续查找
- 首先比较链表头部，如果是则返回
- 然后如果为红黑树，查找树
- 最后再循环链表查找

1. `put` 方法：

- 参数校验
- 若table[]未创建，则初始化
- 当table[i]后面无节点时，直接创建Node（无锁操作）
- 如果当前正在扩容，则帮助扩容并返回最新table[]
- 然后在链表或者红黑树中追加节点
- 最后还回去判断是否到达阀值，如到达变为红黑树结构

1. `size`方法：

- 从上面代码可以看出来，JDK1.8中新增了一个 mappingCount()的API。这个API与size()不同的就是返回值是Long类型，这样就不受Integer.MAX_VALUE的大小限制了
- 两个方法都同时调用了 `sumCount()`方法。对于每个table[i]都有一个CounterCell与之对应，上面方法做了求和之后就返回了。从而可以看出，size() 和 mappingCount() 返回的都是一个估计值（没有加锁，强一致）
- 与JDK1.7里面的实现不同，1.7里面使用了加锁的方式实现。这里面也可以看出 JDK1.8 牺牲了精度，来换取更高的效率





分析
=====

1. `java.util` 包下日常使用的大部分集合都是线程不安全的，如`ArrayList`、`HashMap`等
2. 简单的例子：`ConcurrentModificationException` 并发修改异常

```java
public class ConcurrentModificationExceptionTest {

    public static void main(String[] args) {
        ArrayList<String> list = new ArrayList<>();
        list.add("张三");
        list.add("李四");
        list.add("王五");
        
        Iterator<String> iterator = list.iterator();
        //迭代集合
        while (iterator.hasNext()) {
            String next = iterator.next();
            if ("张三".equals(next)) {
                // 在集合遍历的时候，不能由其他的线程对其内部元素做修改
//                list.remove(next);
                list.add("赵六");
            } else {
                System.out.println(next);
            }
        }
    }
}
```

## 并发 List

### 简介

1. `Vector` 和 `CopyOnWriteArrayList` 是两个线程安全的 List 实现，还有集合线程安全包装类 `SynchronizedList = Collections.synchronizedList()` 进行包装后的线程安全集合
2. 建议：

- `SynchronizedList` 实现效率比较慢，不建议使用；
- `CopyOnWriteArrayList` 的实现原理是减少锁竞争，读方法没有任何锁操作，所以效率很高（牺牲了写）。适合读多写好的环境。
- `Vector` 写的效率比 `CopyOnWriteArrayList` 好，写多读少的情况可以考虑

### 详解

#### SynchronizedList

1. 常用方法：

```java
public E get(int index) {
    synchronized (mutex) {return list.get(index);}
}

public E set(int index, E element) {
    synchronized (mutex) {return list.set(index, element);}
}
```

1. 直接是在利用 `synchronized` 锁定了代码块，简单粗暴，其实不建议使用

#### CopyOnWriteArrayList

1. `CopyOnWriteArrayList` 实现机制

- 进行写操作时，复制内部容器，写入拷贝；读操作，直接返回结果。读写操作的不同的数组容器
- 利用了对象的不变性，在没有对对象进行写操作前，由于对象没有发生改变，不需要加锁
- 试图改变对象时（写），总是先获取对象的一个副本，然后对副本进行修改，最后写回。读的是容器对象（数组对象），写的时候操作拷贝对象。
- 减少锁竞争，提高读的性能，牺牲了写的性能（写要加锁，不然没办法保证副本线程安全）

1. 常用方法

- `get`方法：并没有任何的锁，所以效率很高

```java
public E get(int index) {
    return get(getArray(), index);
}

private E get(Object[] a, int index) {
    return (E) a[index];
}
```

- `add`方法: 每一个的`add`方法中，都会有一次数组拷贝，并且申请了锁

```java
public boolean add(E e) {
    final ReentrantLock lock = this.lock; // 加锁
    lock.lock();
    try {
        Object[] elements = getArray();
        int len = elements.length;
        Object[] newElements = Arrays.copyOf(elements, len + 1); // 做了一次数组拷贝
        newElements[len] = e;   // 修改副本
        setArray(newElements);  // 写回副本
        return true;
    } finally {
        lock.unlock();  // 解锁
    }
}
```

1. 问题：

- 内存占用问题。因为`CopyOnWrite`的写时复制机制,所以在进行写操作的时候，内存里会同时驻扎两份对象的内存。
- 数据一致性问题。`CopyOnWrite` 容器只能保证数据的最终一致性，不能保证数据的实时一致性。所以如果你希望写入的的数据，马上能读到，请不要使用CopyOnWrite容器

#### Vector

1. `get`方法：使用 `synchronized`关键字，存在锁竞争，效率没有`CopyOnWriteArrayList`好

```java
public synchronized E get(int index) {
    if (index >= elementCount)
        throw new ArrayIndexOutOfBoundsException(index);

    return elementData(index);
}
```

1. `add`方法：同样的实现方式，没有副本拷贝，效率比`CopyOnWriteArrayList`好

```java
public synchronized boolean add(E e) {
    modCount++;
    ensureCapacityHelper(elementCount + 1);
    elementData[elementCount++] = e;
    return true;
}
```

## 并发 Set

1. `CopyOnWriteArraySet` 内部依赖 `CopyOnWriteArrayList` 。特性一致，适合读多写少的并发场景
2. `CopyOnWriteArrayList`和`CopyOnWriteArraySet` 这种`CopyOnWrite`类型的容器，更加重要的是要了解其设计实现：

- 读写分离
- 最终一致性
- 使用另外开辟空间的思路，来解决并发冲突

1. 简单源码

```java
public class CopyOnWriteArraySet<E> extends AbstractSet<E> implements java.io.Serializable {
    
    private final CopyOnWriteArrayList<E> al;
    
    public boolean add(E e) {
        return al.addIfAbsent(e);
    }
}
```

## 并发 Map

### 简介

1. 包装类：`Collections.synchronizedList(list);`
2. 并发类：`ConcurrentHashMap`，性能更好，读是无锁的，写操作的锁粒度是比较小的（经典的减小锁粒度设计），整体性能要较好

### ConcurrentHashMap （JDK7、8 设计差别较大）

#### JDK7源码

1. `put`方法：`Segment` 分段加锁

```java
@Override
public V put(K key, V value) {
    if (value == null) {
        throw new NullPointerException();
    }
    int hash = hashOf(key); // hash 分段
    return segmentFor(hash).put(key, hash, value, false);
}

V put(K key, int hash, V value, boolean onlyIfAbsent) {
    lock(); // Segment 分段加锁
    try {
        int c = count;
        if (c ++ > threshold) { // ensure capacity
            int reduced = rehash();
            if (reduced > 0) {
                count = (c -= reduced) - 1; // write-volatile
            }
        }

        HashEntry<K, V>[] tab = table;
        int index = hash & tab.length - 1;
        HashEntry<K, V> first = tab[index];
        HashEntry<K, V> e = first;
        while (e != null && (e.hash != hash || !keyEq(key, e.key()))) {
            e = e.next;
        }

        V oldValue;
        if (e != null) {
            oldValue = e.value();
            if (!onlyIfAbsent) {
                e.setValue(value);
            }
        } else {
            oldValue = null;
            ++ modCount;
            tab[index] = newHashEntry(key, hash, first, value);
            count = c; // write-volatile
        }
        return oldValue;
    } finally {
        unlock();
    }
}
```

2. `get`方法：

```java
public V get(Object key) {
    int hash = hashOf(key);
    return segmentFor(hash).get(key, hash);
}

V get(Object key, int hash) {
    if (count != 0) { // read-volatile
        HashEntry<K, V> e = getFirst(hash);
        while (e != null) {
            if (e.hash == hash && keyEq(key, e.key())) {
                V opaque = e.value();
                if (opaque != null) {
                    return opaque;
                }

                return readValueUnderLock(e); // recheck
            }
            e = e.next;
        }
    }
    return null;
}
```

#### JDK7设计: 使用`Segment`分段锁，减小锁粒度

![ConcurrentHashMap jdk7设计](.\cite\segment分段锁.jpg)

1. 一个典型的 `HashMap`，如果 `get` 和 `add` 进行同步，如果锁对象为整个`HashMap`，那么没有两个线程是可以真正的并发
2. 使用`Segment`分段锁。`ConcurrentHashMap`，采用拆分锁对象的方式提高吞吐量。其将整个 `HashMap` 拆分成若干个段 `segment`，每段都是一个子的`HashMap`
3. 如果需要在 `ConcurrentHashMap` 中增加一个新的数据，并不是将整个`hashmap`加锁，而是先根据`hashcode`得到该数据项应该被存到哪个段里面，然后对该段加锁。多个线程同时进行`put`操作，只要被加入的数据项不是同一个段`segment`中，可以真正的并行
4. 缺点问题：系统需要全局锁时，其消耗的资源比较大。如 ConcurrentHashMap 的 size 方法，有可能需要每段加锁加以统计（因为在统计的时候，有可能会有写入）。JDK7的实现思路：先采用不加锁的方式，连续计算元素的个数，最多计算3次

- 如果前后两次计算结果相同，则说明计算出来的元素个数是准确的
- 如果前后两次计算结果都不同，则给每个Segment进行加锁，再计算一次元素的个数

```java
public int size() {
    final Segment<K, V>[] segments = this.segments;
    long sum = 0;
    long check = 0;
    int[] mc = new int[segments.length];
    // Try a few times to get accurate count. On failure due to continuous
    // async changes in table, resort to locking.
    for (int k = 0; k < RETRIES_BEFORE_LOCK; ++ k) { // 先不加锁算
        check = 0;
        sum = 0;
        int mcsum = 0;
        for (int i = 0; i < segments.length; ++ i) {
            sum += segments[i].count;
            mcsum += mc[i] = segments[i].modCount;
        }
        if (mcsum != 0) {
            for (int i = 0; i < segments.length; ++ i) {
                check += segments[i].count;
                if (mc[i] != segments[i].modCount) {
                    check = -1; // force retry
                    break;
                }
            }
        }
        if (check == sum) {
            break;
        }
    }
    if (check != sum) { // Resort to locking all segments 前后不一致，就加锁算
        sum = 0;
        for (Segment<K, V> segment: segments) {
            segment.lock(); // 每个分段都需要加锁
        }
        for (Segment<K, V> segment: segments) {
            sum += segment.count;
        }
        for (Segment<K, V> segment: segments) {
            segment.unlock();
        }
    }
    if (sum > Integer.MAX_VALUE) {
        return Integer.MAX_VALUE;
    } else {
        return (int) sum;
    }
}
```

#### JDK8设计

![ConcurrentHashMap jdk8设计](.\cite\concurrentHashMap8锁.jpg)

1. 取消了`Segment`分段锁，数组+链表（红黑树）的结构，对于锁的粒度，调整为对每个数组元素加锁（Node）
2. 定位节点的`hash`算法被简化了，这样带来的弊端是Hash冲突会加剧。因此在链表节点数量大于8时，会将链表转化为红黑树进行存储。这样一来，查询的时间复杂度就会由原先的O(n)变为O(logN)
3. 使用 `synchronized` 关键字，这一点也说明 `synchronized` 在JDK8中优化的程度和`ReentrantLock`差不多了

#### JDK8源码

1. `get`方法：没有加锁，所以在多线程操作的过程中，并不能完全的保证一致性

- 首先定位到table[]中的i
- 若table[i]存在，则继续查找
- 首先比较链表头部，如果是则返回
- 然后如果为红黑树，查找树
- 最后再循环链表查找

```java
public V get(Object key) {
    Node<K,V>[] tab; Node<K,V> e, p; int n, eh; K ek;
    int h = spread(key.hashCode());// 定位到table[]中的i
    if ((tab = table) != null && (n = tab.length) > 0 &&
        (e = tabAt(tab, (n - 1) & h)) != null) {// 若table[i]存在
        if ((eh = e.hash) == h) {// 比较链表头部
            if ((ek = e.key) == key || (ek != null && key.equals(ek)))
                return e.val;
        }
        else if (eh < 0)// 若为红黑树，查找树
            return (p = e.find(h, key)) != null ? p.val : null;
        while ((e = e.next) != null) {// 循环链表查找
            if (e.hash == h &&
                ((ek = e.key) == key || (ek != null && key.equals(ek))))
                return e.val;
        }
    }
    return null;// 未找到
}
```

1. `put` 方法：

- 参数校验
- 若table[]未创建，则初始化
- 当table[i]后面无节点时，直接创建Node（无锁操作）
- 如果当前正在扩容，则帮助扩容并返回最新table[]
- 然后在链表或者红黑树中追加节点
- 最后还回去判断是否到达阀值，如到达变为红黑树结构

```java
public V put(K key, V value) {
    return putVal(key, value, false);
}

final V putVal(K key, V value, boolean onlyIfAbsent) {
    if (key == null || value == null) throw new NullPointerException();
    int hash = spread(key.hashCode());
    int binCount = 0;
    for (Node<K,V>[] tab = table;;) {
        Node<K,V> f; int n, i, fh;
        if (tab == null || (n = tab.length) == 0)
            tab = initTable();  // 若table[]未创建，则初始化
        else if ((f = tabAt(tab, i = (n - 1) & hash)) == null) {    // table[i]后面无节点时，直接创建Node（无锁操作)
            if (casTabAt(tab, i, null,
                         new Node<K,V>(hash, key, value, null)))
                break;                   // no lock when adding to empty bin
        }
        else if ((fh = f.hash) == MOVED)    // 如果当前正在扩容，则帮助扩容并返回最新table[]
            tab = helpTransfer(tab, f);
        else {  // 在链表或者红黑树中追加节点
            V oldVal = null;
            synchronized (f) {  // 这里并没有使用ReentrantLock，说明synchronized已经足够优化了
                if (tabAt(tab, i) == f) {
                    if (fh >= 0) {  // 如果为链表结构
                        binCount = 1;
                        for (Node<K,V> e = f;; ++binCount) {
                            K ek;
                            if (e.hash == hash &&
                                ((ek = e.key) == key ||
                                 (ek != null && key.equals(ek)))) { // 找到key，替换value
                                oldVal = e.val;
                                if (!onlyIfAbsent)
                                    e.val = value;
                                break;
                            }
                            Node<K,V> pred = e;
                            if ((e = e.next) == null) { // 在尾部插入Node
                                pred.next = new Node<K,V>(hash, key,
                                                          value, null);
                                break;
                            }
                        }
                    }
                    else if (f instanceof TreeBin) {    // 如果为红黑树
                        Node<K,V> p;
                        binCount = 2;
                        if ((p = ((TreeBin<K,V>)f).putTreeVal(hash, key,
                                                       value)) != null) {
                            oldVal = p.val;
                            if (!onlyIfAbsent)
                                p.val = value;
                        }
                    }
                }
            }
            if (binCount != 0) {
                if (binCount >= TREEIFY_THRESHOLD)  // 到达阀值，变为红黑树结构
                    treeifyBin(tab, i);
                if (oldVal != null)
                    return oldVal;
                break;
            }
        }
    }
    addCount(1L, binCount);
    return null;
}
```

1. `size`方法：

- 从上面代码可以看出来，JDK1.8中新增了一个 mappingCount()的API。这个API与size()不同的就是返回值是Long类型，这样就不受Integer.MAX_VALUE的大小限制了
- 两个方法都同时调用了 `sumCount()`方法。对于每个table[i]都有一个CounterCell与之对应，上面方法做了求和之后就返回了。从而可以看出，size() 和 mappingCount() 返回的都是一个估计值（没有加锁，强一致）
- 与JDK1.7里面的实现不同，1.7里面使用了加锁的方式实现。这里面也可以看出 JDK1.8 牺牲了精度，来换取更高的效率

```java
// 1.2时加入
public int size() {
    long n = sumCount();
    return ((n < 0L) ? 0 :
            (n > (long)Integer.MAX_VALUE) ? Integer.MAX_VALUE :
            (int)n);
}
// 1.8加入的API
public long mappingCount() {
    long n = sumCount();
    return (n < 0L) ? 0L : n; // ignore transient negative values
}
 
final long sumCount() {
    CounterCell[] as = counterCells; CounterCell a;
    long sum = baseCount;
    if (as != null) {
        for (int i = 0; i < as.length; ++i) {
            if ((a = as[i]) != null)
                sum += a.value;
        }
    }
    return sum;
}
```