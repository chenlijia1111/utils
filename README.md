# utils
工具类集合

主要涵盖了平时自己会用到的一些工具方法，为了可以重复使用，就找了个时间慢慢把之前写的以及平时会用到的工具整理起来。

主要模块如下：

cn:中文内容处理工具

code:代码生成工具

common:系统公用代码

core:核心工具

database:数据库工具,java导入导出sql,mybatis分页插件等

email:邮箱工具

encrypt:加密签名工具

http:网络请求工具

image:图片处理工具

list:集合工具

oauth:第三方登录封装

office:office文件工具

pay:第三方支付工具

timer:定时器工具

xml:xml工具

jar包已上传至maven中央仓库

仓库地址
稳定版地址
~~~xml
<dependency>
    <groupId>com.github.chenlijia1111</groupId>
    <artifactId>utils</artifactId>
    <version>1.1.9-RELEASE</version>
</dependency>
~~~

快照版地址
~~~xml
<dependency>
    <groupId>com.github.chenlijia1111</groupId>
    <artifactId>utils</artifactId>
    <version>1.1.9-SNAPSHOT</version>
</dependency>
~~~
如果要下载快照版jar报需要加上快照仓库
~~~xml
<!-- 配置阿里云中央仓库下载 国内加快下载速度 -->
<repositories>
    <repository>
        <id>maven-ali</id>
        <url>http://maven.aliyun.com/nexus/content/groups/public/</url>
    </repository>
    <!--下载快照版本-->
    <repository>
        <id>snapshots</id>
        <url>https://oss.sonatype.org/content/repositories/snapshots</url>
        <releases>
            <enabled>true</enabled>
        </releases>
        <snapshots>
            <enabled>true</enabled>
            <updatePolicy>always</updatePolicy>
        </snapshots>
    </repository>
</repositories>
~~~

