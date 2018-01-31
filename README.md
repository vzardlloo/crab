# 🦀 Crab

Crab是一款简单的、轻量级的、采用Java开发的http服务器,主要用到了NIO与多线程技术

# 特点
+ 简单小巧，便于帮助理解http服务器的基本工作原理
+ 配置使用简单
+ 依赖很少,仅依赖了`lombok`来简化代码书写，和`dom4j`来操作配置文件

注：**在运行源码时要安装`lombok`插件,关于[lombok](https://projectlombok.org/)**

# 进展
* [x] 基本功能完成
* [ ] 开发更多功能
# 使用
首先添加依赖：
```xml
<dependency>
    <groupId>io.github.vzardlloo</groupId>
    <artifactId>crab</artifactId>
    <version>1.0</version>
</dependency>
```
or
```
compile 'io.github.vzardlloo:crab:1.0'
```
Crab的使用非常简单：

首先在项目的`resource`目录下创建`crab.xml`,内容为
```
<crabConfig>
    <item key="port" value="端口号"/>
    <item key="workspace" value="工作空间路径"/>
</crabConfig>
```
然后开始使用crab,代码非常简单直观：
```
public static void main(String[] args) throws IOException {
        new Crab().start();
    }
```
即可启用crab。
以上是默认的使用方式，你还可以对crab进行一些自定义，比如想自定义配置文件的存放位置,你可以这样

````
public static void main(String[] args) throws IOException {
        new Crab().setConfigFilePath("xxxxx").start();
    }
````
或者干脆不想使用配置文件也是可以的
```
 public static void main(String[] args) throws IOException {
        new Crab(端口号)
                .addHandler(new AssetHandler("工作空间路径"))
                .start();
    }

```
以上的端口号需要确保没有被占用，至于工作空间可以任意指定，不需要保证你的计算机上已经存在该路径，因为Crab会自动根据配置创建工作空间，并进行一些简单的初始化操作。

一切工作完成之后,Crab在本地启动后，在浏览器地址栏输入：`localhost：端口号`，会看到Crab的欢迎页面：
![](http://oo3aq3ac8.bkt.clouddn.com/crab.png)
这时你就可以把你的`html`页面等放入你的Crab工作空间里，然后通过浏览器便可访问到它们。

