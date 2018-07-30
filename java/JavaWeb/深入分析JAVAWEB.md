# 深入分析 Java Web #

## Web请求过程 ##
### B/S与C/S网络架构 ###
* C/S
	* Client/Server，客户端/服务端，如手机中的应用：QQ，微博
	* 采用长连接的交互方式
	* 使用自定义的应用层协议
* B/S
	* Browser/Server，浏览器/服务器，网页
	* 客户端使用统一的浏览器，不需要特殊的配置和网络连接
	* 服务端基于统一的HTTP，规范开发模式，节省开发成本
		* 基于HTTP的服务器：Apache、IIS、Tomcat、Nginx、JBoss
		* 这些服务器可以直接拿来使用，不需要单独开发