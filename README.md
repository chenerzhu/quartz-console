### SpringBoot实现Quartz动态调度-可视化页面操作
  
***通过前端可视化界面简单几步就可以动态设置Quartz定时任务***

------------



### 实现功能
- 	可视化Quartz定时任务
-   支持任务元数据库db存储
-   支持添加动态类任务（如动态添加实现Runnable接口class）
-   支持添加已存在java任务模块（有固定的处理逻辑）
-   支持定时执行linux命令或调用脚本

------------



### 如何使用   
     
- 配置文件： application.properties(可配置数据库等信息);quartz.properties(配置quartz相关信息)   
- 启动方式：springboot启动方式，idea直接运行或者maven打包jar包，```java -jar quartz-console-1.0.jar``` 直接运行
- 访问地址： http://localhost:8081/index
- 操作：[Quartz可视化操作](http://htmlpreview.github.io/?https://github.com/chenerzhu/quartz-console/blob/master/src/main/resources/example/index.html "Quartz可视化操作")
