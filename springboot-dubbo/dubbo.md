# dubbo 架构
![architecture](src/resources/images/dubbo-architecture.png)  
    
# dubbo 线程派发模型
![protocol](src/resources/images/dubbo-protocol.jpg)  
    
# dubbo io
![io](src/resources/images/dubbo-io.jpg)  
    
# dubbo 调用链
![invoke](src/resources/images/dubbo-invoke.png)  
    

# 快速启动
Dubbo 采用全 Spring 配置方式，透明化接入应用，对应用没有任何 API 侵入，只需用 Spring 加载 Dubbo 的配置即可，Dubbo 基于 Spring 的 Schema 扩展 进行加载。
    

## 用 Spring 配置声明暴露服务
provider.xml:
    
```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:dubbo="http://dubbo.apache.org/schema/dubbo"
    xsi:schemaLocation="http://www.springframework.org/schema/beans        http://www.springframework.org/schema/beans/spring-beans-4.3.xsd        http://dubbo.apache.org/schema/dubbo        http://dubbo.apache.org/schema/dubbo/dubbo.xsd">
 
    <!-- 提供方应用信息，用于计算依赖关系 -->
    <dubbo:application name="hello-world-app"  />
 
    <!-- 使用multicast广播注册中心暴露服务地址 -->
    <dubbo:registry address="multicast://224.5.6.7:1234" />
 
    <!-- 用dubbo协议在20880端口暴露服务 -->
    <dubbo:protocol name="dubbo" port="20880" />
 
    <!-- 声明需要暴露的服务接口 -->
    <dubbo:service interface="org.apache.dubbo.demo.DemoService" ref="demoService" />
 
    <!-- 和本地bean一样实现服务 -->
    <bean id="demoService" class="org.apache.dubbo.demo.provider.DemoServiceImpl" />
</beans>
```
    

## 通过 Spring 配置引用远程服务
consumer.xml:
    
```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:dubbo="http://dubbo.apache.org/schema/dubbo"
    xsi:schemaLocation="http://www.springframework.org/schema/beans        http://www.springframework.org/schema/beans/spring-beans-4.3.xsd        http://dubbo.apache.org/schema/dubbo        http://dubbo.apache.org/schema/dubbo/dubbo.xsd">
 
    <!-- 消费方应用名，用于计算依赖关系，不是匹配条件，不要与提供方一样 -->
    <dubbo:application name="consumer-of-helloworld-app"  />
 
    <!-- 使用multicast广播注册中心暴露发现服务地址 -->
    <dubbo:registry address="multicast://224.5.6.7:1234" />
 
    <!-- 生成远程服务代理，可以和本地bean一样使用demoService -->
    <dubbo:reference id="demoService" interface="org.apache.dubbo.demo.DemoService" />
</beans>
```
    

# XML 配置
所有标签都支持自定义参数，用于不同扩展点实现的特殊配置。
    
```
<dubbo:protocol name="jms">
    <dubbo:parameter key="queue" value="your_queue" />
</dubbo:protocol>
```
    
或者
    
```
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:dubbo="http://dubbo.apache.org/schema/dubbo"
    xmlns:p="http://www.springframework.org/schema/p"
    xsi:schemaLocation="http://www.springframework.org/schema/beans 
        http://www.springframework.org/schema/beans/spring-beans-4.3.xsd 
        http://dubbo.apache.org/schema/dubbo 
        http://dubbo.apache.org/schema/dubbo/dubbo.xsd">  
        
    <dubbo:protocol name="jms" p:queue="your_queue" />  
</beans>  
```
    

## 配置元素依赖关系
![config](src/resources/images/dubbo-config.jpg)  
    

## 标签
    
 标签 | 用途 | 解释
 ---- | ----- | ------  
 <dubbo:service/> | 服务配置 | 用于暴露一个服务，定义服务的元信息，一个服务可以用多个协议暴露，一个服务也可以注册到多个注册中心
 <dubbo:reference/> | 引用配置 | 用于创建一个远程服务代理，一个引用可以指向多个注册中心
 <dubbo:protocol/> | 协议配置 | 用于配置提供服务的协议信息，协议由提供方指定，消费方被动接受
 <dubbo:application/> | 应用配置 | 用于配置当前应用信息，不管该应用是提供者还是消费者
 <dubbo:module/> | 模块配置 | 用于配置当前模块信息，可选
 <dubbo:registry/> | 注册中心配置 | 用于配置连接注册中心相关信息
 <dubbo:monitor/> | 监控中心配置 | 用于配置连接监控中心相关信息，可选
 <dubbo:provider/> | 提供方配置 |  当 ProtocolConfig 和 ServiceConfig 某属性没有配置时，采用此缺省值，可选
 <dubbo:consumer/> | 消费方配置 |  当 ReferenceConfig 某属性没有配置时，采用此缺省值，可选
 <dubbo:method/> | 方法配置 | 用于 ServiceConfig 和 ReferenceConfig 指定方法级的配置信息
 <dubbo:argument/> | 参数配置 | 用于指定方法参数配置
    

## 配置的覆盖关系
dubbo的配置由服务提供方配置，通过 URL 经由注册中心传递给消费方。

不同粒度配置的覆盖关系：

- 方法级优先，接口级次之，全局配置再次之。
- 如果粒度一样，则消费方优先，提供方次之。
    
查找顺序：
    
![config override](src/resources/images/dubbo-config-override.jpg)  
    
建议由服务提供方设置超时，因为一个方法需要执行多长时间，服务提供方更清楚，如果一个消费方同时引用多个服务，就不需要关心每个服务的超时设置。
    

# 注解配置

## 引入springboot 自动配置依赖
```
        <!-- dubbo starter -->
        <dependency>
            <groupId>org.apache.dubbo</groupId>
            <artifactId>dubbo-spring-boot-starter</artifactId>
            <version>2.7.0</version>
        </dependency>
        <!-- retry -->
        <dependency>
            <groupId>org.apache.curator</groupId>
            <artifactId>curator-framework</artifactId>
            <version>4.0.1</version>
            <exclusions>
                <exclusion>
                    <groupId>io.netty</groupId>
                    <artifactId>*</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
        <dependency>
            <groupId>org.apache.curator</groupId>
            <artifactId>curator-recipes</artifactId>
            <version>4.0.1</version>
        </dependency>
```

## @Service
`@Service` 注解用于发布服务。当前版本没有method级别的注解，通常配置在parameters key/value 属性里，包括cluster、loadbalance、timeout、retries(默认2)、connections、actives、mock等。
    
`@Service` `filter = "legacy-block"` 激活服务级别过滤器。也可以通过`dubbo.provider.filter = legacy-block`激活所有服务的过滤器。
    
```
@Service(filter = "legacy-block", loadbalance = "roundrobin", cluster="failfast", timeout = 6000, retries = 0, version = "1.0")
// parameters = {"getById.timeout", "3000", "findAll.timeout", "5000"}
public class UserServiceImpl implements UserService {

    @Override
    public User getById(Long id) {
        return new User(id, "chenzq", "1234");
    }

    @Override
    public List<User> findAll() {
        return Arrays.asList(new User(1001L, "chenzq", "1234"), new User(1002L, "jaeson", "1234"), new User(1003L, "jaesonchen", "1234"));
    }

    @Override
    public boolean check() {
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            // ignore
        }
        return true;
    }
}
```
    

## @Reference
`@Reference` 注解用于生成远程服务代理。当前版本没有method级别的注解，通常配置在parameters key/value 属性里，包括cluster、loadbalance、timeout、retries(默认2)、connections、actives、mock等。
    
```
    @Reference(check = false, version = "*", parameters = { "check.timeout", "1000", 
            "check.mock", "com.asiainfo.springboot.dubbo.service.UserServiceMock" })
    private UserService userService;
    
    @Reference(group = "*", version = "1.0", parameters = { "getMenu.merger", "true" })
    private MenuService menuService;
```
    
`@Reference` parameters = { "getMenu.merger", "true" } 合并同一个接口的多个实现。
    
`@Reference` mock = "true" 或者 parameters = { "check.mock", "com.asiainfo.springboot.dubbo.service.UserServiceMock" }) 激活服务降级。
    

## 配置格式
目前Dubbo支持的所有配置都是.properties格式的，包括-D、Externalized Configuration等，.properties中的所有配置项遵循一种path-based的配置格式：
    
```
# 应用级别
dubbo.{config-type}[.{config-id}].{config-item}={config-item-value}
# 服务级别
dubbo.service.{interface-name}[.{method-name}].{config-item}={config-item-value}
dubbo.reference.{interface-name}[.{method-name}].{config-item}={config-item-value}
# 多配置项
dubbo.{config-type}s.{config-id}.{config-item}={config-item-value}
```
    
- 应用级别
    
````
dubbo.application.name=demo-provider
dubbo.registry.address=zookeeper://127.0.0.1:2181
dubbo.protocol.port=-1
````

- 服务级别
    
````
dubbo.service.org.apache.dubbo.samples.api.DemoService.timeout=5000
dubbo.reference.org.apache.dubbo.samples.api.DemoService.timeout=6000
dubbo.reference.org.apache.dubbo.samples.api.DemoService.sayHello.timeout=7000
```
    
- 多配置项
    
````
dubbo.registries.unit1.address=zookeeper://127.0.0.1:2181
dubbo.registries.unit2.address=zookeeper://127.0.0.1:2182

dubbo.protocols.dubbo.name=dubbo
dubbo.protocols.dubbo.port=20880
dubbo.protocols.hessian.name=hessian
dubbo.protocols.hessian.port=8089
```
    
- 扩展配置
    
````
dubbo.application.parameters.item1=value1
dubbo.application.parameters.item2=value2
dubbo.registry.parameters.item3=value3
dubbo.reference.org.apache.dubbo.samples.api.DemoService.parameters.item4=value4
```
    

## application-provider.properties
```
# Dubbo Application
# 指定当前服务/应用的名字（同样的服务名字相同，不要和别的服务同名）
dubbo.application.name = dubbo-provider
# @Service服务扫描路径
dubbo.scan.base-packages = com.asiainfo.springboot.dubbo.service

# Dubbo Protocol
dubbo.protocol.name = dubbo
dubbo.protocol.port = 20880

# Dubbo Registry
# 指定注册中心的位置
dubbo.registry.id = my-registry
dubbo.registry.address = zookeeper://192.168.0.103:2181

# filter
#dubbo.provider.filter = legacy-block
```
    

## application-consumer.properties
```
# 指定当前服务/应用的名字（同样的服务名字相同，不要和别的服务同名）
dubbo.application.name = dubbo-consumer
# 同一台电脑上同时跑provider/consumer，不配置启动时报qos绑定地址错误
dubbo.application.qosEnable = false

# 指定注册中心的位置
dubbo.registry.id = my-registry
dubbo.registry.address = zookeeper://192.168.0.103:2181
```
    

# 服务化最佳实践
1. 分包: 建议将服务接口、服务模型、服务异常等均放在 API 包中，因为服务模型和异常也是 API 的一部分，这样做也符合分包原则：重用发布等价原则(REP)，共同重用原则(CRP)。
    
如果需要，也可以考虑在 API 包中放置一份 Spring 的引用配置，这样使用方只需在 Spring 加载过程中引用此配置即可。配置建议放在模块的包目录下，以免冲突，如：`com/alibaba/china/xxx/dubbo-reference.xml`。
    
2. 粒度: 服务接口尽可能大粒度，每个服务方法应代表一个功能，而不是某功能的一个步骤，否则将面临分布式事务问题，Dubbo 暂未提供分布式事务支持。
    
服务接口建议以业务场景为单位划分，并对相近业务做抽象，防止接口数量爆炸。不建议使用过于抽象的通用接口，如：Map query(Map)，这样的接口没有明确语义，会给后期维护带来不便。
    
3. 版本: 每个接口都应定义版本号，为后续不兼容升级提供可能，如： `<dubbo:service interface="com.xxx.XxxService" version="1.0" />`。
    
建议使用两位版本号，因为第三位版本号通常表示兼容升级，只有不兼容时才需要变更服务版本。当不兼容时，先升级一半提供者为新版本，再将消费者全部升为新版本，然后将剩下的一半提供者升为新版本。
    
4. 兼容性: 服务接口增加方法，或服务模型增加字段，可向后兼容，删除方法或删除字段，将不兼容，枚举类型新增字段也不兼容，需通过变更版本号升级。
    
5. 枚举值: 如果是完备集，可以用 `Enum`，比如：`ENABLE, DISABLE`。
    
如果是业务种类，以后明显会有类型增加，不建议用 `Enum`，可以用` String` 代替。
    
如果是在返回值中用了 `Enum`，并新增了 `Enum` 值，建议先升级服务消费方，这样服务提供方不会返回新值。
    
如果是在传入参数中用了` Enum`，并新增了 `Enum` 值，建议先升级服务提供方，这样服务消费方不会传入新值。
    
6. 序列化: 服务参数及返回值建议使用 `POJO` 对象，即通过 `setter, getter` 方法表示属性的对象。
    
服务参数及返回值不建议使用接口，因为数据模型抽象的意义不大，并且序列化需要接口实现类的元信息，并不能起到隐藏实现的意图。
    
服务参数及返回值都必须是传值调用，而不能是传引用调用，消费方和提供方的参数或返回值引用并不是同一个，只是值相同，Dubbo 不支持引用远程对象。
    
7. 异常: 建议使用异常汇报错误，而不是返回错误码，异常信息能携带更多信息，并且语义更友好。
    
如果担心性能问题，在必要时，可以通过 `override` 掉异常类的 `fillInStackTrace()` 方法为空方法，使其不拷贝栈信息。
    
查询方法不建议抛出 `checked` 异常，否则调用方在查询时将过多的 `try...catch`，并且不能进行有效处理。
    
服务提供方不应将 DAO 或 SQL 等异常抛给消费方，应在服务实现中对消费方不关心的异常进行包装，否则可能出现消费方无法反序列化相应异常。
    
8. 调用: 不要只是因为是 Dubbo 调用，而把调用 `try...catch` 起来。`try...catch` 应该加上合适的回滚边界上。
    
`Provider` 端需要对输入参数进行校验。如有性能上的考虑，服务实现者可以考虑在 API 包上加上服务 Stub 类来完成检验。
    


# 推荐用法
1. 在 Provider 端配置合理的 Provider 端属性。
    
建议在 Provider 端配置的 Provider 端属性有：
- `threads`：服务线程池大小
- `executes`：一个服务提供者并行执行请求上限，即当 Provider 对一个服务的并发调用达到上限后，新调用会阻塞，此时 Consumer 可能会超时。在方法上配置 `dubbo:method` 则针对该方法进行并发限制，在接口上配置 `dubbo:service`，则针对该服务进行并发限制
    
```
<dubbo:protocol threads="200" /> 
<dubbo:service interface="com.alibaba.hello.api.HelloService" version="1.0.0" ref="helloService" executes="200" >
    <dubbo:method name="findAllPerson" executes="50" />
</dubbo:service>
```
    
2. 在 Provider 端尽量多配置 Consumer 端属性。
    
作服务的提供方，比服务消费方更清楚服务的性能参数，如调用的超时时间、合理的重试次数等。在 Provider 端配置后，Consumer 端不配置则会使用 Provider 端的配置，即 Provider 端的配置可以作为 Consumer 的缺省值 。否则，Consumer 会使用 Consumer 端的全局设置，这对于 Provider 是不可控的，并且往往是不合理的。
    
建议在 Provider 端配置的 Consumer 端属性有：
- `timeout`：方法调用的超时时间
- `retries`：失败重试次数，缺省是 2
- `loadbalance`：负载均衡算法，缺省是随机 random。还可以配置轮询 roundrobin、最不活跃优先 leastactive 和一致性哈希 consistenthash 等
- `actives`：消费者端的最大并发调用限制，即当 Consumer 对一个服务的并发调用到上限后，新调用会阻塞直到超时，在方法上配置 dubbo:method 则针对该方法进行并发限制，在接口上配置 dubbo:service，则针对该服务进行并发限制
    
```
<dubbo:service interface="com.alibaba.hello.api.HelloService" version="1.0.0" ref="helloService"
    timeout="300" retry="2" loadbalance="random" actives="0" />
<dubbo:service interface="com.alibaba.hello.api.WorldService" version="1.0.0" ref="helloService"
    timeout="300" retry="2" loadbalance="random" actives="0" >
    <dubbo:method name="findAllPerson" timeout="10000" retries="9" loadbalance="leastactive" actives="5" />
<dubbo:service/>
```
    
3. 配置管理信息。
    
目前有负责人信息和组织信息用于区分站点。以便于在发现问题时找到服务对应负责人，建议至少配置两个人以便备份。负责人和组织信息可以在运维平台 (Dubbo Ops) 上看到。
    
在应用层面配置负责人、组织信息：    
`<dubbo:application owner="ding.lid,william.liangf" organization="intl" />`
    
在服务层面（服务端）配置负责人：    
`<dubbo:service owner="ding.lid,william.liangf" />`
    
在服务层面（消费端）配置负责人：    
`<dubbo:reference owner="ding.lid,william.liangf" />`
    
4. 配置 Dubbo 缓存文件。
    
该文件会缓存注册中心列表和服务提供者列表。配置缓存文件后，应用重启过程中，若注册中心不可用，应用会从该缓存文件读取服务提供者列表，进一步保证应用可靠性。
        
注意：如果有多个应用进程，请注意不要使用同一个文件，避免内容被覆盖。提供者列表缓存文件：    
`<dubbo:registry file="${user.home}/output/dubbo.cache" />`
    
5. 不要使用 `dubbo.properties` 文件配置，推荐使用对应 XML 配置。
    
Dubbo 中所有的配置项都可以配置在 Spring 配置文件中，并且可以针对单个服务配置。如完全不配置则使用 Dubbo 缺省值。
    

# 为什么不能传大包
因 dubbo 协议采用单一长连接，如果每次请求的数据包大小为 500KByte，假设网络为千兆网卡，每条连接最大 7MByte，单个服务提供者的 TPS(每秒处理事务数)最大为：128MByte / 500KByte = 262。单个消费者调用单个服务提供者的 TPS(每秒处理事务数)最大为：7MByte / 500KByte = 14。如果能接受，可以考虑使用，否则网络将成为瓶颈。
    

# 包名改造
dubbo已经成为apache项目。
    
1. groupId 由 `com.alibaba` 改为 `org.apache.dubbo`
2. package: 为了减少用户升级成本，让用户可以做到渐进式升级，2.7.0版本继续保留了一些常用基础API和SPI `com.alibaba.dubbo`的支持。package 由` com.alibaba.dubbo` 改为 `org.apache.dubbo`。
