## 第10章 深入理解 Session 与 Cookie ##
* Session 与 Cookie 的作用都是为了保持访问用户与后端服务器的交互状态，各有优缺点
	* Cookie 字节数大，当访问量很大时，占有带宽多
	* Session 不容易在多台服务器之间共享

### 10.1 Cookie 的定义 ###
* 当一个用户通过HTTP访问一个服务器是，这个服务器会将一些Key/Value键值对返回给客户端浏览器，并给这些数据加上一些限制条件（存活时间等）
* 在条件符合时，这个用户下次访问这个服务器时，数据又被完整地带回给服务器
* 如超市购物时，办理购物卡一样，下次直接就可识别购物卡了


* 当我们请求某个URL路径时，浏览器会根据这个URL路径，将符合条件的Cookie放在Request请求头中，传给服务端，服务端通过 request.getCookies()来取得所有Cookie

### 10.2 Cookie 的问题 ###
* 不同浏览器的Cookie的大小限制不同
* Cookie 管理的混乱
* 安全问题

### 10.3 Session 的定义 ###
* SessionId 是客户端第一次访问时，会在服务端生成一个session，包含一个sessionId属性。由tomcat生成的sessionId叫做jsessionId
* 是在访问tomcat服务器httpServletRequest的getSession(true)的时候创建的(如果内存中有直接返回，没有重新创建)
* tomcat的ManagerBase抽象类提供创建sessionId的受保护方法：随机数+时间+jvmId
* tomcat的StandardManager类管理session(存储在内存中)，定时检查，持久化(到file,数据库，一些缓存如redis)，失效等操作


* 当浏览器不支持Cookie时，浏览器会将用户的SessionCookieName 重写到用户请求的URL参数中
* SessionCookieName是在web.xml中配置session-config项中的cookie-config下的name属性就是sessionCookieName的值，默认就是“JSESSIONID”
* 服务端会将sessionID设置到cookie中
* 如果客户端支持cookie，Tomcat会解析Cookie中的SessionID，并覆盖URL中的SessionID

* 有一个后台进程会定期检查Session是否过期，并且每次调用request.getSession()时，如果没取到(即过期清除了)，则会创建一个新的session和新的sessionID,但里面的值已经没了
* <Manager pathname="" maxInactiveInterval="60" />设置过期时间，默认是60s，当为-1时，表示不过期

### 10.4 Cookie 的安全问题 ###
* Cookie 所有的数据都存在客户端中，通过HTTP的头部从客户端传到服务端，再从服务端传回客户端
* 这些数据可以被访问到，甚至被修改，所以很不安全
* 而Session数据是存在服务端的，客户端只保存一个SessionId，所以更适合存储用户隐私和重要数据

### 10.5 分布式Session框架 ###
* 通过一个订阅服务器集中管理
* 通过同步使每个节点保存完整的session集合

### 10.6 Cookie 的压缩和编解码 ###
* 通过压缩和编码使压缩后的的格式正确，且数据量小