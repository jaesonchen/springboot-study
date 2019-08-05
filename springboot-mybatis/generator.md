# mybatis 代码生成工具

## 引入依赖
```
<!-- mybatis 代码生成依赖 -->
<dependency>
    <groupId>org.mybatis.generator</groupId>
    <artifactId>mybatis-generator-core</artifactId>
    <version>1.3.7</version>
</dependency>


<pluginManagement>
    <plugins>
        <!-- mybatis 代码生成插件 -->
        <!-- mvn mybatis-generator:generate && mvn org.mybatis.generator:mybatis-generator-maven-plugin:generate -->
        <plugin>
            <groupId>org.mybatis.generator</groupId>
            <artifactId>mybatis-generator-maven-plugin</artifactId>
            <version>1.3.7</version>
            <configuration>
                <configurationFile>src/main/resources/generatorConfig.xml</configurationFile>
                <verbose>true</verbose>
                <overwrite>true</overwrite>
            </configuration>
            <executions>
                <execution>
                    <id>Generate MyBatis Artifacts</id>
                    <goals>
                        <goal>generate</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</pluginManagement>
```
    

## generatorConfig.xml
```
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE generatorConfiguration
    PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
    "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">

<generatorConfiguration>
    <!-- 引入配置文件，以使用${}占位符 -->
    <!-- <properties resource="jdbc.properties" /> -->

    <!--
        classPathEntry：类路径，可以配置多个
        location:里面的是路径(也可以直接写绝对路径) -->
    <classPathEntry location="D:\workspace\repository\mysql\mysql-connector-java\5.1.47\mysql-connector-java-5.1.47.jar" />
    <!--
        context：用于生成一组对象的环境(至少配置1个)
        id:表达唯一的名称
        targetRuntime：用于指定生成的代码的运行环境(MyBatis3/MyBatis3Simple)
            MyBatis3: 默认值
            MyBatis3Simple：不会生成与Example(案例)相关的方法 -->
    <context id="mysqlTables" targetRuntime="MyBatis3Simple">
        <!-- 生成的pojo，将implements Serializable-->    
        <plugin type="org.mybatis.generator.plugins.SerializablePlugin" />
    
        <!-- 用于配置如果生成注释信息
            suppressAllComments: 阻止生成注释 ，默认为false
            suppressDate: 阻止生成的注释时间戳，默认为false
            addRemarkComments：注释是否添加数据库表的备注信息，默认为false -->
        <commentGenerator>
            <property name="suppressDate" value="true" />
            <property name="suppressAllComments" value="true" />
        </commentGenerator>
        
        <!-- 配置连接数据库的基本信息 -->
        <jdbcConnection driverClass="com.mysql.jdbc.Driver" 
                        connectionURL="jdbc:mysql://192.168.0.102:3306/mybatis?useUnicode=true&amp;useSSL=false" 
                        userId="root" 
                        password="root" />
                        
        <!-- 指定JDBC类型和Java类型如何转换， 默认false
             false  把JDBC DECIMAL 和 NUMERIC 类型解析为 Integer    
             true   把JDBC DECIMAL 和 NUMERIC 类型解析为java.math.BigDecimal -->
        <javaTypeResolver>
            <property name="forceBigDecimals" value="false" />
        </javaTypeResolver>

        <!--
            javaModelGenerator: 控制生成的实体类
                targetPackage：生成Model类存放位置(包名)
                targetProject：指定目标项目路径(根目录)
                    trimStrings：从数据库返回的值被清理前后的空格(默认false) -->
        <javaModelGenerator targetPackage="com.asiainfo.mybatis.generator.model" 
                            targetProject="src/main/java">
            <property name="enableSubPackages" value="true" />
            <property name="trimStrings" value="true" />
        </javaModelGenerator>
        
        <!--对应的mapper.xml文件  -->
        <!--
            sqlMapGenerator：生成映射文件存放位置(Mapper.xml文件)
                targetPackage: 生成SQL映射文件(XML文件)在哪个包中
                targetProject: 指定目标项目路径(根目录) -->
        <sqlMapGenerator targetPackage="com.asiainfo.mybatis.generator.mapper" 
                         targetProject="src/main/java">
            <property name="enableSubPackages" value="true" />
        </sqlMapGenerator>

        <!-- 对应的Mapper接口类文件 -->
        <!--
            javaClientGenerator：Java客户端生成器(生成Dao/Mapper的接口)，该标签可选(最多配置一个)，如果不配置，就不会生成Mapper接口
                type:选择客户端代码生成器
                    MyBatis3
                        ANNOTATEDMAPPER：基于注解的Mapper接口，不会有对应的XML映射文件
                        MIXEDMAPPER: XML和注解混合形式
                        XMLMAPPER：所有方法都在XML中(接口调用依赖XML)
                    MyBatis3Simple
                        ANNOTATEDMAPPER：基于注解的Mapper接口，不会有对应的XML映射文件
                        XMLMAPPER：所有方法都在XML中(接口调用依赖XML)
                targetPackage：生成Mapper接口存放的包名
                targetProject：指定目标项目路径 -->
        <javaClientGenerator type="XMLMAPPER" targetPackage="com.asiainfo.mybatis.generator.dao" targetProject="src/main/java">
            <property name="enableSubPackages" value="true"/>
        </javaClientGenerator>
        
        <!--
            table：生成对应表及类名
                tableName：对应表名(注: %代表所有)
                domainObjectName: 对应的类名
                generatedKey：主键自增的id字段(针对当前数据库配置MySQL) 
                enableCountByExample="false" 
                enableUpdateByExample="false"
                enableDeleteByExample="false" 
                enableSelectByExample="false"
                selectByExampleQueryId="false" -->
        <table tableName="user" domainObjectName="User">
            <generatedKey column="user_id" sqlStatement="MySql" />
        </table>
        <table tableName="card" domainObjectName="Card" />
        <table tableName="course" domainObjectName="Course">
            <generatedKey column="course_id" sqlStatement="MySql" />
        </table>
        <!-- 注意：【上面是生成单个表的配置，下面是生成数据库所有表的配置】 -->
        <!-- 把所有的表都生成: domain是根据表名字来的, 驼峰式命名, 创建表的时候, 尽量不要写t_user，写user -->
        <!--
        <table tableName="%" >
            <generatedKey column="id" sqlStatement="MySql" />
        </table>-->
    </context>
</generatorConfiguration>
```
    

## 运行maven生成代码和xml
`mvn mybatis-generator:generate` 或者 `mvn org.mybatis.generator:mybatis-generator-maven-plugin:generate`



