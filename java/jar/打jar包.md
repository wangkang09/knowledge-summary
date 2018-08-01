## 打jar包 ##
1. 到包的最上层目录中，从java/的下层COM开始打起
![](https://raw.githubusercontent.com/wangkang09/knowledge-summary/master/java/jar/%E6%89%93jar%E5%8C%851.png)
2. 添加MANIFEST.MF文件，Main-Class是入口类
![](https://raw.githubusercontent.com/wangkang09/knowledge-summary/master/java/jar/%E6%89%93jar%E5%8C%852.png)    

3. jar cvfm jarName.jar Manifest.mf -C java/ .
4. 用解压软件打开，jarName.jar,将MANIFEST.MF文件复制替换jar里面的

