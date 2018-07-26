# markdown练习 #
---
**测试斜线**　　**<font color=green>`ctrl+i`</font>**  `* *`  
*我是斜线*  

**测试粗体**　　**<font color=green>`ctrl+b`</font>**  `** **`  
**我是黑桃**

**测试引用**　　**<font color=green>`ctrl+q`</font>** `>` 
> 我是引用  

**测试链接**　　**<font color=green>`ctrl+ｌ`</font>** `[内容](链接地址 "描述")`  
[我是百度链接](http://www.baidu.com "百度链接")  

**测试索引超链接**  
我现在写的文章参考了[Markdown 语法备忘录][1]、[markdown语法：常用备查][2]、[CSDN-markdown编辑器语法——字体、字号与颜色][3] 

**自动链接**  
<http://www.baidu.com>

**注脚**  
点击我有注脚哦。[^1]  
[^1]:这里是注脚  
我是随机文本。

---
**测试图片**　　**<font color=green>`ctrl+ｇ`</font>** `！[图片名字](链接地址 "描述")`     
---
![github 照片](https://raw.githubusercontent.com/wangkang09/shein-note/master/pic/github.jpg "github 照片")

<img src="https://raw.githubusercontent.com/wangkang09/shein-note/master/pic/github.jpg"  alt="上海鲜花港 - 郁金香" width="100" height="100" />  

<div align=center>
	<img src="https://raw.githubusercontent.com/wangkang09/shein-note/master/pic/github.jpg"  alt="上海鲜花港 - 郁金香" width="100" height="100" />
</div>
	
<div align=right>
	<img src="https://raw.githubusercontent.com/wangkang09/shein-note/master/pic/github.jpg"  alt="上海鲜花港 - 郁金香" width="100" height="100" />
</div>

### <font color=red> 任何对象都能弄成链接模式</font> ###
#### 图片和文本在一个块中的时候，可以设置图片的 `align`属性 ####
* `top` : 文本在图片中间
* `middle`：文本在上面
* `bottom`：默认，文本在下面
<div background="https://raw.githubusercontent.com/wangkang09/shein-note/master/pic/markdown-resource/background/1.jpg">
	<a href="http://www.baidu.com">
		<img src="https://raw.githubusercontent.com/wangkang09/shein-note/master/pic/github.jpg"  alt="上海鲜花港 - 郁金香" width="100" height="100" align="middle"/>
	</a>
	<a href="https://www.baidu.com">
　　		<font size = 123 color=blue > who am i!</font>
	</a>
</div>

##测试嵌套列表<font color=red size=3>:回车之后，按`tab`键，即可嵌套</font>##
* 嵌套1
* 嵌套2
	* 嵌套2.0
	* 嵌套2.1
		* 嵌套2.1.0
		* 嵌套2.1.1

---
1. 嵌套1
2. 嵌套2  
	- 嵌套2.0
	- 嵌套2.1
		- 嵌套2.1.0
		- 嵌套2.1.1


## 测试代码块<font color=red size=3>:只要在代码前加一个或多个`tab`即可</font> ##

	public class test {
		public static void main(String[] args){
			System.out.println("这就是代码块的风格！")
		}
	}

<!--注释-->
## 测试转义<font color=red size=3>:在特殊字符前加\(反引号)</font> ##
<i>有效果</i>  
<i\> 没效果</i\>  
[wang\](http://www.baidu.com\)  
[wang](http://www.baidu.com)  

##测试表格##
<table bgcolor=blue >
	<tbody>
		<tr >
			<th bgcolor=#7FFFD4 width=200>测试类型</th><th>快捷键</th><th>测试效果
		<tr>
		<tr>
			<td>测试斜线</td>
			<td>ctrl+i</td>
			<td><i>我是斜线</i></td>
		</tr>
		<tr>
			<td height=70>测试粗体</td>
			<td>ctrl+b</td>
			<td><b>我是斜线</b></td>
		</tr>
		<tr>
			<td >测试删除线</td>
			<td></td>
			<td><del>删除线</del></td>
		</tr>
		<tr>
			<td>测试代码</td>
			<td>ctrl+k</td>
			<td><font color=red><code>我是代码</code></font></td>
		</tr>
		<tr>
			<td>测试下标</td>
			<td></td>
			<td>我是氧气O<sub>2</sbu></td>
		</tr>
		<tr>
			<td>测试上标</td>
			<td></td>
			<td>我是平方O<sup>2</sup></td>
		</tr>
		<tr>
			<td>测试引用</td>
			<td>ctrl+q</td>
			<td></td>
		</tr>
		<tr>
			<td>测试链接</td>
			<td>ctrl+l</td>
			<td></td>
		</tr>
		<tr>
			<td>测试图片</td>
			<td>ctrl+g</td>
			<td></td>
		</tr>
	</tbody>
</table>
---

[1]:https://blog.csdn.net/qcx321/article/details/53780672  
[2]:https://blog.sarakale.com/tc/27.html  
[3]:https://blog.csdn.net/testcs_dn/article/details/45719357  

