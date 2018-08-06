## 7 Servlet 工作原理解析 ##
### 7.1 Web 应用的初始化工作 ###
* 应用的初始化主要是解析 web.xml 文件，这个文件描述了一个 Web 应用的关键信息，也是一个 Web 应用入口
* 将 WebXML 对象中的属性设置到 Context 容器中
	* Servlet对象
	* filter
	* listener