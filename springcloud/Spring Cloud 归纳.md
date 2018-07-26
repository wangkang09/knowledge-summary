## Spring Cloud 总纲 ##

**1. Spring Cloud Netflix**  

1.1 Netflix Eureka  
　　服务治理，一个基于 REST 的服务，用于服务注册、服务发现，实现云端的负载均衡和中间层服务器的故障转移。  
1.2 Netflix Zull  
　　边缘服务工具，网关组件，提供动态路由，过滤，监控，弹性，安全等的边缘服务。  
1.3 Netflix Hystrix      
　　容错管理工具，旨在通过控制服务和第三方库的节点，从而对延迟和故障提供更强大的容错能力。  
1.4 Ribbon  
　　客服端负载均衡的服务调用组件。  
1.5 Feign  
　　基于 Ribbon 和 Hystrix 的声明式服务调用组件。  
1.6 Archaius  
　　外部化配置组件，配置管理API，提供动态类型化属性、线程安全配置操作、轮询框架、回调机制等功能。

**2. Spring Cloud Config**  
　　配置管理工具，支持使用Git存储配置内容，可以使用它实现应用配置的外部化存储，并支持客户端配置信息刷新、加密/解密等。  

**3. Spring Cloud Bus**  
　　事件、消息总线，用于在集群中传播状态变化，可与 Spring Cloud Config 联合实现热部署。通常会使用轻量级消息代理来构建一个共用的消息主题让系统中所有微服务实例都连接上来，由于该主题中产生的消息会被所有实例监听和消费，所以称为**消息总线**。    
　　Spring Cloud Bus 在绑定具体消息代理的输入输出通道时，均使用了抽象接口的方式，正真的实现来自于 starter-bus-amqp 和 starter-bus-kafka 而这两个包又依赖了 starter-stream-rabbit 和 starter-stream-kafka，<font color=red>正真实现与这些消息代理进行交互操作的是 Spring Cloud Stream</font>。  
　　可以将 Spring Cloud Bus 理解为一个使用了 Spring Cloud Stream 构建的上层应用。

**4. Spring Cloud Stream**  
　　1）<font color=blue>通过 Stream 我们可以很方便的使用各个消息中间件</font>  
　　2）Stream 让开发者屏蔽各个消息代理之间的差异，能够很方便地切换不同的消息代理，而不影响业务，<font color=blue>它在业务程序和消息代理之间定义了一层抽象</font>，称为<font color=red>绑定器</font>  
　　3）我们要使用其他消息代理时，只需要实现一套指定消息代理的绑定器即可

**5. Spring Cloud Sleuth**  
　　1）日志的分布式跟踪实现  
　　2）整合 Logstash：实现日志的集中收集  
　　3）整合 ElasticSearch、Kibana 实现日志的搜索与分析，统称 ELK  
　　4）整合 Zipkin 实现分布式系统的延迟监控和其他与时间有关的需求
　　5）整合消息中间件，实现日志的异步收集  
　　6）整合数据库，实现日志的持久化  

**6. Spring Cloud Consul**  
　　封装了 Consul 操作，Consul 是一个服务发现与配置管理工具，与 Docker 容器可以无缝集成  

**7. Spring Cloud Zookeeper**  
　　封装 Zookeeper，用于 Zookeeper 方式的服务注册与发现  

**8. Spring Cloud Security**  
　　安全工具包，为应用程序添加安全控制，主要是指OAuth2

**9. Spring Cloud AWS**  
　　用于简化整合 Amazon Web Service 的组件  

**10. Spring Cloud CLI**  
　　用于在 Groovy 中快速创建 Spring Cloud 应用的 Spring Boot CLI 插件