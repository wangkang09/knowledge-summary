## C与Java是有区别的！

* Java 中所有的变量(不管是引用变量，还是简单变量)都是指针，指针的值指向一个数据类型
* 所以，所有的赋值，都是值的赋值
  * 简单类型，因为是不可变类型，每一次操作都是重新赋值（把指针的地址改变了）
  * 引用类型，如果是不可变类型，和简单类型一样，每次都重新赋值
  * 引用类型，如果是可变类型，就可以对变量指针指向的**地址的值**进行改变，而不改变**变量的值**
* c中有指针变量和普通变量的区别
  * 指针变量就相当与Java中的变量
  * 普通变量就是内存中的一块有具体值的区域

```c
struct MyStruct
{
	int a;
	int b;
	int c;
};
struct MyStruct ss={20,30,40};
//声明了结构对象ss，并把ss 的成员初始化为20，30 和40。
struct MyStruct *ptr=&ss;
```

![](.\img\c的地址定义图.png)

```java
public class MyStruct {
    
    public MyStruct(int a, int b, int c) {
        this.a=a;
        this.b=b;
        this.c=c;
    }
    int a;
    int b;
    int c;
}

public class Main {
    public static void main(String[] args) {
        MyStruct my = new MyStruct(20,30,40);
        MyStruct prt = my;
    }
}
```

![](.\img\java的地址定义图.png)



