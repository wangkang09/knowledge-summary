[TOC]

# Java序列化

## 定义

* Java序列化是指把Java对象保存为二进制字节码的过程
* Java反序列化是指把二进制码重新转换成Java对象的过程



## Java序列化的场景

* 一般情况下Java对象的声明周期都比Java虚拟机的要短，实际应用中我们希望在JVM停止运行之后能够持久化指定的对象，这时候就需要把对象进行序列化之后保存 
* 需要把Java对象通过网络进行传输的时候。因为数据只能够以二进制的形式在网络中进行传输，因此当把对象通过网络发送出去之前需要先序列化成二进制数据，在接收端读到二进制数据之后反序列化成Java对象 

```java
FileOutputStream fos = new FileOutputStream("temp.out");
ObjectOutputStream oos = new ObjectOutputStream(fos);
TestObject testObject = new TestObject();
oos.writeObject(testObject);//进行序列化写入文件
oos.flush();
oos.close();
FileInputStream fis = new FileInputStream("temp.out");
ObjectInputStream ois = new ObjectInputStream(fis);
TestObject deTest = (TestObject) ois.readObject();//从二进制数据中反序列化
```



## 序列化总结

* static,transient后的变量不能被序列化，这是代码中的if逻辑
* 当父类继承Serializable接口时，所有子类都可以被序列化 
* 子类实现了Serializable接口，父类没有，父类中的属性不能被序列化（不报错，数据不会丢失），但是在子类中的属性仍能正确序列化
* 如果序列化的属性是对象，则这个对象也必须实现Serializable接口，否则会报错 
* 在反序列化时，如果对象的属性有修改或删减，则修改的部分属性会丢失，但不会报错 
* 在反序列化时，如果serialVersionUID被修改，则反序列化时会失败
* 当一个对象的实例变量引用其他对象，序列化该对象时，也把引用对象进行序列化
* 序列化是深度复制