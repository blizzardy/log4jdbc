# log4jdbc
A more extensive README will be created soon. For now, you can view the usage instructions at the old Google Code hosting site:

https://code.google.com/p/log4jdbc/

You can download the prebuilt jars at:

https://code.google.com/p/log4jdbc/downloads/list


## 概要说明

###  特性

* 完全支持JDBC3和JDBC4
* 配置简单，一般情况下你只需要将你的`DriverClass`改为:`net.sf.log4jdbc.DriverSpy`，并在你的`jdbcUrl`之前拼接`jdbc:log4`
* 自动将占位符(?)替换为实际的参数
* 能够及时方便地显示SQL的实际执行时间
* 显示SQL Connection的数量的信息
* 能在JDK1.4+和SLF4J1.X上和大多数常见的JDBC驱动协同工作
* open source

### 官方MVN依赖

```
<dependency>
    <groupId>com.googlecode.log4jdbc</groupId>
    <artifactId>log4jdbc</artifactId>
    <version>1.2</version>
</dependency>
```

### 配置文件

在类路径下提供`log4jdbc.propertie`配置文件或在系统属性(`System.getProperties()`)中提供配置属性, 
常用属性如下:

property | default | description | since
---------|---------|------------|---------
log4jdbc.drivers | - | log4jdbc 加载的一个或多个驱动的全类名。如果有多个，每个之间用逗号分隔(不带空格) | 1.0
log4jdbc.auto.load.popular.drivers | true | 自动加载常用的jdbc driver，如果设置为false，则必须提供 log4jdbc.drivers 属性。 | 1.2beta2
log4jdbc.debug.stack.prefix | - | 用于定位日志堆栈 | 1.0
log4jdbc.sqltiming.warn.threshold |  | 毫秒值.执行时间超过该值的SQL语句将被记录为warn级别. | 1.1beta1
log4jdbc.sqltiming.error.threshold |  | 毫秒值.执行时间超过该值的SQL语句将被记录为error级别. | 1.1beta1
log4jdbc.dump.booleanastruefalse | false | 当该值为 false 时，boolean 值显示为 0 和 1 ，为 true 时 boolean 值显示为 true 和 false | 1.2alpha1
log4jdbc.dump.sql.maxlinelength | 90 | SQL 分行的最大值 | 1.2alpha1
log4jdbc.dump.fulldebugstacktrace | false | 设置为 true 将会输出大篇幅的 debug信息 | 1.2alpha1
log4jdbc.dump.sql.select | true | 是否输出 select 语句 | 1.2alpha1
log4jdbc.dump.sql.insert | true | 是否输出 insert 语句 | 1.2alpha1
log4jdbc.dump.sql.delete | true | 是否输出 delete 语句 | 1.2alpha1
log4jdbc.dump.sql.update | true | 是否输出 update 语句 | 1.2alpha1
log4jdbc.dump.sql.create | true | 是否输出 create 语句 | 1.2alpha1
log4jdbc.dump.sql.addsemicolon | false | 是否在 SQL 的行末添加一个分号 | 1.2alpha1
log4jdbc.statement.warn | false | 设为 true 时, 会有 SQL 前添加警告标识 | 1.2alpha2
log4jdbc.trim.sql | true | 是否对 SQL 做 trim | 1.2beta2
log4jdbc.trim.sql.extrablanklines | true | Set this to false to not trim extra blank lines in the logged SQL (by default, when more than one blank line in a row occurs, the contiguous lines are collapsed to just one blank line.) (Previous versions didn't trim extra blank lines at all.) | 1.2
log4jdbc.suppress.generated.keys.exception | false | Set to true to ignore any exception produced by the method, Statement.getGeneratedKeys() (Useful for using log4jdbc with Coldfusion.) | 1.2beta2


### 默认驱动

不属于默认驱动的类需要在属性`log4jdbc.drivers`中指定

Driver Class | Database Type
-------------|----------------
oracle.jdbc.driver.OracleDriver | Older Oracle Driver
oracle.jdbc.OracleDriver | Newer Oracle Driver
com.sybase.jdbc2.jdbc.SybDriver | Sybase
net.sourceforge.jtds.jdbc.Driver | jTDS SQL Server & Sybase driver
com.microsoft.jdbc.sqlserver.SQLServerDriver | Microsoft SQL Server 2000 driver
com.microsoft.sqlserver.jdbc.SQLServerDriver | Microsoft SQL Server 2005 driver
weblogic.jdbc.sqlserver.SQLServerDriver | Weblogic SQL Server driver
com.informix.jdbc.IfxDriver | Informix
org.apache.derby.jdbc.ClientDriver | Apache Derby client/server driver, aka the Java DB
org.apache.derby.jdbc.EmbeddedDriver | Apache Derby embedded driver, aka the Java DB
com.mysql.jdbc.Driver | MySQL
org.postgresql.Driver | PostgresSQL
org.hsqldb.jdbcDriver | HSQLDB pure Java database
org.h2.Driver | H2 pure Java database

### jdbc配置

* `jdbc.driver`替换为`net.sf.log4jdbc.DriverSpy`
* `jdbc.url`添加前缀`jdbc:log4`, 如:
  `jdbc:mysql://localhost:3306/mvn`改为**jdbc:log4**`jdbc:mysql://localhost:3306/mvn`
  
### 日志配置

log4jdbc使用5种logger:

logger | 描述 | since
-------|-----|--------
jdbc.sqlonly | 仅仅记录 SQL 语句，会将占位符替换为实际的参数 | 1.0
jdbc.sqltiming | 包含 SQL 语句实际的执行时间 | 1.0
jdbc.audit | 除了 ResultSet 之外的所有JDBC调用信息，篇幅较长 | 1.0
jdbc.resultset | 包含 ResultSet 的信息，输出篇幅较长 | 1.0
jdbc.connection | 输出了 Connection 的 open、close 等信息 | 1.2alpha1

另外`log4jdbc.debug`会输出log4jdbc的内部调试, 如加载驱动的时的信息，driver found 或 not found 等

