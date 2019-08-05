# 配置文件详解
Spring Boot的默认配置文件位置为： `src/main/resources/application.properties`，Spring Boot的配置文件除了可以使用传统的properties文件之外，还支持现在被广泛推荐使用的YAML文件。
    
注意：YAML目前还有一些不足，它无法通过@PropertySource注解来加载配置。但是，YAML加载属性到内存中保存的时候是有序的，所以当配置文件中的信息需要具备顺序含义时，YAML的配置方式比起properties配置文件更有优势。
    

## 常用注解
- `@Configuration` 标注在类上，声明该类是bean配置类，替代Spring 配置文件
- `@EnableConfigurationProperties` 激活某个@ConfigurationProperties标注的bean，通常会用cglib生成代理使其程为@Configuration配置类
- `@ConfigurationProperties` 加载全局配置文件application/application-{profile}，批量引入属性，通常指定prefix
- `@PropertySource` 加载指定路径的properties/yml 配置文件，非application-{profile}格式的配置文件，引入后可以通过@Autowired Environment env;读取属性值
- `@ImportResource` 加载xml格式Spring 配置文件
- `@Import` 通常是引入 @Configuration 注解的类 (java config)到容器里，也可以是普通的类
- `@Value` 自动注入属性，可以指定缺省值
    


## 自定义属性
在使用Spring Boot的时候，通常也需要定义一些自己使用的属性，然后通过`@Value("${属性名}")`注解来加载对应的配置属性。
    
```
com.didispace.blog.name = 程序猿DD

@Value("${com.didispace.blog.name}")
private String name;
```
    
`@Value`注解加载属性值的时候可以支持两种表达式来进行配置：
- 一种是我们上面介绍的PlaceHolder方式，格式为 `${完整属性名}`，大括号内为PlaceHolder
- 另外还可以使用SpEL表达式（`Spring Expression Language`）， 格式为 `#{...}`，大括号内为SpEL表达式
    

## 参数引用
在application.properties中的各个参数之间，我们也可以直接通过使用PlaceHolder的方式来进行引用。
    
```
com.didispace.blog.name = 程序猿DD
com.didispace.blog.title = Spring Boot教程
com.didispace.blog.desc = ${com.didispace.blog.name}正在努力写《${com.didispace.blog.title}》
```
    
desc参数引用了上文中定义的name和title属性，最后desc属性的值就是`程序猿DD正在努力写Spring Boot教程`。
    

## 使用随机数
在一些特殊情况下，有些参数我们希望它每次加载的时候不是一个固定的值，比如：密钥、服务端口等。在Spring Boot的属性配置文件中，我们可以通过使用`${random}`配置来产生随机的int值、long值或者string字符串，这样我们就可以容易的通过配置来属性的随机生成，而不是在程序中通过编码来实现这些逻辑。
    
${random}的配置方式主要有一下几种：
    
```
# 随机字符串
com.didispace.blog.value = ${random.value}
# 随机int
com.didispace.blog.number = ${random.int}
# 随机long
com.didispace.blog.bignumber = ${random.long}
# 10以内的随机数
com.didispace.blog.test1 = ${random.int(10)}
# 10-20的随机数
com.didispace.blog.test2 = ${random.int[10,20]}
```
    

## 命令行参数
`java -jar`命令除了启动应用之外，还可以在命令行中来指定应用的参数，比如：`java -jar xxx.jar --server.port=8888`，直接以命令行的方式，来设置`server.port`属性。
    
在命令行方式启动Spring Boot应用时，连续的两个减号`--`就是对application.properties中的属性值进行赋值的标识。所以，`java -jar xxx.jar --server.port=8888`命令，等价于我们在application.properties中添加属性`server.port=8888`。
    
通过命令行来修改属性值是Spring Boot非常重要的一个特性，通过此特性，理论上已经使得我们应用的属性在启动前是可变的，所以其中端口号也好、数据库连接也好，都是可以在应用启动时发生改变，而不同于以往的Spring应用通过Maven的Profile在编译器进行不同环境的构建。其最大的区别就是，Spring Boot的这种方式，可以让应用程序的打包内容，贯穿开发、测试以及线上部署，而Maven不同Profile的方案每个环境所构建的包，其内容本质上是不同的。
    
通过命令行来修改属性值固然提供了不错的便利性，但是通过命令行就能更改应用运行的参数，那岂不是很不安全？是的，所以Spring Boot也贴心的提供了屏蔽命令行访问属性的设置，只需要这句设置就能屏蔽：`SpringApplication.setAddCommandLineProperties(false)`。
    


## 多环境配置
我们在开发任何应用的时候，通常同一套程序会被应用和安装到几个不同的环境，比如：开发、测试、生产等。其中每个环境的数据库地址、服务器端口等等配置都会不同，如果在为不同环境打包时都要频繁修改配置文件的话，那必将是个非常繁琐且容易发生错误的事。
    
对于多环境的配置，各种项目构建工具或是框架的基本思路是一致的，通过配置多份不同环境的配置文件，再通过打包命令指定需要打包的内容之后进行区分打包，Spring Boot也不例外，或者说更加简单。
    
在Spring Boot中多环境配置文件名需要满足`application-{profile}.properties`的格式，其中`{profile}`对应你的环境标识，比如：
    
```
application-dev.properties：开发环境
application-test.properties：测试环境
application-prod.properties：生产环境
```
    
至于哪个具体的配置文件会被加载，需要在`application.properties`文件中通过`spring.profiles.active`属性来设置或者通过`java -jar xxx.jar --spring.profiles.active=test`指定，其值对应配置文件中的`{profile}`值。如：`spring.profiles.active=test`就会加载`application-test.properties`配置文件内容。
    

## 属性加载顺序
Spring Boot为了能够更合理的重写各属性的值，使用了下面这种较为特别的属性加载顺序：
1. 命令行中传入的参数。
2. SPRING_APPLICATION_JSON中的属性。SPRING_APPLICATION_JSON是以JSON格式配置在系统环境变量中的内容。
3. Java的系统属性，可以通过System.getProperties()获得的内容。
4. 操作系统的环境变量。
5. 通过random.*配置的随机属性。
6. 位于当前应用jar包之外，针对不同{profile}环境的配置文件内容，例如：application-{profile}.properties或是YAML定义的配置文件。
7. 位于当前应用jar包之内，针对不同{profile}环境的配置文件内容，例如：application-{profile}.properties或是YAML定义的配置文件。
8. 位于当前应用jar包之外的application.properties和YAML配置内容。
9. 位于当前应用jar包之内的application.properties和YAML配置内容。
    
优先级按上面的顺序有高到低，可以看到，其中第6项和第8项都是从应用jar包之外读取配置文件。所以，实现外部化配置的原理就是从此切入，为其指定外部配置文件的加载位置来取代jar包之内的配置内容。通过这样的实现，我们的工程在配置中就变的非常干净，我们只需要在本地放置开发需要的配置即可，而其他环境的配置就可以不用关心，由其对应环境的负责人去维护即可。
    


