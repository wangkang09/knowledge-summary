## 11 Tomcat 的系统架构和设计模式 ##
* Tomcat 有两个核心组件：Connector 和 Container
* 多个Connector 和一个 Container 就行成了一个 Service，可以对外提供服务了
* Service 需要一个生存环境，有Server 提供，Tomcat 的生命周期由Server控制

* Tomcat 的各个组件是通过Pipeline连在一起的，而各个组件的功能都封装在各个Pipeline的BaseValue中

### 11.1 Service ###
* Connector 负责和外部交流，Container 负责处理内部事务
* Service 将它们包装起来，并添加了一些其他元素，向外面提供服务
* 在 Tomcat 中 Service 接口的标准实现类是 StandardService，它不仅实现了 Service接口，同时还实现了 Lifecycle接口，这样就可以控制下面组件的生命周期了
* Service将Connector放在一个数组中

### 11.2 Server ###
* 提供一个接口，让其他程序能够访问到这个 Service 集合
* 同时维护它包含的所有Service的生命周期，包括如何初始化、如何结束服务、如何找到别人要访问的Service
* 它的标准实现类是 StandardServer，同时也实现了 Lifecycle、MbeanRegistration接口方法
* Server也像 Service管理Connector一样，将Service放在一个数组中

### 11.3 Lifecycle 组件的生命线 ###
* Tomcat 组件的生命周期是通过 Lifecycle 接口来控制的，组件主要继承这个接口并实现其中的方法就可以统一被拥有它的组件控制了
* 直到最高级的组件(Server)就可以控制 Tomcat 中的所有组件的生命周期了
* 而控制Server的是Startup，也就是启动和关闭Tomcat
* 父组件的start方法，调用子组件的start方法，同理stop
* 监听代码会包围Service组件的启动、关闭过程

---
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

### 11.4 Connector  ###
* 负责接收浏览器发送过来的TCP连接请求，创建一个 Request 和 Response 对象，分别用于和请求端交换数据
* 然后会产生一个线程来处理这个请求，并把产生的 Request 和 Response 对象传给这个请求的线程
* 处理这个请求的线程由 Container 组件完成
* Connector 最重要的功能就是接收请求，然后分配线程让 Container 来处理，所以是多线程的，这是Connector设计的核心

### 11.5 Container ###
* Container 是容器的父接口，所有子接口必须实现这个接口
* 容器的设计是典型的责任链设计模式
	* 由4个子容器组件构成，Engine、Host、Context和Wrapper
	* 这4个组件不是平行的，而是父子关系
	* Engine 包含 Host，Host 包含 Context，Context 包含 Wrapper
	* 通常一个 Servlet Class 对应一个 Wrapper

* Engine
	* 决定从Connector过来的请求交给那个Host来处理
	* StandardEngineValue只是从request中拿到Host，然后调用Host组件的Pipeline
* Host
	* 代表Engine下的一个虚拟主机，用于运行多个应用
	* 负责安装和展开这些应用，并标识这个应用，以便能够区分它们
* Context
	* 提供Servlet运行的基本环境，各种资源组件和管理主机，启动子容器和pipeline
	* 理论上只要有Context就能运行Servlet，可以没有Engine和Host
	* 管理Servlet实例，包装Servlet实例成Wrapper对象
	* 有个reloadable属性，当为true时，war被修改后Tomcat会自动重新加载这个应用
	* 这个功能是在ContainerBase类中内部类中实现周期调用的，因为所以容器都会继承ContainerBase类，所以能够监听所有Servlet
* Wrapper
	* Servlet的包装类，负责管理一个Servlet，包括装载、初始化、执行及资源回收
	* 装载Servlet后就会调用Servlet的init方法，同时会传一个StandardWrapperFacade对象给Servlet
	* Servlet初始化完成后，就等着StandardWrapperValue去调用它的Service方法了
	* 调用Service方法之前，要调用Servlet所有的filter


### 11.6 Tomcat 其他组件 ###
* Logger：负责记录各种事件
* Loader：负责加载类文件，如加载应用程序中的Servlet
* Manager：负责管理Session
* Realm：负责用户验证与授权
* Pipeline：负责完成容器invoke方法的调用，对请求进行处理(责任链模式的经典应用)

* 当Tomcat容器启动时，这些组件也要

