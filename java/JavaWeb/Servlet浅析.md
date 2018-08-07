##  Servlet

###  定义：

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



### 参考文献

 [servlet到底是什么](https://blog.csdn.net/honghailiang888/article/details/50585475)