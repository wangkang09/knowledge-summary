# HelloServlet实现代码

```java
public class HelloServlet extends HttpServlet {
 
	/**
	 * 
	 */
	private static final long serialVersionUID = -468861535655735406L;
 
	/**
	 * Constructor of the object.
	 */
	public HelloServlet() {
		super();
	}
 
	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}
 
	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
			PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<HTML>");
		out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
		out.println("  <BODY>");
			out.print("    This is ");
			out.print(this.getClass());
			out.println(", using the GET method");
		out.println("  </BODY>");
		out.println("</HTML>");
			out.flush();
			out.close();
		
	}
 
	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
 
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<HTML>");
		out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
		out.println("  <BODY>");
		out.print("    This is ");
		out.print(this.getClass());
		out.println(", using the POST method");
		out.println("  </BODY>");
		out.println("</HTML>");
		out.flush();
		out.close();
	}
 
	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		System.out.println("servlet init");
	}
 
 
}

```

* 关键是复写了 doGet、doPost方法
* 其中的out.println()很重要，实现客户端与服务端的流交互



# web.xml中的Servlet配置

```xml

<servlet>
    <description>This is the description of my J2EE component</description>
    <display-name>This is the display name of my J2EE component</display-name>
    <servlet-name>HelloServlet</servlet-name>
    <servlet-class>com.hhl.servlet.HelloServlet</servlet-class>
</servlet>
<servlet-mapping>
    <servlet-name>HelloServlet</servlet-name>
    <url-pattern>/</url-pattern>
</servlet-mapping>
```

* 同一个Servlet可以被映射到多个URL中

```xml
<servlet-mapping>
    <servlet-name>HelloServlet</servlet-name>
    <url-pattern>/</url-pattern>
</servlet-mapping>
<servlet-mapping>
    <servlet-name>HelloServlet</servlet-name>
    <url-pattern>/1.html</url-pattern>
</servlet-mapping>
```

* 也可以使用通配符设置映射关系