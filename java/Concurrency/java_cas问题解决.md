* 从Java1.5开始JDK的atomic包里提供了一个类AtomicStampedReference来解决ABA问题。这个类的compareAndSet方法作用是首先检查当前引用是否等于预期引用，并且当前标志是否等于预期标志，如果全部相等，则以原子方式将该引用和该标志的值设置为给定的更新值
* 其中用Pair类，封装了一个版本标志

---
    AtomicStampedReference a1 = new AtomicStampedReference(initialRef,initialStamp);
    a1.compareAndSet(expectedReference,newReference,expectedStamp,newStamp);

* 在a1的要CAS的变量中封装了一个Pair类，将原始变量initialRef和initialStamp封装到pair类中
* 最终CAS的是pair类
