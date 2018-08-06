# 深入分析 Java Web #

## 1 Web请求过程 ##
### 1.1 B/S与C/S网络架构 ###
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

### 1.2 URL和URI的区别 ###
* URL是URI的一种，URI不一定是URL
* 让URI能成为URL的当然就是那个“访问机制”，“网络位置”
	* http:// or ftp:// or ldap:// ...
* URI可以分为URL,URN或同时具备locators 和names特性的一个东西
	* 可以没有URL，但是必须有URN
	* URN可以当作一个人的名字
	* URL可以当作一个人的地址
* URI：uniform resource identifier，统一资源标识符，用来唯一的标识一个资源
* URL：uniform resource locator，统一资源定位器，它是一种具体的URI，即URL可以用来标识一个资源，而且还指明了如何locate这个资源
* URN：uniform resource name，统一资源命名，是通过名字来标识资源

### 1.3 如何发起一个请求 ###
* 如何发起一个HTTP请求和如何建立一个Socket连接区别不大
* 只不过 outputStream.write**写的二进制数据格式要符合HTTP**
* 在建立连接之前先DNS出IP地址和端口，再与远程服务器建立Socket连接
* 通过outputStream.write将请求发送到目标服务器，等待inputStream.read返回数据
* 最后断开这个连接

### 1.4 浏览器缓存机制 ###
* 如果浏览页面发现异常情况，通常考虑是不是浏览器做了缓存
* 按 CTRL+F5刷新页面，浏览器会直接向目标URL发送请求，而不会使用浏览器缓存的数据
* 但是若应用服务器前端部署了一个缓存服务器，可能访问到的仍然是缓存数据
* 为了保证数据的最新，必须通过HTTP控制

**在请求头中加配置项**  

* Cache-Control/Pragma
	* no-cache:所用内容不会被缓存
* Expires
	* 后面更一个时间，超过该时间，缓存失效
* Last-Modified/Etag 

### 1.5 DNS 域名解析 ###
1. 浏览器先检查缓存中有没有这个域名对应的解析过的IP地址，如果有结束
	1. 浏览器缓存域名是有限制的，缓存大小和时间都可以设定，但时间太长，ip变化后无法访问，太短则每次都要重新解析
2. 如果缓存没命中，浏览器会查找操作系统缓存中是否有这个域名对应的DNS解析结果！hosts文件中，自己可以灵活的设定
3. 如果本地没命中，则请求域名服务器
	1. 我们的网络配置中都会有 “DNS服务器地址”这一项
4. 如果LDNS仍然没有命中，就直接到Root Server域名服务器请求解析
5. 根域名服务器返回给本地域名服务器一个所查询域的**主域名服务器**（gTLD Server）地址
	1. gTLD是国际顶级域名服务器，如：.com、.cn、.org等
6. 本地域名服务器（Local DNS Server）再向上一步返回的gTLD服务器发送请求
7. 接受请求的gTLD服务器查询并返回此域名对应的Name Server域名服务器的地址
	1. 这个Name Server 通常就是你注册的域名服务器，域名提供商的服务器来完成解析
8. Name Server 域名服务器会查询存储的域名和IP的映射关系表
	1. 正常情况下都根据域名得到目标IP记录，连同一个TTL值返回给DNS Server域名服务器
9. 返回该域名对应的IP和TTL值，Local DNS Server 会缓存这个域名和IP的对应关系，时间有TTL值控制
10. 把解析结果返回给用户，用户根据TTL值缓存在本地系统中，域名解析过程结束

#### 1.5.1 清楚缓存的域名 ####
* ipconfig/flushdns --win刷新缓存
* /etc/init.d/nscd restart --linux刷新缓存

#### 几种域名解析方法 ####
* A记录
* MX记录
* CNAME记录
* NS记录
* TXT记录


### 1.6 CDN工作机制 ###
* 内容分布网络（Content Delivery Network），是构建在internet的一种先进的流量分配网络
* CDN=镜像+缓存+整体负载均衡
* 以缓存网站中的静态数据为主
	* CSS、JS、图片和静态页面等数据
	* 用户在从主站服务器请求到动态内容后，再从CDN上下载这些静态数据，从而加速网页数据内容的下载速度

#### 1.6.1 CDN达到的目标 ####
* 可扩展性
	* 性能可扩展：应对新增大量数据的扩展能力
	* 成本可扩展：
* 安全性：减少DDOS等恶意攻击
* 可靠性、响应和执行

#### 1.6.2 CDN动态加速 ####
* 在CDN的DNS解析中，通过动态的链路探测来寻找回源最好的一条路径，从而加速用户的访问效率

## 2 Java I/O 工作机制 ##
I/O是整个人机交互的核心问题，且I/O问题尤其突出，很容易成为一个性能瓶颈
### 2.1 I/O 分类： ###
* 字节操作：InputStream 和 OutputStream
* 字符操作：Writer 和 Reader
* 磁盘操作：File
* 网络操作：Socket
* **数据持久化或网络传输都是以字节进行的，所以必须要有从字符到字节或从字节到字符的转化**
	* 转化过程要经过编解码，不久耗时，而且经常出现乱码问题，很头疼
	* InputStreamReader是从字节到字符，如果不指定编码，则使用默认编码
	* 其中StreamDecoder和StreamEncoder完成编解码过程
### 2.2 I/O 核心问题 ###
* 数据格式影响I/O操作
* 传输方式影响I/O操作
* 也就是将什么样的数据，写到什么地方

### 2.3 磁盘I/O工作机制 ###
* 读取和写入文件I/O操作，都要调用操作系统提供的接口，因为磁盘设备是有操作系统管理的
* 读写分别对应read(),write()两个系统调用
* 只要是系统调用就可能存在内核空间地址和用户空间地址切换的问题
* 这样就会存在，数据从磁盘复制到内核空间，再从内核空间复制到用户空间，会非常缓慢
* 操作系统为了加速I/O访问，在内核空间使用缓存机制

#### 2.3.1 标准访问文件方式 ####
* 读取操作
	* 先看内核缓存中有没有，如果有直接返回
	* 没有就去磁盘中读取，然后缓存在内核中
* 写入操作
	* 将数据从用户地址空间**复制到内核缓存中**，这是写操作已经完成，至于什么时候写到磁盘中有操作系统决定，除非显示调用了sync同步命令

#### 2.3.2 直接I/O方式 ####
* 应用程序直接访问磁盘数据，不经过操作系统内核缓冲，目的是减少一次数据复制
* 如数据库，可以自己管理缓存热点数据，而操作系统很难做到缓存特定的数据
* 通常直接I/O和异步I/O结合使用

#### 2.3.3 同步访问文件方式 ####
* 数据的读取、写入同时同步操作
* 只有当数据被成功写入磁盘后，才返回给应用程序成功的标志
* 性能比较差，一般用于数据安全性很高的场景

#### 2.3.4 异步访问方式 ####
* 当线程发送数据请求后，会直接去执行其他事务，而不是阻塞等待

#### 2.3.5 内存映射方式 ####
* 即内核缓存和磁盘数据时共享的
* 也是为了减少数据复制

### 2.4 Java 访问磁盘文件 ###
* 通过FileInputStream来访问文件
* 其中有StreamDecoder来完成字节到字符的转化

### 2.5 Java 序列化技术 ###
* 当父类继承Serializable接口时，所有子类都可以被序列化
* 子类实现了Serializable接口，父类没有，父类中的属性不能序列化（不报错，但数据丢失），子类中的属性仍能正确序列化
* 如果序列化的属性是对象，则这个对象也必须实现Serializable接口，否则会报错
* 在反序列化时，如果对象的属性有修改和删减，则修改部分属性会丢失，但不会报错
* 在反序列化时，如果serialVersionUID被修改，则反序列化时会失败

### 2.6 网络I/O工作机制 ###
#### 2.6.1 TCP 状态转换 ####
* closed：起始点，在超时或者连接关闭时，进入此状态
* listen：Server 端在等待连接时的状态
* syn-sent：客户端发起连接，发送syn给服务端，如果服务端不能连接，直接进入closed状态
* syn-rcvd：服务端接收到客户端syn请求，状态有listen->syn-rcvd。同时回应一个ack，发送一个syn给客户端；另一种情况，客户端在发起syn的同时接收到了服务端的syn，客户端会有syn-sent，转化为syn-rcvd
* established：服务端和客户端完成3次握手进入后的状态，说明已经可以传输数据了
* time-wait：

#### 2.6.2 影响网络传输的因素 ####
* 网络带宽：即一条物理链路在1s内能够传输的最大比特数（b/s）
* 传输距离：在光纤中因为折射率，速度大概是2/3，如果在杭州和青岛的两个机房同步数据，必定存在30ms的传输延时
* TCP拥塞控制：TCP传输方和接收方用拥塞控制达到步调一致，会设定一个窗口（BDP）

#### 2.6.3 Java Socket 的工作机制 ####
* Socket是一个抽象的概念，可以把它比作两个城市之间的交通工具，有了它，才可以在城市之间来回穿梭
* Socket如交通工具一样，有多种，大部分使用TCP/IP的流套接字，他是一个稳定的通信协议

#### 2.6.4 建立通信链路 ####
* 客户端要与服务端通信是，客户端首先要创建一个Socket实例，操作系统将为这个Socket实例分配一个没有被使用的本地端口号，并**创建一个包含本地地址、远端地址和端口号的套接字数据结构**，这个数据结构一直保存在系统中，直到连接关闭
* 之后要 进行TCP的3次握手协议，完成后，Socket才创建完成，否则，抛IOException错误
* 在完成3次握手后，server端会创建一个新的Socket专门和这个客户端连接

#### 2.6.5 数据传输 ####
* 连接建立成功后，客户端和服务端都有一个Socket实例，每个都有一个inputStream和outputStream，并通过这两个对象来交换数据
* 网络I/O是通过字节流传输，会有一定大小的缓存区，数据的写入和读取都是通过这个缓存区完成的
* 写入端将数据写到outputstream对象的sendQ队列中，**当队列填满时，数据将被转移到另一端的inputStream的RecvQ队列中**
* 如果这时，recvQ队列已经满了，则outputStream的write方法会被阻塞，直到RecvQ队列有足够的空间容纳sendQ发送的数据
* 缓存区的大小、写入端/读取端的速度，非常影响连接的数据传输效率
* 如果两边同时传输数据可能会产生死锁，NIO将避免此情况

### 2.7 NIO 工作方式 ###
#### 2.7.1 BIO 带来的挑战 ####
* BIO即阻塞IO，不管是磁盘I/O，还是网络I/O，数据写入和读取时都可能会阻塞

#### 2.7.2 NIO 工作机制 ####
* NIO有三个核心的概念
	* Channel：相比socket更具体，可以把它当作具体的交通工具
	* Buffer：相比Stream更加具体，channel是汽车的话，Buffer就是汽车上的座位
	* Selector：是车辆调度系统，监控每辆车运行状态

* NIO模式用户可以控制很多参数
* 关键是，有一个线程来处理所有连接的数据交互，每个连接的数据交互都不是阻塞方式，所以可以同时处理大量的连接请求

* Buffer是取的Socket缓存区的数据，即RecvQ,SendQ，也可以设置直接取操作系统缓存数据

#### 2.7.3 NIO的数据访问方式 ####
* NIO有两个优化的方法
	* FileChannel.transferXXX
		* 减少数据从内核到用户空间的复制，数据直接在内核空间移动
	* FileChannel.map
		* 内存映射，适合大文件的只读性操作，如大文件的MD5校验

## 3 Java Web 中的中文编码问题

### 3.1 为什么要编码

- 计算机存储信息的最小单元是1byte，即8个bit，所以只能表示的字符范围是0-225个
- 人类表示的符号太多，无法用1个byte完全表示
- 必须要有一个新的数据结构char，从char到byte必须编码
- char的字节数从1-8个不等，根据编码方式

### 3.2 几种常见的编码方式

- ASCII码
  - 共用128个，用一个字节的低7位表示
- ISO-8859-1
  - 扩展ASCII码，仍然是单字节，有256个字符
- GB2312
  - 双字节编码
- GBK
  - 扩展GB2312加入了更多的汉子
- UTF-16
  - 定义了Unicode字符在计算机中的存取方法
  - 用两个字节表示Unicode的转化格式，为定长方法
  - 是Java 和 XML 的基础
  - 每两个字节表示一个字符，大大简化了字符串操作
- UTF-8
  - 变长编码

### 3.3 在I/O操作中存在的编码

- Reader类读的是字符，InputStream 读的是字节
- 解码工作交给了StreamDecoder
- 中文环境默认GBK编码
- 不建议使用默认编码，跨环境是会出现乱码

### 3.4 在内存操作中的编码

- Java中用String表示字符串，所以String类中就提供了转换到字节的方法

------

```
String s = "这是一段中文字符串";
byte[] b = s.getBytes("UTF-8");
String n = new String(b,"UTF-8");
```

- char[] 到 byte[]的编码
- ByteBuffer类提供，char到byte的软转换

### 3.5 在Java Web 中的编解码

#### 3.5.1 URL的编解码

- 每个浏览器对URL的编码都不太一样
- 以Tomcat为例：
- URL的URI部分进行解码的字符集是在connector的<Connector URIEncoding="UTF-8"/>中设置的，默认是 ISO-8859-1，所以在有中文URL时，最好把URIEncoding设置成UTF-8编码
- QueryString的解码，要么使用header中的ContentType定义的CharSet，要么为默认值ISO-8859-1
- 如果要使用contentType中的编码，则要<Connector URIEncoding="UTF-8" useBodyEncodingForURI="true"/>

#### 3.5.2 HTTP Header 的编解码

- 默认是ISO-8859-1
- 最好不要在Header中传递非ASCII字符

#### 3.5.3 POST 表单的编解码

- post表单的参数传递方式与QueryString 不同，是通过HTTP 的 BODY 传递到服务端的
- 浏览器根据 ContentType的CharSet对表单进行编码
- 服务器端也是根据ContentType的CharSet进行解码，所以一般不会出错

#### 常见问题

- 中文变成了看不懂的字符
  - 在解码时，字符集与编码字符集不一致会导致，一个汉字字符变成两个乱码字符
- 一个汉字变成一个问号
  - 中文和正文字符经过不支持中文的ISO-8859-1编码后，遇到不在范围的值统一用3f表示
- 一个汉字变成两个问号

## 4 Javac 编译原理

- Java 语言有Java语言规范，这个规范详细描述了Java语言有哪些词法和语法
- Java虚拟机也有Java虚拟机规范
- 这两个规范完全不是一回事，都有自己的词法和语法解析规范
- **Javac编译器的任务就是将Java语言规范转换成Java虚拟机规范，完成翻译工作**

### 4.1 Javac 是什么

- Javac 是一种编译器，能将一种语言规范转换成另一种语言规范
- Javac 的任务是将Java源代码语言先转换成JVM能够识别的语言（Java字节码，JVM能够识别的二进制码），**然后JVM将JVM语言再转换成机器语言**
- JVM 消除了不同种类，不同平台机器之间的差异，使Java 能够一次编译，处处运行

### 4.2 Javac 编译器的基本模块

- 词法分析器
  - 读取源码，找出其中的语法关键字，识别哪些是合法的，哪些不合法
  - 形成一些规范化的Token流
- 语法分析器
  - 对Token流进行分析，检查这些关键字组合在一起是否符合Java语言规范
  - 形成一个符合Java语言规范的抽象语法树
- 语义分析器
  - 把一些复杂的语法转换成简单的语法，如foreach，注解等
  - 使得语法树更接近目标语言的语法规则
- 代码生成器
  - 通过字节码生成器生成字节码，将抽象语法树转换成字节码

## 7 Servlet 工作原理解析

### 7.1 Web 应用的初始化工作

- 应用的初始化主要是解析 web.xml 文件，这个文件描述了一个 Web 应用的关键信息，也是一个 Web 应用入口
- 将 WebXML 对象中的属性设置到 Context 容器中
  - Servlet对象
  - filter
  - listener

## 10 深入理解 Session 与 Cookie

- Session 与 Cookie 的作用都是为了保持访问用户与后端服务器的交互状态，各有优缺点
  - Cookie 字节数大，当访问量很大时，占有带宽多
  - Session 不容易在多台服务器之间共享

### 10.1 Cookie 的定义

- 当一个用户通过HTTP访问一个服务器是，这个服务器会将一些Key/Value键值对返回给客户端浏览器，并给这些数据加上一些限制条件（存活时间等）
- 在条件符合时，这个用户下次访问这个服务器时，数据又被完整地带回给服务器
- 如超市购物时，办理购物卡一样，下次直接就可识别购物卡了

- 当我们请求某个URL路径时，浏览器会根据这个URL路径，将符合条件的Cookie放在Request请求头中，传给服务端，服务端通过 request.getCookies()来取得所有Cookie

### 10.2 Cookie 的问题

- 不同浏览器的Cookie的大小限制不同
- Cookie 管理的混乱
- 安全问题

### 10.3 Session 的定义

- SessionId 是客户端第一次访问时，会在服务端生成一个session，包含一个sessionId属性。由tomcat生成的sessionId叫做jsessionId
- 是在访问tomcat服务器httpServletRequest的getSession(true)的时候创建的(如果内存中有直接返回，没有重新创建)
- tomcat的ManagerBase抽象类提供创建sessionId的受保护方法：随机数+时间+jvmId
- tomcat的StandardManager类管理session(存储在内存中)，定时检查，持久化(到file,数据库，一些缓存如redis)，失效等操作

- 当浏览器不支持Cookie时，浏览器会将用户的SessionCookieName 重写到用户请求的URL参数中
- SessionCookieName是在web.xml中配置session-config项中的cookie-config下的name属性就是sessionCookieName的值，默认就是“JSESSIONID”
- 服务端会将sessionID设置到cookie中
- 如果客户端支持cookie，Tomcat会解析Cookie中的SessionID，并覆盖URL中的SessionID
- 有一个后台进程会定期检查Session是否过期，并且每次调用request.getSession()时，如果没取到(即过期清除了)，则会创建一个新的session和新的sessionID,但里面的值已经没了
- <Manager pathname="" maxInactiveInterval="60" />设置过期时间，默认是60s，当为-1时，表示不过期

### 10.4 Cookie 的安全问题

- Cookie 所有的数据都存在客户端中，通过HTTP的头部从客户端传到服务端，再从服务端传回客户端
- 这些数据可以被访问到，甚至被修改，所以很不安全
- 而Session数据是存在服务端的，客户端只保存一个SessionId，所以更适合存储用户隐私和重要数据

### 10.5 分布式Session框架

- 通过一个订阅服务器集中管理
- 通过同步使每个节点保存完整的session集合

### 10.6 Cookie 的压缩和编解码

- 通过压缩和编码使压缩后的的格式正确，且数据量小

## 11 Tomcat 的系统架构和设计模式

- Tomcat 有两个核心组件：Connector 和 Container
- 多个Connector 和一个 Container 就行成了一个 Service，可以对外提供服务了
- Service 需要一个生存环境，有Server 提供，Tomcat 的生命周期由Server控制
- Tomcat 的各个组件是通过Pipeline连在一起的，而各个组件的功能都封装在各个Pipeline的BaseValue中

### 11.1 Service

- Connector 负责和外部交流，Container 负责处理内部事务
- Service 将它们包装起来，并添加了一些其他元素，向外面提供服务
- 在 Tomcat 中 Service 接口的标准实现类是 StandardService，它不仅实现了 Service接口，同时还实现了 Lifecycle接口，这样就可以控制下面组件的生命周期了
- Service将Connector放在一个数组中

### 11.2 Server

- 提供一个接口，让其他程序能够访问到这个 Service 集合
- 同时维护它包含的所有Service的生命周期，包括如何初始化、如何结束服务、如何找到别人要访问的Service
- 它的标准实现类是 StandardServer，同时也实现了 Lifecycle、MbeanRegistration接口方法
- Server也像 Service管理Connector一样，将Service放在一个数组中

### 11.3 Lifecycle 组件的生命线

- Tomcat 组件的生命周期是通过 Lifecycle 接口来控制的，组件主要继承这个接口并实现其中的方法就可以统一被拥有它的组件控制了
- 直到最高级的组件(Server)就可以控制 Tomcat 中的所有组件的生命周期了
- 而控制Server的是Startup，也就是启动和关闭Tomcat
- 父组件的start方法，调用子组件的start方法，同理stop
- 监听代码会包围Service组件的启动、关闭过程

------

```
public void start() throws LifecycleException {
	if(started) {
		log.debug(sm.getString("standardServer.start.started"));
		return;
	}
	lifecycle.fireLifecycleEvent(BEFORE_START_EVENT,null);	
	lifecycle.fireLifecycleEvent(START_EVENT,null);
	started = true;
	synchronized(services) {
		for(int i=0; i<services.length; i++) {
			if(services[i] instanceof Lifecycle) {
				((Lifecycle) servicees[i]).start();
			}
		}
	}
	lifecycle.fireLifecycleEvent(AFTER_START_EVENT,null);
}
```

### 11.4 Connector

- 负责接收浏览器发送过来的TCP连接请求，创建一个 Request 和 Response 对象，分别用于和请求端交换数据
- 然后会产生一个线程来处理这个请求，并把产生的 Request 和 Response 对象传给这个请求的线程
- 处理这个请求的线程由 Container 组件完成
- Connector 最重要的功能就是接收请求，然后分配线程让 Container 来处理，所以是多线程的，这是Connector设计的核心

### 11.5 Container

- Container 是容器的父接口，所有子接口必须实现这个接口
- 容器的设计是典型的责任链设计模式
  - 由4个子容器组件构成，Engine、Host、Context和Wrapper
  - 这4个组件不是平行的，而是父子关系
  - Engine 包含 Host，Host 包含 Context，Context 包含 Wrapper
  - 通常一个 Servlet Class 对应一个 Wrapper
- Engine
  - 决定从Connector过来的请求交给那个Host来处理
  - StandardEngineValue只是从request中拿到Host，然后调用Host组件的Pipeline
- Host
  - 代表Engine下的一个虚拟主机，用于运行多个应用
  - 负责安装和展开这些应用，并标识这个应用，以便能够区分它们
- Context
  - 提供Servlet运行的基本环境，各种资源组件和管理主机，启动子容器和pipeline
  - 理论上只要有Context就能运行Servlet，可以没有Engine和Host
  - 管理Servlet实例，包装Servlet实例成Wrapper对象
  - 有个reloadable属性，当为true时，war被修改后Tomcat会自动重新加载这个应用
  - 这个功能是在ContainerBase类中内部类中实现周期调用的，因为所以容器都会继承ContainerBase类，所以能够监听所有Servlet
- Wrapper
  - Servlet的包装类，负责管理一个Servlet，包括装载、初始化、执行及资源回收
  - 装载Servlet后就会调用Servlet的init方法，同时会传一个StandardWrapperFacade对象给Servlet
  - Servlet初始化完成后，就等着StandardWrapperValue去调用它的Service方法了
  - 调用Service方法之前，要调用Servlet所有的filter

### 11.6 Tomcat 其他组件

- Logger：负责记录各种事件
- Loader：负责加载类文件，如加载应用程序中的Servlet
- Manager：负责管理Session
- Realm：负责用户验证与授权
- Pipeline：负责完成容器invoke方法的调用，对请求进行处理(责任链模式的经典应用)
- 当Tomcat容器启动时，这些组件也要

