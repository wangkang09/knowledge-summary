# 实现对数据库连接池DataSource的初始化代码 

```java
import javax.servlet.ServletContext;   
import javax.servlet.ServletContextEvent;   
import javax.servlet.ServletContextListener;   
import org.apache.commons.dbcp.BasicDataSource;       
/**
 * 现在来说说Servlet的监听器Listener，它是实现了javax.servlet.ServletContextListener 接口的
 * 服务器端程序，它也是随web应用的启动而启动，只初始化一次，随web应用的停止而销毁。主要作用是：做一些初始化
 * 的内容添加工作、设置一些基本的内容、比如一些参数或者是一些固定的对象等等。
 * 
 * 示例代码：使用监听器对数据库连接池DataSource进行初始化
 */ 
public class ListenerTest implements ServletContextListener{     
   // 应用监听器的销毁方法   
   public void contextDestroyed(ServletContextEvent servletContextEvent) {   
        ServletContext servletContext = servletContextEvent.getServletContext();
        // 在整个web应用销毁之前调用，将所有应用空间所设置的内容清空
        servletContext.removeAttribute("dataSource");
        System.out.println("销毁工作完成...");  
   }   
    // 应用监听器的初始化方法   
    public void contextInitialized(ServletContextEvent servletContextEvent) {   
        // 通过这个事件可以获取整个应用的空间   
        // 在整个web应用下面启动的时候做一些初始化的内容添加工作   
        ServletContext servletContext = servletContextEvent.getServletContext();  
        // 设置一些基本的内容；比如一些参数或者是一些固定的对象   
        // 创建DataSource对象，连接池技术 dbcp   
        BasicDataSource basicDataSource = new BasicDataSource(); 
        basicDataSource.setDriverClassName("com.jdbc.Driver"); 
        basicDataSource.setUrl("jdbc:mysqlocalhost:3306/"); 
        basicDataSource.setUsername("root");   
        basicDataSource.setPassword("root");   
        basicDataSource.setMaxActive(10);//最大连接数   
        basicDataSource.setMaxIdle(5);//最大管理数   
        //bds.setMaxWait(maxWait); 最大等待时间   
        // 把 DataSource 放入ServletContext空间中，   
        // 供整个web应用的使用(获取数据库连接)
        servletContext.setAttribute("dataSource", basicDataSource);   
        System.out.println("应用监听器初始化工作完成...");   
        System.out.println("已经创建DataSource...");  
    }   
}

```

