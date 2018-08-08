# listener

## 定义

* Listener 用于监听 java web程序中的事件，例如创建、修改、删除Session、request、context等，并触发响应的事件 
* Listener 对应观察者模式，事件发生的时候会自动触发该事件对应的Listeer。 Listener 主要用于对 Session、request、context 进行监控 
* 不同功能的Listener 需要实现不同的 Listener  接口，一个Listener也可以实现多个接口，这样就可以多种功能的监听器一起工作 

## 类型

* 可分为三大监听类型，九大接口
  * Servlet Context Events 监听
    * ServletContextListener ：context的创建与销毁
    * ServletContextAttributeListener ：context中的属性的增删改，contextAttributes
  * HTTP Session Events 监听
    * HttpSessionListener ：Session的创建与销毁
    * HttpSessionAttributeListener ：Session中的属性的增删改,sessionAttributes
    * HttpSessionActivationListener ：Session里面的内容保存到硬盘，或者从硬盘读取 
    * HttpSessionBindingListener ：某个对象绑定/解绑到HttpSession中
  * Servlet Request Events 监听
    * ServletRequestListener ：Request的创建与销毁
    * ServletRequestAttributeListener：Request中的属性的增删改,requestAttributes
    *  AsyncListener：一个异步事件的超时、连接断开、完成
* [实现监听对象的创建与销毁代码][1]
* [实现对象属性的监听代码 ][2]
* [监听Session 内的对象代码][3]
* [实现单态登录代码][4]
* [实现在线人数监听代码][5]
* [实现对数据库连接池DataSource的初始化代码 ][6]



## 参考文献

[Servlet学习笔记（九）：监听器Listener详解](https://blog.csdn.net/u012228718/article/details/41730799)

[Servlet笔记系列(9)：监听器Listener详解](https://sam-blog.gitee.io/2014/12/05/Servlet%E7%AC%94%E8%AE%B0%E7%B3%BB%E5%88%97(9)%EF%BC%9A%E7%9B%91%E5%90%AC%E5%99%A8Listener%E8%AF%A6%E8%A7%A3/)

[Java三大器之监听器（Listener）的工作原理和代码演示](https://blog.csdn.net/reggergdsg/article/details/52891311)



[1]: ./cite/实现监听对象的创建与销毁.md
[2]: ./cite/实现对象属性的监听.md
[3]: ./cite/监听Session内的对象.md
[4]: ./cite/实现单态登录代码.md
[5]: ./cite/实现在线人数监听代码.md
[6]: ./cite/实现对数据库连接池DataSource的初始化代码.md

