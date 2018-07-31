## 3 Java Web 中的中文编码问题 ##
### 3.1 为什么要编码 ###
* 计算机存储信息的最小单元是1byte，即8个bit，所以只能表示的字符范围是0-225个
* 人类表示的符号太多，无法用1个byte完全表示
* 必须要有一个新的数据结构char，从char到byte必须编码
* char的字节数从1-8个不等，根据编码方式

### 3.2 几种常见的编码方式 ###
* ASCII码
	* 共用128个，用一个字节的低7位表示
* ISO-8859-1
	* 扩展ASCII码，仍然是单字节，有256个字符
* GB2312
	* 双字节编码
* GBK
	* 扩展GB2312加入了更多的汉子
* UTF-16
	* 定义了Unicode字符在计算机中的存取方法
	* 用两个字节表示Unicode的转化格式，为定长方法
	* 是Java 和 XML 的基础
	* 每两个字节表示一个字符，大大简化了字符串操作
* UTF-8
	* 变长编码

### 3.3 在I/O操作中存在的编码 ###
* Reader类读的是字符，InputStream 读的是字节
* 解码工作交给了StreamDecoder
* 中文环境默认GBK编码
* 不建议使用默认编码，跨环境是会出现乱码

### 3.4 在内存操作中的编码 ###  
* Java中用String表示字符串，所以String类中就提供了转换到字节的方法

---
    String s = "这是一段中文字符串";
    byte[] b = s.getBytes("UTF-8");
    String n = new String(b,"UTF-8");
* char[] 到 byte[]的编码
* ByteBuffer类提供，char到byte的软转换

### 3.5 在Java Web 中的编解码 ###
#### 3.5.1 URL的编解码 ####
* 每个浏览器对URL的编码都不太一样
* 以Tomcat为例：
* URL的URI部分进行解码的字符集是在connector的<Connector URIEncoding="UTF-8"/>中设置的，默认是 ISO-8859-1，所以在有中文URL时，最好把URIEncoding设置成UTF-8编码
* QueryString的解码，要么使用header中的ContentType定义的CharSet，要么为默认值ISO-8859-1
* 如果要使用contentType中的编码，则要<Connector URIEncoding="UTF-8" useBodyEncodingForURI="true"/>

#### 3.5.2 HTTP Header 的编解码 ####
* 默认是ISO-8859-1
* 最好不要在Header中传递非ASCII字符

#### 3.5.3 POST 表单的编解码 ####
* post表单的参数传递方式与QueryString 不同，是通过HTTP 的 BODY 传递到服务端的
* 浏览器根据 ContentType的CharSet对表单进行编码
* 服务器端也是根据ContentType的CharSet进行解码，所以一般不会出错

#### 常见问题 ####
* 中文变成了看不懂的字符
	* 在解码时，字符集与编码字符集不一致会导致，一个汉字字符变成两个乱码字符
* 一个汉字变成一个问号
	* 中文和正文字符经过不支持中文的ISO-8859-1编码后，遇到不在范围的值统一用3f表示

* 一个汉字变成两个问号
	* 