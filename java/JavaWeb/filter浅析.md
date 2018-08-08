# Filter

## 定义

* 过滤器、拦截器
* WEB开发人员通过Filter技术，对web服务器管理的所有web资源：例如Jsp, Servlet, 静态图片文件或静态 html 文件等进行拦截 ，从而实现一些特殊的功能 
* 实现URL级别的权限访问控制、过滤敏感词汇、压缩响应信息、编码格式等一些高级功能 



## 特点

* Servlet过滤器可以检查和修改ServletRequest和ServletResponse对象 
* Servlet过滤器可以被指定和特定的URL关联，只有当客户请求访问该URL时，才会触发过滤器 
* Servlet过滤器可以被串联在一起，形成管道效应，协同修改请求和响应对象 



## 作用

* 查询请求并作出相应的行动 

* 阻塞请求-响应对，使其不能进一步传递 

* 修改请求的头部和数据。用户可以提供自定义的请求 

* 修改响应的头部和数据。用户可以通过提供定制的响应版本实现 

* 与外部资源进行交互 

  

## 生命周期

* 和Servlet一样Filter的创建和销毁也是由WEB服务器负责 
* 在应用启动的时候就进行装载Filter类 
* 容器创建好Filter对象实例后，调用init()方法。接着被Web容器保存进应用级的集合容器中去了等待着，用户访问资源 
* 当用户访问的资源正好被Filter的url-pattern拦截时，容器会取出Filter类调用doFilter方法 
* 当应用服务被停止或重新装载了，则会执行Filter的destroy方法，Filter对象销毁
* init方法与destroy方法只会直接一次 



## 适用场合

* 认证过滤 
* 登录和审核过滤 
* 图像转换过滤  
* 数据压缩过滤  
* 加密过滤  
* 令牌过滤  
* 资源访问触发事件过滤  
* XSL/T过滤  
* Mime-type过滤 



## 使用

* Servlet API中提供了一个Filter接口 
* 编写 Java类实现了这个接口，则把这个java类称之为过滤器Filter 
* 开发人员可以实现用户在访问某个目标资源之前，对访问的请求和响应进行拦截 ，进行一些自定义的操作
* 通过控制对chain.doFilter的方法的调用，来决定是否需要访问目标资源 
  * 判断用户是否有访问某些资源的权限，有权限放行，没权限不执行chain.doFilter方法 
* 通过在调用chain.doFilter方法之前，做些处理来达到某些目的 
  * 解决中文乱码的问题等等 
* 通过在调用chain.doFilter方法之后，做些处理来达到某些目的 
  * 比如对整个web网站进行压缩 之后在传输
* 如果有多个过滤器都匹配该请求，**顺序决定于web.xml filter-mapping的顺序**，在前面的先执行，后面的后执行  

```xml
<filter>
    <filter-name>baseFilter</filter-name>
    <filter-class>com.mango.filter.BaseFilter</filter-class>
</filter>

<filter-mapping>
    <filter-name>baseFilter</filter-name>
    <url-pattern>/*</url-pattern>
</filter-mapping>
```

```java
public class BaseFilter implements Filter {
 
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub 
	}
 
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String context = httpRequest.getContextPath();
		request.setAttribute("CONTEXT", context);
		chain.doFilter(request, response);
	}
 
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}
}
```



## 对请求过滤

* Servlet容器创建一个过滤器实例 
* 过滤器实例调用init方法，读取过滤器的初始化参数 
* 过滤器实例调用doFilter方法，根据初始化参数的值判断该请求是否合法 
* 如果该请求不合法则阻塞该请求 
* 如果该请求合法则调用chain.doFilter方法将该请求向后续传递 



## 对响应过滤

* 过滤器截获客户端的请求 
* 重新封装ServletResponse，在封装后的ServletResponse中提供用户自定义的输出流 
* 将请求向后续传递 
* Web组件产生响应 
* 从封装后的ServletResponse中获取用户自定义的**输出流** 
* 将响应内容通过用户自定义的**输出流写入到缓冲流中** 
* 在缓冲流中修改响应的内容后清空缓冲流，输出响应内容 



## 注意事项

* 由于Filter、FilterConfig、FilterChain都是位于javax.servlet包下，并非HTTP包所特有的，所以其中所用到的请求、响应对象ServletRequest、ServletResponse**在使用前都必须先转换成HttpServletRequest、HttpServletResponse**再进行下一步操作 
* 在web.xml中配置Servlet和Servlet过滤器，应该先声明过滤器元素，再声明Servlet元素 
* 如果要在Servlet中观察过滤器生成的日志，应该确保在server.xml的localhost对应的<host>元素中配置如下<logger>元素 

```xml
<Logger className = “org.apache.catalina.logger.FileLogger”
directory = “logs”prefix = “localhost_log.”suffix=”.txt”
timestamp = “true”/>
```



## 调用流程图

![调用流程图](.\cite\调用流程图.jpg)

```java

public void doFilter(ServletRequest req, ServletResponse resp,FilterChain chain) throws IOException, ServletException {  
    //再调用chain之前，对请求进行相关处理！！
    System.out.println("before encoding " + encoding + " filter！");  
    req.setCharacterEncoding(encoding);  
    chain.doFilter(req, resp);    
    //对响应进行处理！！
    System.out.println("after encoding " + encoding + " filter！");  
    System.err.println("----------------------------------------");  
} 

public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {  
    System.out.println("before the log filter!");  
    // 将请求转换成HttpServletRequest 请求!!!  
    HttpServletRequest hreq = (HttpServletRequest) req;  
    // 记录日志  
    System.out.println("Log Filter已经截获到用户的请求的地址:"+hreq.getServletPath() );  
    try {  
        // Filter 只是链式处理，请求依然转发到目的地址。  
        chain.doFilter(req, res);  
    } catch (Exception e) {  
        e.printStackTrace();  
    }  
    System.out.println("after the log filter!");  
}
```





## 参考文献

[Java Web中的Servlet及Filter](Java Web中的Servlet及Filter)

[ JavaWeb学习篇之----Servlet过滤器Filter和监听器 ](https://blog.csdn.net/jiangwei0910410003/article/details/23372847)