[TOC]

# 前言

## 程序设计是对复杂性的管理

* 待解决问题的复杂性
* 用来解决该问题的工具的复杂性

##  Java的设计目标

* 为程序员减少复杂性，即减少开发健壮代码所需的时间以及困难
* 所以使得代码的运行并不快



# 1 对象导论

## 1.1 抽象的重要性

* 人们所能够解决的问题的复杂性直接取决于抽象的类型[^1]和质量

* 创建抽象数据类型是面向对象程序设计的基本概念之一

*  对象具有：状态[^2]、行为[^3]、标识[^4]

*  类描述了：具有相同**特性**（数据元素）**和行为**（功能）的对象（即实例）集合

## 1.2 面向对象程序设计的概念

* 面向对象程序设计的挑战之一：在问题空间的元素和解空间的对象之间创建一对一的映射
* 代码与隐藏的数据一起构成了**实现**

## 1.3 将对象看成服务提供者（生产者）

* 程序本身向用户提供服务，它将**调用其它对象提供的服务**来实现这一目的
* 有助于提高对象的内聚性：高内聚是软件设计的基本质量要求之一，不能将过多的功能塞到一个对象中

## 1.4 访问权限修饰符

* 让客户端程序员无法接触到他们不需要或不应该接触到的部分——这部分对数据类型的内部操作来说是必须的，但并不是用户解决问题所需要的接口
* 运行库设计者可以**改变类内部的工作方式而不用担心会影响其它类**，可以使接口和实现清晰的分离

1. public：在任何类里，**创建该对象后**，都可以直接调用public修饰的属性和方法
   * **能被继承**
2. protected：在同一个包内，创建该对象后，可以调用。且可以继承使用
   * **能被继承**
3. 默认：在同一个包内（**即处于同一级别**），包的父包就不行了，**不能被子类继承**
   * **不能被继承**
4. private：只有在该类里，**创建该对象后**，才可以调用private修饰的属性和方法
   * **不能被继承**

public -> protected -> 默认 -> private

## 1.5 URL类型分析

![](./cite/五种UML类型图.png)

关系强弱顺序：依赖 < 关联 < 聚合 < 组合 < 继承

**依赖：**弱相关，只是**在方法中用到**

![](./img/依赖.png)

```java
public class Person {
	public void doSomething(){
		Card card = new Card();//局部变量
		....
	}
}
public class Person {
	public void doSomething(Card card){//方法参数
		....
	}
}
public class Person {
    Card card;
	public void doSomething(Card card){
		int id = Card.getId();//静态方法调用
		....
	}
}
```

**关联：**比实现相关性强，是类的属性

**单向或双向**（通常我们需要避免使用双向关联关系），是一种"has a"关系，如果A单向关联B，则可以说A has a B，通常表现为全局变量 

![](./img/关联.png)

```java
public class Person {
	public Phone phone;
	
	public void setPhone(Phone phone){		
		this.phone = phone;
	}
	
	public Phone getPhone(){		
		return phone;
	}
}
```

**聚合： **关联关系的一种， **整体不存在了，部分依然存在** 

单向， **关联关系的一种**，与关联关系之间的区别是语义上的，关联的两个对象通常是平等的，**聚合则一般不平等**，有一种**整体和局部**的感觉 

![](./img/聚合.png)

```java
public class Team {
	public Person person;
	
	public Team(Person person){
		this.person = person;
	}
}
```



**组合：**单向，强依赖的特殊聚合关系 ， **整体不存在了，部分也将消亡** 

![](./img/组合.png)

```java
public class Person {
	public Head head;
	public Body body;
	public Arm arm;
	public Leg leg;
	
	public Person(){
		head = new Head();
		body = new Body();
		arm = new Arm();
		leg = new Leg();
	}
}
```

**实现：**接口

**继承：**即"is a"关系，类继承抽象类 

![](./img/继承)

## 1.6 类的层次结构

* UML类型，构成了类的层次结构

## 1.7 多态

* 把对象**不当作**它所属的特定类型来对待，而是将其**当作基类的对象来对待**
* 即方法操作的是泛化类型，不需担心对象将如何处理消息
* 前期绑定：方法运行时将这个调用**解析到将要被执行的代码的绝对地址**
* 后期绑定：Java使用一小段特殊的代码来**替代绝对地址调用**
* C++默认的是前期绑定，只有使用`virtual`关键字修饰才会为后期绑定，Java 默认是后期绑定

## 1.8 静态分配与动态分配

```java
public class StaticDispatch {
    static abstract class Human {
        void say() {
            System.out.println("human");
        }
    };
    static class Man extends Human{
        @Override
        void say() {
            System.out.println("man");
        }
    };
    static class Woman extends Human{
        @Override
        void say() {
            System.out.println("woman");
        }
    };
    public void sayHello(Human guy) {
        guy.say();
        System.out.println("hello,guy");
    }
    public void sayHello(Man guy) {
        guy.say();
        System.out.println("hello,Man");
    }
    public void sayHello(Woman guy) {
        guy.say();
        System.out.println("hello,Woman");
    }

    public static void main(String[] args) {
        Human man = new Man();
        Human woman = new Woman();
        Woman wo = new Woman();
        StaticDispatch st = new StaticDispatch();
        st.sayHello(man);
        st.sayHello(woman);
        st.sayHello(wo);
    }
}
```

* **一定要搞清楚是静态分配还是动态分配！**
* 首先，`st.sayHello(man)`因为`st`是确定的，所以是静态分配
* 所以，方法中的类型，**要看作静态类型**，`Human` !
* 最后， **静态分配**，进入`sayHello(Human guy)`
* 其中，又调用`guy.say()`
* 显然`guy`是基类，所有**产生动态分配**

## 1.9 单继承结构

* 在Java中（包括除C++以外的所有OOP语言）都是单继承结构，终极基类就是`Object`
* 在单继承中，所有的对象都具有一个共用接口，所以所有的对象归根到底**都是相同的基本类型**
* 单继承保证所有对象都具有某些功能，所有对象可以很容易地在堆上创建，而参数传递也极大的简化了，且使垃圾回收器的实现变得容易得多

## 1.10 容器

* 容器，即存放对象的地方，可以称为集合
* 在java5之前，容器只存储`Object`，所以当对象加入容器时，必须向上转型为Object，因此会丢失其身份
* 而向下转型和运行时的检查需要额外的程序运行时间

## 1.11 泛型/GC/异常/并发

* 泛型：泛型避免了向下转型和运行时检查的时间
* GC：GC使Java编程不需要考虑内存的分配与释放，使得编程更加简单
* 异常：Java内置异常处理机制，程序必须处理异常，使得错误处理非常容易
* 并发：最初，程序员使用机器底层的知识来编写中断服务程序，主进程挂起是**通过硬件中断来触发的**，一方面难度大，另一方面一致性差

## 1.12 插件/脚本语言/Java

* 插件：客户端编程所迈出的最重要的一步就是插件（plug-in）的开发。程序员可以下载一段代码，并将其插入到浏览器中的适当位置，以此来为浏览器添加新功能。但是编写插件并不轻松，价值在于：程序员不需要经过浏览器生产商的许可，就可以开发某种语言扩展，并将它们添加到服务器中。
* 脚本语言：使用脚本语言，可以将客户端程序的源代码直接嵌入到HTML页面中， **解释这种语言的插件**在HTML页面被显示时自动激活。缺点是：代码会暴露给任何人去浏览。JavaScript不需要任何插件支持。
* Java：脚本语言已经可以解决百分之八十的问题，而Java用来解决剩下的比较困难的问题



# 2 销毁













参考：

https://blog.csdn.net/a19881029/article/details/8957441























[^1]: 类型是指：所抽象的是什么，汇编语言是对底层机器的轻微抽象，其它命令式语言（FORTRAN,BASIC,C）是对汇编的抽象
[^2]: 内部数据
[^3]: 方法
[^4]: 每个对象都是唯一的，是具有相同的特性和行为的对象所属的类的一部分