[TOC]

# Spring Bean 过程

* 事务管理抽象层是Spring AOP的最佳实践



## IOC注入方式

* 构造方法注入：对于有多个构造器，只能声明一个构造器参数
* setter方法注入：相比构造方法注入，可以在对象构造完成后再注入，尤其可以避免循环依赖
* 接口注入：这个方式需要被依赖类实现依赖类的接口，代码入侵很大



## 传统方式、IOC方式、工厂模式的比较

```java
public class obj {
    private Isub sub;
    //getter,setter
}
public class obj {
    pirvate Isub = new Sub();//这里的创建可能很麻烦
}
public class obj {
    private Isub = Factory.getSub();
}
public calss Factory {
    public static Isub getSub() {
        return new Sub();//这里的创建可能很麻烦
    }
}
```

* 通过以上代码可以看出，如果依赖类改变时，传统方式就需要改动所有被依赖类中的代码



## IOC 容器如何管理对象

- 直接编码方式

```java
public static void main(String[] args) {
    DefaultListableBeanFactory beanRegistry = new DefaultListableBeanFactory();
    BeanFactory container = (BeanFactory)bindViaCode(beanRegistry);
    FxNewsProvider newsProvider = (FxNewsProvider)container.getBean("djNewsProvider");
    newsProvider.getAndPersistNews();
}
public static BeanFactory bindViaCode(BeanDefinitionRegistry registry) {
    //获取各个bean的定义
    AbstractBeanDefinition newsProvider = new RootBeanDifinition(FxNewsProvider.class,true);
    AbstractBeanDefinition newsListener = new RootBeanDifinition(DowJonesNewsListener.class,true);
    AbstractBeanDefinition newsPersister = new RootBeanDifinition(DowJonesNewsPersister.class,true);
    ...
    //将各个bean的定义注册到容器中
    registry.registerBeanDefinition("djNewsProvider",newsProvider);
    registry.registerBeanDefinition("djListener",newsListener);
    registry.registerBeanDefinition("djPersister",newsPersister);
    ...
    //依赖管理
    //1.通过构造方法注入
    ConstructorArgumentValues argValues = new ConstructorArgumentValues();
    argValues.addIndexedArgumentValue(0,newsListener);
    argValues.addIndexedArgumentValue(1,newsPersister);
    newsProvider.setConstructorArgumentValues(argValues);
    
    //2.通过setter方法注入
    MutablePropertyValues propertyValues = new MutablePropertyValues();
    propertyValues.addPropertyValue(new propertyValue("newsListener",newsListener));
    propertyValues.addPropertyValue(new PropertyValue("newsPersister",newsPersister));
    newsProvider.setPropertyValues(propertyValues);
    
    //绑定完成
    return (BeanFactory)registry;
}
```

- 配置文件方式
  - 根据不同的外部配置文件格式，给出相应的BeanDefinitionReader实现类
  - 由BeanDefinitionReader实现类解析对应配置文件，将内容映射到BeanDefinition中
  - 然后，将生成的BeanDefinition类注册到BeanDefinitionRegistry中
- 注解方式：可以算是编码方式的一种特殊实现



## Spring 容器

### 容器类型

spring提供了两种容器类型：BeanFactory，ApplicationContext

* BeanFactory，提供了完整的IOC服务支持，默认采用延迟初始化策略，只有当客户端访问容器中的对象时，才创建对象
* ApplicationContext，在BeanFactory的基础上提供了高级特性，如事件发布，国际化；默认容器启动后，bean全部初始化并绑定完成

### Bean的命名

* 每个Bean可以有一个id属性，并可以根据该id在IoC容器中查找该Bean，该id属性值必须在**IoC容器**中唯一 

* 可以不指定id属性，只指定全限定类名，如 ：

  ```xml
  <bean class="..bean0"></bean>
  <bean id="id0" class="..bean1">
      <property name="newBean">
         <bean class="..bean2" />
      </property>
      <constructor-arg>
      	<beam class="..bean3">
      </constructor-arg>
  </bean>
  ```
  * 可以通过getBean(Class<T> Type)来获取Bean
  * 如果不想其他对象通过ref引用bean2/3，可以不指定id属性

  ### XML浅析

* 可以在beans中统一指定
  * 初始化方式：default-lazy-init
  * 自动绑定类型： default-autowire
  * 依赖检查：default-dependency-check
  * 初始化/销毁方法：defailt-init/destroy-method

* 构造方法注入的XML
  * XML中定义的参数的顺序，默认是构造器参数的顺序
  * 可以在XML定义中指定 index="0"，指定参数顺序
  * 可以通过type="int"属性，指定特定的构造器

* <ref>属性
  * <ref bean>：通常情况下，直接使用bean来指定对象引用
  * <ref local>：只能指定与当前配置的对象在同一个配置文件的对象定义名称
  * <ref parent>：只能指定位于当前容器的父容器定义的对象引用

* <idref>属性

  ```xml
  <property name="bean1">
  	<value>helloBean</value>
  </property>
  <property name="bean1">
  	<idref bean="helloBean">
  </property>
  ```

  * 以上两种方式效果一样，都是注入的helloBean字符串给bean1属性
  * 但idref更好些

### Bean的实例化

* 第一步，生成BeanWrapper实例

  * 容器采用策略模式来决定采用何种方式初始化bean实例，如动态JDK，CGLIB
  * 完成初始化后，包装成beanWrapper实例返回

* 第二步，设置对象属性

  * 将之前CustomEditorConfigurer注册的PropertyEditor复制一份给BeanWrapper实例
  * 通过复制的内容进行设置属性

* 第三步，遍历查看对象实例是否实现了一系列的Aware接口

  * 当对象实例化完成并相关属性以及依赖设置完成后
  * 如果对象实例实现了相应的Aware接口，则将这些接口定义中规定的依赖注入给当前对象实例
  * BeanNameAware：将对象实例的bean定义对应的beanName设置到当前对象实例
  * BeanClassLoaderAware：将对象实例的Classloader注入到对象实例
  * BeanFactoryAware：将BeanFactory自身注入到当前对象实例，之后可以通过当前对象的属性，获取其他实例对象
  * ApplicationContextAware：同BeanFactoryAware
  * 国际化，事件发布

* 第四步，设置BeanPostProcessor

* 第五步，运行InitializingBean和init-method

  * 在调用BeanPostProcessor前置处理器后

  * 会接着检测当前对象是否实现了InitializingBean接口

  * 如果是，则会调用其afterPropertiesSet()方法，进行相关设置

    ```java
    public interface InitializingBean {
        void afterPropertiesSet() throws Exception;
    }
    ```

  * 如果XML bean中指定了init-method，执行该方法

* 第六步，DisposableBean与destroy-method

  * 同第五步

