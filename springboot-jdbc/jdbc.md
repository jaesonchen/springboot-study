# 数据源配置

## 引入spring-boot-starter-jdbc
为了连接数据库需要引入jdbc支持，在pom.xml中引入如下配置：
    
```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jdbc</artifactId>
</dependency>
```
    

## 嵌入式数据库支持
嵌入式数据库通常用于开发和测试环境，不推荐用于生产环境。Spring Boot提供自动配置的嵌入式数据库有H2、HSQL、Derby，你不需要提供任何连接配置就能使用。
    
```
<dependency>
    <groupId>org.hsqldb</groupId>
    <artifactId>hsqldb</artifactId>
    <scope>runtime</scope>
</dependency>
```
    

## 连接生产数据源
以MySQL数据库为例，先引入MySQL连接的依赖包，在pom.xml中加入：
    
```
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>5.1.21</version>
</dependency>
```
    

## 数据源配置
在src/main/resources/application.properties中配置数据源信息：
    
```
spring.datasource.url=jdbc:mysql://localhost:3306/test
spring.datasource.username=root
spring.datasource.password=root
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
```
    

## JNDI数据源
当你将应用部署于应用服务器上的时候想让数据源由应用服务器管理，那么可以使用如下配置方式引入JNDI数据源。
    
```
spring.datasource.jndi-name=java:jboss/datasources/customers
```
    

## JdbcTemplate
Spring的JdbcTemplate是自动配置的，你可以直接使用`@Autowired`来注入到你自己的bean中来使用。
    
```
@Repository
public class UserDaoImpl implements UserDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    ...
}
```
   

# 多数据源配置
springboot2.0中的配置多数据源的url属性名为：`jdbc-url`。
    
创建一个Spring配置类，定义两个DataSource用来读取`application.properties`中的不同配置。主数据源配置为`spring.datasource.primary`开头的配置，第二数据源配置为`spring.datasource.secondary`开头的配置。
    
```
# primary datasource
spring.datasource.primary.driver-class-name=com.mysql.jdbc.Driver
spring.datasource.primary.jdbc-url=jdbc:mysql://192.168.0.103:3306/primary?useUnicode=true&characterEncoding=UTF-8&useSSL=false
spring.datasource.primary.username=root
spring.datasource.primary.password=root

# secondary datasource
spring.datasource.secondary.driver-class-name=com.mysql.jdbc.Driver
spring.datasource.secondary.jdbc-url=jdbc:mysql://192.168.0.103:3306/secondary?useUnicode=true&characterEncoding=UTF-8&useSSL=false
spring.datasource.secondary.username=root
spring.datasource.secondary.password=root
```
    
```
@Configuration
public class DataSourceConfig {

    @Primary
    @Bean(name = "primaryDataSource")
    @Qualifier("primaryDataSource")
    @ConfigurationProperties(prefix="spring.datasource.primary")
    public DataSource primaryDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "secondaryDataSource")
    @Qualifier("secondaryDataSource")
    @ConfigurationProperties(prefix="spring.datasource.secondary")
    public DataSource secondaryDataSource() {
        return DataSourceBuilder.create().build();
    }
}
```
    

## AbstractRoutingDataSource 读写分离
基于特定的查找key路由到特定的数据源。它内部维护了一组目标数据源，并且做了路由key与目标数据源之间的映射，提供基于key查找数据源的方法。

1. 继承`AbstractRoutingDataSource`实现数据源key查找逻辑。
    
```
public class MyRoutingDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceHolder.getDataSouce();
    }
}
```
    
2. 配置路由数据源
    
```
    @Bean(name = "routeDataSource")
    @Qualifier("routeDataSource")
    public DataSource myRoutingDataSource(@Qualifier("dataSource") DataSource masterDataSource,
                                          @Qualifier("secondaryDataSource") DataSource slaveDataSource) {
        
        Map<Object, Object> dataSources = new HashMap<>();
        dataSources.put(DataSourceType.MASTER, masterDataSource);
        dataSources.put(DataSourceType.SLAVE, slaveDataSource);
        MyRoutingDataSource myRoutingDataSource = new MyRoutingDataSource();
        myRoutingDataSource.setDefaultTargetDataSource(masterDataSource);
        myRoutingDataSource.setTargetDataSources(dataSources);
        return myRoutingDataSource;
    }
    
    @Primary
    @Bean(name = "jdbcTemplate")
    public JdbcTemplate jdbcTemplate(@Qualifier("routeDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
```
    
3. 定义切面逻辑和Master注解
切面逻辑主要用于在方法入口处指定主从数据源。`@Master`注解用于特定的查询方法，当需要查询刚写入的内容时，可以在方法上标注`@Master`达到查询主库的目的。
    
```
@Component
@Aspect
public class DataSourceAspect {

    final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Pointcut("!@annotation(com.asiainfo.springboot.jdbc.route.Master) " 
            + "&& (execution(* com.asiainfo.springboot.jdbc.service..*.select*(..)) " 
            + "|| execution(* com.asiainfo.springboot.jdbc.service..*.get*(..)) "
            + "|| execution(* com.asiainfo.springboot.jdbc.service..*.find*(..)))")
    public void readPointcut() {
    }

    @Pointcut("@annotation(com.asiainfo.springboot.jdbc.route.Master) " +
            "|| execution(* com.asiainfo.springboot.jdbc.service..*.insert*(..)) " +
            "|| execution(* com.asiainfo.springboot.jdbc.service..*.add*(..)) " +
            "|| execution(* com.asiainfo.springboot.jdbc.service..*.save*(..)) " +
            "|| execution(* com.asiainfo.springboot.jdbc.service..*.update*(..)) " +
            "|| execution(* com.asiainfo.springboot.jdbc.service..*.delete*(..)) " +
            "|| execution(* com.asiainfo.springboot.jdbc.service..*.remove*(..))")
    public void writePointcut() {
    }
    
    @Before("readPointcut()")
    public void beforeRead(JoinPoint point) {
        logger.info("before read {}", point);
        DataSourceHolder.setDataSource(DataSourceType.SLAVE);
    }
    
    @Before("writePointcut()")
    public void beforeWrite(JoinPoint point) {
        logger.info("before write {}", point);
        DataSourceHolder.setDataSource(DataSourceType.MASTER);
    }

    @AfterReturning("readPointcut() || writePointcut()")
    public void afterReturn(JoinPoint joinPoint) {
        logger.info("afterReturn {}", joinPoint);
        DataSourceHolder.clear();
    }
}


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Master {

}
```

