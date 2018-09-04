## hashmap 1.7与1.8的resize方法

## 1.7：头插法会发生循环链表
```java
void resize(int newCapacity) {
    Entry[] oldTable = table;
    int oldCapacity = oldTable.length;
    ......
    //创建一个新的Hash Table
    Entry[] newTable = new Entry[newCapacity];
    //将Old Hash Table上的数据迁移到New Hash Table上
    transfer(newTable);
    table = newTable;//指针变换！
    threshold = (int)(newCapacity * loadFactor);
}

void transfer(Entry[] newTable) {
    Entry[] src = table;
    int newCapacity = newTable.length;
    //下面这段代码的意思是：
    //  从OldTable里摘一个元素出来，然后放到NewTable中
    for (int j = 0; j < src.length; j++) {
        Entry<K,V> e = src[j];
        if (e != null) {
            src[j] = null;
            do {
                //头插法！！！！
                Entry<K,V> next = e.next;//第一个线程到这里时它的e指针指向3元素，next指针指向7元素
                int i = indexFor(e.hash, newCapacity);
                e.next = newTable[i];
                newTable[i] = e;
                e = next;
            } while (e != null);
        }
    }
}

void addEntry(int hash, K key, V value, int bucketIndex) {
 //先判断大小,关键！！！   
  if ((size >= threshold) && (null != table[bucketIndex])) {
 // 若HashMap的实际大小不小于 “阈值”，则调整HashMap的大小    
            resize(2 * table.length);//扩容,每次增长2倍
            hash = (null != key) ? hash(key) : 0;
            bucketIndex = indexFor(hash, table.length);
        }
       createEntry(hash, key, value, bucketIndex);//新增Entry。将“key-value”插入指定位置，bucketIndex是位置索引。 
}
void createEntry(int hash, K key, V value, int bucketIndex) {
    //明显的头插法！！！！！！！！！
      // 保存“bucketIndex”位置的值到“e”中 
        Entry<K,V> e = table[bucketIndex];
    // 设置“bucketIndex”位置的元素为“新Entry”，  
    // 设置“e”为“新Entry的下一个节点”  
        table[bucketIndex] = new Entry<>(hash, key, value, e);
        size++;
}
```
* map的初始化长度为4，阈值为3，只要添加第4个节点时就扩展
* 主线程先put(5,5)（3,3）（7,7）： **因为是先判断大小，再size++，再插入！**，所以当put(7,7)后并没有扩容
* 这样table[i]：7->3->5->null
* 线程1put(13,13)，走到transfer方法，还没进入
* 线程1，进入transfer方法，直到Entry<K,V> next = e.next;
  * 这样指针e->7，**next->3**
* 线程2put(17,17)走进transfer方法，完成transfer后 **table[i]：5->3->7->null **，**关键就是这里：顺序变掉了，本来3指向5的，现在3指向7了**，这样线程1操作e=3之后，e.next 又指向了7，又对7进行操作了，形成了循环
* 进入线程1，此时将e->7放入newTable[i]的头部，现在**e=next=3**,进入下次循环
  * 现在newTable[i]：7->null
  * 现在e->3，将e->3放入newTable[i]头部，将e.next指向旧的newTable[i]，**现在e=next=7，这是关键e不为null，进入下次循环**
    * 现在newTable[i]：3->7->null
  * 现在e->7，将e->7放入newTable[i]的头部，将e.next指向旧的newTable[i]，**现在e=next=null了**
    * 现在newTable[i]：7->3->7->null，形成了循环





## 1.8：尾插法不会发生循环链表

```java
final Node<K,V>[] resize() {
    Node<K,V>[] oldTab = table;
    int oldCap = (oldTab == null) ? 0 : oldTab.length;
    int oldThr = threshold;
    int newCap, newThr = 0;
    if (oldCap > 0) {
        if (oldCap >= MAXIMUM_CAPACITY) {
            threshold = Integer.MAX_VALUE;
            return oldTab;
        }
        else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY &&
                 oldCap >= DEFAULT_INITIAL_CAPACITY)
            newThr = oldThr << 1; // double threshold
    }
    else if (oldThr > 0) // initial capacity was placed in threshold
        newCap = oldThr;
    else {               // zero initial threshold signifies using defaults
        newCap = DEFAULT_INITIAL_CAPACITY;
        newThr = (int)(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
    }
    if (newThr == 0) {
        float ft = (float)newCap * loadFactor;
        newThr = (newCap < MAXIMUM_CAPACITY && ft < (float)MAXIMUM_CAPACITY ?
                  (int)ft : Integer.MAX_VALUE);
    }
    threshold = newThr;
    @SuppressWarnings({"rawtypes","unchecked"})
        Node<K,V>[] newTab = (Node<K,V>[])new Node[newCap];
    table = newTab;
    if (oldTab != null) {
        for (int j = 0; j < oldCap; ++j) {
            Node<K,V> e;
            if ((e = oldTab[j]) != null) {
                oldTab[j] = null;
                if (e.next == null)
                    newTab[e.hash & (newCap - 1)] = e;
                else if (e instanceof TreeNode)
                    ((TreeNode<K,V>)e).split(this, newTab, j, oldCap);
                else { // preserve order
                    //尾插法！！！
                    Node<K,V> loHead = null, loTail = null;
                    Node<K,V> hiHead = null, hiTail = null;
                    Node<K,V> next;
                    do {
                        next = e.next;
                        if ((e.hash & oldCap) == 0) {
                            if (loTail == null)
                                loHead = e;
                            else
                                loTail.next = e;
                            loTail = e;
                        }
                        else {
                            if (hiTail == null)
                                hiHead = e;
                            else
                                hiTail.next = e;
                            hiTail = e;
                        }
                    } while ((e = next) != null);
                    if (loTail != null) {
                        loTail.next = null;
                        newTab[j] = loHead;
                    }
                    if (hiTail != null) {
                        hiTail.next = null;
                        newTab[j + oldCap] = hiHead;
                    }
                }
            }
        }
    }
    return newTab;
}
```

* 1.7是头插法，这样resize的时候，插入newTable[i]，只需要直接将新节点的next指针指向newTable[i]，再将newTable[i]指向新节点，不要遍历链表找到尾部
* 1.8是尾插法，维护了两组指针，高低头尾指针，这样就可以避免遍历链表找到尾部的操作了
* 而且因为是尾插法，所以newTable中的节点**顺序不变**，这样不会形成循环了
* 1.8是先插入，再扩容，1.7是先扩容再插入