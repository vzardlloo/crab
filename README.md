# 🦀 Crab

Crab是一款采用Java开发的http服务器。

# 使用

```java
Crab crab = new Crab(10000);
crab.addHandler(new AssetHandler("D://work"));
crab.start();
```
将`test.html`放到`D://work`目录下<br>
打开浏览器，输入 [http://127.0.0.1:10000/test.html](http://127.0.0.1:10000/test.html)