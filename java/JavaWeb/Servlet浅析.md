##  Servlet

###  定义

* Servlet是sun公司提供的一门用于开发动态web资源的技术 
* Sun公司在其API中提供了一个servlet接口，用户若想用发一个动态web资源(即开发一个Java程序向浏览器输出数据)，需要完成以下2个步骤 
  * 编写一个Java类，实现servlet接口 
  * 把开发好的Java类部署到web服务器中 
  * 通常我们也把实现了servlet接口的java程序，称之为Servlet 

### Servlet运行过程

Servlet程序是由WEB服务器调用，web服务器收到客户端的Servlet访问请求后：

1. Web服务器首先检查是否已经装载并创建了该Servlet的实例对象。如果是，则直接执行第4步，否则，执行第2步
2.  装载并创建该Servlet的一个实例对象 
3. 调用Servlet实例对象的init()方法 
4. 创建一个用于封装HTTP请求消息的HttpServletRequest对象和一个代表HTTP响应消息的HttpServletResponse对象，然后调用Servlet的service()方法并将请求和响应对象作为参数传递进去 
5. WEB应用程序被停止或重新启动之前，Servlet引擎将卸载Servlet，并在卸载之前调用Servlet的destroy()方法 

****

![Servlet调用图](.\cite\Servlet调用图.png)



### Servlet接口实现类

有两个默认的实现类：GenericServlet、[HttpServlet][2]

* HttpServlet 继承了GenericServlet

* HttpServlet指能够处理HTTP请求的servlet，它在原有Servlet接口上添加了一些与HTTP协议处理方法 
* 因此开发人员在编写Servlet时，通常应继承这个类，而避免直接去实现Servlet接口 
* HttpServlet在实现Servlet接口时，覆写了service方法，该方法体内的代码会自动判断用户的请求方式 
* 因此，开发人员在编写Servlet时，通常**只需要覆写doXXX方法**，而不要去覆写service方法 
* 点击查看[HelloServlet实现代码及WEB.xml配置][1]



### Servlet与普通Java类的区别

* Servlet是一个继承了Servlet接口的Java类
* Servlet是一个供其他Java程序（Servlet引擎）调用的Java类，它不能独立运行，它的运行完全由Servlet引擎来控制和调度 
* 针对客户端的多次Servlet请求，通常情况下，**服务器只会创建一个Servlet实例对象**，也就是说Servlet实例对象一旦创建，它就**会驻留在内存中**，为后续的其它请求服务，直至web容器退出，servlet实例对象才会销毁 （单例）
* 在Servlet的整个生命周期内，Servlet的**init方法只被调用一次** 
* 对一个Servlet的每次访问请求都导致Servlet引擎**调用一次servlet的service方法** 
* 对于每次访问请求，Servlet引擎都会创建一个新的HttpServletRequest请求对象和一个新的HttpServletResponse响应对象 
* 然后将这两个对象作为参数传递给它调用的Servlet的service()方法，service方法再根据请求方式分别调用doXXX方法 
* **destroy() 方法仅执行一次**，即在服务器停止且卸装 Servlet 时执行该方法。典型的，将 Servlet 作为服务器进程的一部分来关闭。缺省的 destroy() 方法通常是符合要求的，但也可以覆盖它，典型的是管理服务器端资源。例如，如果 Servlet 在运行时会累计统计数据，则可以编写一个 destroy() 方法，该方法用于在未装入 Servlet 时将统计数字保存在文件中。另一个示例是关闭数据库连接 



### load-on-startup属性

* 标记容器是否在启动的时候就加载这个servlet 
* 当值为0或者大于0时，表示容器在应用启动时就加载这个servlet 
* 当是一个负数时或者没有指定时，则指示容器在该servlet被选择时才加载 
* 非负数的值越小，启动该servlet的优先级越高 

```xml
<servlet>
    <description>This is the description of my J2EE component</description>
    <display-name>This is the display name of my J2EE component</display-name>
    <servlet-name>HelloServlet</servlet-name>
    <servlet-class>com.hhl.servlet.HelloServlet</servlet-class>
    <load-on-startup>0</load-on-startup>
</servlet>
```



###  DefaultServlet

* 默认的servlet是既服务于静态资源又服务于目录列表(如果允许目录列表的话)的servlet 
* 它在[$CATALINA_HOME/conf/web.xml][3]中被全局声明 ，其中定义了几个参数
  * DefaultServlet，DefaultServletMapping
  * JspServlet，JspServletMapping
  * sessionOutTime=30
  * welcome-file-list
* 开发者也可以在项目的WEB.XML中重新设置以上属性

```xml
<servlet-mapping>
    <servlet-name>default</servlet-name>
    <url-pattern>/static/*</url-pattern>
</servlet-mapping>
```



### Servlet线程安全问题

* 因为这个生命周期中，每个Servlet类只有一个实例
* 当Servlet类中，有全局变量时，就会产生线程安全问题
* 尽量避免Servlet类中有全局变量


### 参考文献

 [servlet到底是什么](https://blog.csdn.net/honghailiang888/article/details/50585475)

 [DefaultServlet](https://blog.csdn.net/zdhcumt/article/details/6867264)



[1]: ./cite/HelloServlet实现代码.md
[2]: ./cite/HttpServlet代码.md
[3]: ./cite/WebXml.md