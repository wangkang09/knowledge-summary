## 关键部分

* 头出尾插法避免了ABA带来的严肃问题
* poll最关键的是头指针的值一定不为null这个条件！！
* 如果是null肯定被别的线程操作了



### offer

* offer操作就是循环CAS，不成功重新获取tail指针，直到成功为止

```java
    public boolean offer(E e) {
        checkNotNull(e);
        final Node<E> newNode = new Node<E>(e);//创建一个新的节点

        //t保存的是tail指向的地址，和tail没关系了
        for (Node<E> t = tail, p = t;;) {//循环CAS进行无锁操作
            Node<E> q = p.next;//缓存尾节点指向节点的下一个节点，一般是null，这样就可以直接设置成新的节点了
            if (q == null) {
                // p is last node
                if (p.casNext(null, newNode)) {//CAS设置为新的节点，如果设置失败，说明别的线程修改了尾节点的下一个节点的地址，重新for，之后估计q==null了
                    // Successful CAS is the linearization point
                    // for e to become an element of this queue,
                    // and for newNode to become "live".
                    if (p != t) // p的地址是newNode，t的地址是
                        casTail(t, newNode);  // 原本的tail不能被改变，改变了就设置不成功
                    return true;
                }
                // Lost CAS race to another thread; re-read next
            }
            else if (p == q)
                // We have fallen off list.  If tail is unchanged, it
                // will also be off-list, in which case we need to
                // jump to head, from which all live nodes are always
                // reachable.  Else the new tail is a better bet.
                p = (t != (t = tail)) ? t : head;//重新获取tail指针
            else//
                // Check for tail updates after two hops.
                p = (p != t && t != (t = tail)) ? t : q;//也是重新获取tail指针
        }
    }
```

### poll

* poll最关键的是头指针的值一定不为null这个条件！！
* 如果是null肯定被别的线程操作了
* 另一个关键点是casItem(item,null)是原子操作
* 当操作完成之后，item变成了null，值变了

```java
public E poll() {
    restartFromHead:
    for (;;) {
        for (Node<E> h = head, p = h, q;;) {
            E item = p.item;

            if (item != null && p.casItem(item, null)) {
                // Successful CAS is the linearization point
                // for item to be removed from this queue.
                if (p != h) // hop two nodes at a time
                    updateHead(h, ((q = p.next) != null) ? q : p);
                return item;
            }
            else if ((q = p.next) == null) {
                updateHead(h, p);
                return null;
            }
            else if (p == q)
                continue restartFromHead;
            else
                p = q;
        }
    }
}
```