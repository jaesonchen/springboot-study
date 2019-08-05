# dubbo
- 传输层 Transporter: mina, netty
- 序列化 Serialization: dubbo, hessian2, jdk, json
- 线程池 ThreadPool: fixed, cached, limit, eager
    

## 配置覆盖原则
细粒度配置优先于粗粒度配置，consumer优先于provider。
    
##启动时检查(check -> [consumer, reference])
Dubbo cosumer缺省会在启动时检查依赖的服务是否可用，不可用时会抛出异常，阻止 Spring 初始化完成，以便上线时，能及早发现问题，默认 `check="true"`。可以通过 `check="false"` 关闭检查，比如，测试时，有些服务不关心，或者出现了循环依赖，必须有一方先启动。
    
另外，如果你的 Spring 容器是懒加载的，或者通过 API 编程延迟引用服务，请关闭 check，否则服务临时不可用时，会抛出异常。
    
关闭所有服务的启动时检查 (没有提供者时报错)：    
`<dubbo:consumer check="false" />`

关闭单个服务的启动时检查 (没有提供者时报错)：    
`<dubbo:reference interface="com.foo.BarService" check="false" />`
    
`dubbo.properties`配置：
    
```
dubbo.reference.com.foo.BarService.check=false
dubbo.reference.check=false //强制改变所有 reference 的 check 值，就算配置中有声明，也会被覆盖。
dubbo.consumer.check=false  //设置 check 的缺省值，如果配置中有显式的声明，如：<dubbo:reference check="true"/>，不会受影响。
```
    

## 集群容错(cluster -> [service, reference], retries -> [service, method, reference, refer-menthod])
在集群调用失败时，Dubbo 提供了多种容错方案，缺省为 `failover`失败重试，通常配合`retries`使用。

- `Failover`: 失败自动切换重试，当出现失败，重试其它服务器。通常用于读操作，但重试会带来更长延迟。可通过 `retries="2"` 来设置重试次数(不含第一次)。
    
```
<dubbo:service retries="2" />
<dubbo:reference retries="2" />
<dubbo:reference>
    <dubbo:method name="findFoo" retries="2" />
</dubbo:reference>
```
    
- `Failfast`: 快速失败，只发起一次调用，失败立即报错。通常用于非幂等性的写操作，比如新增记录。

- `Failsafe`：失败安全，出现异常时，直接忽略。通常用于写入审计日志等操作。

- `Failback`: 失败自动恢复，后台记录失败请求，定时重发。通常用于消息通知操作。

- `Forking`: 并行调用多个服务器，只要一个成功即返回。通常用于实时性要求较高的读操作，但需要浪费更多服务资源。可通过 `forks="2"` 来设置最大并行数。

- `Broadcast`: 广播调用所有提供者，逐个调用，任意一台报错则报错 。通常用于通知所有提供者更新缓存或日志等本地资源信息。
    
```
<dubbo:service cluster="failsafe" />
<dubbo:reference cluster="failsafe" />
```
    

## 负载均衡(loadbalance -> [service, method, reference, refer-menthod], parameter -> [hash.arguments, hash.nodes])
- `Random LoadBalance`: 随机，按权重设置随机概率（默认）。在一个截面上碰撞的概率高，但调用量越大分布越均匀，而且按概率使用权重后也比较均匀，有利于动态调整提供者权重。

- `RoundRobin LoadBalance`: 轮询，按公约后的权重设置轮询比率。存在慢的提供者累积请求的问题。

- `LeastActive LoadBalance`: 最少活跃调用数，相同活跃数的随机，活跃数指调用前后计数差。使慢的提供者收到更少请求，因为越慢的提供者的调用前后计数差会越大。

- `ConsistentHash LoadBalance`: 一致性 Hash，相同参数的请求总是发到同一提供者。当某一台提供者挂时，原本发往该提供者的请求，基于虚拟节点，平摊到其它提供者，不会引起剧烈变动。缺省只对方法第一个参数 Hash，如果要修改，请配置 `<dubbo:parameter key="hash.arguments" value="0,1" />`。缺省用 160 份虚拟节点，如果要修改，请配置 `<dubbo:parameter key="hash.nodes" value="320" />`。

服务端：
    
```
<dubbo:service interface="..." loadbalance="roundrobin" />
<dubbo:service interface="...">
    <dubbo:method name="..." loadbalance="roundrobin"/>
</dubbo:service>
````
    
客户端：
    
```
<dubbo:reference interface="..." loadbalance="roundrobin" />
<dubbo:reference interface="...">
    <dubbo:method name="..." loadbalance="roundrobin"/>
</dubbo:reference>
```
    

## 线程模型(dispatcher, threadpool, threads -> [protocol])
dubbo线程模型类似于netty里的boss线程负责监听所有事件，没有work线程池的独立selector用于监听socket读事件，这里所谓的线程池就是用于处理一个socket里读写事件的Runnable Task的普通线程池。如果事件处理的逻辑能迅速完成，并且不会发起新的 IO 请求，比如只是在内存中记个标识，则直接在 IO 线程上处理更快，因为减少了线程池调度。但如果事件处理逻辑较慢，或者需要发起新的 IO 请求，比如需要查询数据库，则必须派发到线程池，否则 IO 线程阻塞，将导致不能接收其它请求。需要通过不同的派发策略和不同的线程池配置的组合来应对不同的场景：`<dubbo:protocol name="dubbo" dispatcher="all" threadpool="fixed" threads="100" />`
    
`dispatcher`：
    
- `all` 所有消息都派发到线程池，包括请求，响应，连接事件，断开事件，心跳等。
- `direct` 所有消息都不派发到线程池，全部在 IO 线程上直接执行。
- `message` 只有请求响应消息派发到线程池，其它连接、断开事件，心跳等消息，直接在 IO 线程上执行。

`threadpool`：
    
- `fixed` 固定大小线程池，启动时建立线程，不关闭，一直持有。(缺省)
- `cached` 缓存线程池，空闲一分钟自动删除，需要时重建。
- `limited` 可伸缩线程池，但池中的线程数只会增长不会收缩。只增长不收缩的目的是为了避免收缩时突然来了大流量引起的性能问题。
- `eager` 优先创建Worker线程池。在任务数量大于`corePoolSize`但是小于`maximumPoolSize`时，优先创建`Worker`来处理任务。 当任务数量大于`maximumPoolSize`时，将任务放入阻塞队列中。阻塞队列充满时抛出`RejectedExecutionException`。(相比于cached，cached在任务数量超过`maximumPoolSize`时直接抛出异常而不是将任务放入阻塞队列)
    
`threads` 线程池大小
      
    
## 协议(name -> [protocol, service, reference])
Dubbo 允许配置多协议，在不同服务上支持不同协议或者同一服务上同时支持多种协议。不同服务在性能上适用不同协议进行传输，比如大数据用短连接协议，小数据大并发用长连接协议。
    
```
<!-- 多协议配置 -->
<dubbo:protocol name="dubbo" port="20880" />
<dubbo:protocol name="hessian" port="8080" />
<!-- 使用dubbo协议暴露服务 -->
<dubbo:service interface="com.alibaba.hello.api.HelloService" version="1.0.0" ref="helloService" protocol="dubbo" />
<!-- 使用hessian协议暴露服务 -->
<dubbo:service interface="com.alibaba.hello.api.DemoService" version="1.0.0" ref="demoService" protocol="hessian" /> 
<!-- 使用多个协议暴露服务，用逗号分隔 -->
<dubbo:service interface="com.alibaba.hello.api.PublicService" version="1.0.0" protocol="dubbo,hessian" />
```
    

## 注册中心(id, address, default -> [registry], registry -> [service, reference])
Dubbo 支持同一服务向多注册中心同时注册，或者不同服务分别注册到不同的注册中心上去。
    
```
<!-- 多注册中心配置，竖号分隔表示同时连接多个不同注册中心，同一注册中心的多个集群地址用逗号分隔 -->
<dubbo:registry id="chinaRegistry" address="10.20.141.150:9090" />
<!-- default 默认true -->
<dubbo:registry id="intlRegistry" address="10.20.141.151:9010" default="false" />
<!-- 向多个注册中心注册 -->
<dubbo:service interface="com.alibaba.hello.api.HelloService" version="1.0.0" ref="helloService" registry="hangzhouRegistry,qingdaoRegistry" />
<!-- 不同服务使用不同注册中心 -->
<dubbo:service interface="com.alibaba.hello.api.HelloService" version="1.0.0" ref="helloService" registry="chinaRegistry" />
<dubbo:service interface="com.alibaba.hello.api.DemoService" version="1.0.0" ref="demoService" registry="intlRegistry" />
<!-- 多注册中心引用 -->
<dubbo:reference id="chinaHelloService" interface="com.alibaba.hello.api.HelloService" version="1.0.0" registry="chinaRegistry" />
<dubbo:reference id="intlHelloService" interface="com.alibaba.hello.api.HelloService" version="1.0.0" registry="intlRegistry" />
```
    

## 服务分组(group -> [service, reference])
当一个接口有多种实现时，可以用 group 区分。
    
```
<dubbo:service group="feedback" interface="com.xxx.IndexService" />
<dubbo:service group="member" interface="com.xxx.IndexService" />
<dubbo:reference id="feedbackIndexService" group="feedback" interface="com.xxx.IndexService" />
<dubbo:reference id="memberIndexService" group="member" interface="com.xxx.IndexService" />
<!-- 服务合并，合并多个实现的结果集 -->
<dubbo:reference id="indexService" interface="interface="com.xxx.IndexService" group="*" merger="true" />
<!-- 任意组，总是只调一个可用组的实现 -->
<dubbo:reference id="barService" interface="com.foo.BarService" group="*" />
```
    

## 版本(version -> [service, reference])
当一个接口实现，出现不兼容升级时，可以用版本号过渡，版本号不同的服务相互间不引用。
    
可以按照以下的步骤进行版本迁移：
- 在低压力时间段，先升级一半提供者为新版本
- 再将所有消费者升级为新版本
- 然后将剩下的一半提供者升级为新版本
    
```
<dubbo:service interface="com.foo.BarService" version="1.0.0" />
<dubbo:reference id="barService" interface="com.foo.BarService" version="1.0.0" />
<!-- 不需要区分版本 -->
<dubbo:reference id="barService" interface="com.foo.BarService" version="*" />
```
    

## 结果缓存(cache -> [reference, refer-menthod])
consumer端结果缓存，用于加速热门数据的访问速度，Dubbo 提供声明式缓存，以减少用户加缓存的工作量。

- `lru` 基于最近最少使用原则删除多余缓存，保持最热的数据被缓存。
- `threadlocal` 当前线程缓存，比如一个页面渲染，用到很多 portal，每个 portal 都要去查用户信息，通过线程缓存，可以减少这种多余访问。
- `jcache` 与 JSR107 集成，可以桥接各种缓存实现（如何桥接有待研究）。

```
<dubbo:reference interface="com.foo.BarService" cache="lru" />
<dubbo:reference interface="com.foo.BarService">
    <dubbo:method name="findBar" cache="lru" />
</dubbo:reference>
```
    

## 回声测试
回声测试用于检测服务是否可用，回声测试按照正常请求流程执行，能够测试整个调用是否通畅，可用于监控。 所有服务自动实现 `EchoService` 接口，只需将任意服务引用强制转型为 `EchoService`，即可使用。
    
```
// 远程服务引用
MemberService memberService = ctx.getBean("memberService");
// 强制转型为EchoService
EchoService echoService = (EchoService) memberService;
// 回声测试可用性
String status = echoService.$echo("OK"); 
assert(status.equals("OK"));
```
    

## 上下文信息RpcContext
上下文中存放的是当前调用过程中所需的环境信息。`RpcContext` 是一个 `ThreadLocal` 的临时状态记录器，当接收到 RPC 请求，或发起 RPC 请求时，`RpcContext` 的状态都会变化。
    
```
// rpc远程调用
xxxService.xxx();
// 本端是否为消费端，这里会返回true
boolean isConsumerSide = RpcContext.getContext().isConsumerSide();
// 获取最后一次调用的提供方IP地址
String serverIP = RpcContext.getContext().getRemoteHost();
// 获取当前服务配置信息，所有配置信息都将转换为URL的参数
String application = RpcContext.getContext().getUrl().getParameter("application");
// 注意：每次发起RPC调用，上下文状态会变化
yyyService.yyy();
    

// 服务提供方
public class XxxServiceImpl implements XxxService {
public void xxx() {
    // 本端是否为提供端，这里会返回true
    boolean isProviderSide = RpcContext.getContext().isProviderSide();
    // 获取调用方IP地址
    String clientIP = RpcContext.getContext().getRemoteHost();
    // 获取当前服务配置信息，所有配置信息都将转换为URL的参数
    String application = RpcContext.getContext().getUrl().getParameter("application");
    // 注意：每次发起RPC调用，上下文状态会变化
    yyyService.yyy();
    // 此时本端变成消费端，这里会返回false
    boolean isProviderSide = RpcContext.getContext().isProviderSide();
    } 
}
```
    

## 隐式参数
可以通过 `RpcContext` 上的 `setAttachment` 和 `getAttachment` 在服务消费方和提供方之间进行参数的隐式传递。`setAttachment` 设置的 KV 对，在完成下面一次远程调用会被清空，即多次远程调用要多次设置。
    
注意：`path, group, version, dubbo, token, timeout` 几个 key 是保留字段。
    
```
// 消费端
// 隐式传参，后面的远程调用会隐式将这些参数发送到服务器端，类似cookie，用于框架集成，不建议常规业务使用
RpcContext.getContext().setAttachment("index", "1"); 
xxxService.xxx(); // 远程调用

// 服务端
public class XxxServiceImpl implements XxxService {
    public void xxx() {
        // 获取客户端隐式传入的参数，用于框架集成，不建议常规业务使用
        String index = RpcContext.getContext().getAttachment("index"); 
    }
}
```
    
    
## 事件通知(oninvoke, onreturn, onthrow -> [refer-menthod])
在consumer 调用之前、调用之后、出现异常时，会触发`oninvoke、onreturn、onthrow` 三个事件，可以配置当事件发生时，通知consumer 端哪个类的哪个方法 。
    
```
// consumer 事件通知
<bean id ="demoCallback" class = "org.apache.dubbo.callback.implicit.NofifyImpl" />
<dubbo:reference id="demoService" interface="org.apache.dubbo.callback.implicit.IDemoService" version="1.0.0">
    <dubbo:method name="get" onreturn = "demoCallback.onreturn" onthrow="demoCallback.onthrow" />
</dubbo:reference>
```
    

## 本地伪装Mock (mock -> [reference])
本地伪装通常用于服务降级，比如某验权服务，当服务提供方全部挂掉后，客户端不抛出异常，而是通过 Mock 数据返回授权失败。
    
```
<dubbo:reference interface="com.foo.BarService" mock="true" />
<dubbo:reference interface="com.foo.BarService" mock="com.foo.BarServiceMock" />
<dubbo:reference id="demoService" check="false" interface="com.foo.BarService">
    <dubbo:parameter key="sayHello.mock" value="force:return fake"/>
</dubbo:reference>

public class BarServiceMock implements BarService {
    public String sayHello(String name) {
        // 你可以伪造容错数据，此方法只在出现RpcException时被执行
        return "容错数据";
    }
}
```
    
如果服务的consumer经常需要`try-catch` 捕获异常，请考虑改为 Mock 实现，并在 Mock 实现中 `return null`。
    
`<dubbo:reference interface="com.foo.BarService" mock="return null" />`
    
mock中使用的关键字：
- `return`: 使用 return 来返回一个字符串表示的对象，作为 Mock 的返回值。合法的字符串可以是：
    - `empty`: 代表空，基本类型的默认值，或者集合类的空值
    - `null`: null
    - `true`: true
    - `false`: false
    - JSON 格式：反序列化 JSON 所得到的对象
- `throw`: 使用 throw 来返回一个 Exception 对象，作为 Mock 的返回值。
    
当调用出错时，抛出一个默认的 RPCException: `<dubbo:reference interface="com.foo.BarService" mock="throw" /> `
    
当调用出错时，抛出指定的 Exception： `<dubbo:reference interface="com.foo.BarService" mock="throw com.foo.MockException" />`

- `force` 和 `fail`
    - `force`: 代表强制使用 Mock 行为，在这种情况下不会走远程调用。
    - `fail`:  与默认行为一致，只有当远程调用发生错误时才使用 Mock 行为。
    
`<dubbo:reference interface="com.foo.BarService" mock="force:return true" />`
    

## 延迟暴露(delay -> [service])
如果你的服务需要预热时间，比如初始化缓存，等待相关资源就位等，可以使用 delay 进行延迟暴露。 所有服务都将在 Spring 初始化完成后进行暴露，所以通常你不需要延迟暴露服务，无需配置 delay。
    
`<dubbo:service delay="5000" />`
    
## 并发控制(executes, actives -> [service, method, reference, refer-menthod])
服务器端并发执行 或 占用线程池线程数，这里应该是指的服务端的worker线程池里，同时在运行一个service/method的线程数，这些请求可能来自多个consumer。通常一个consumer客户端只能对一个provider节点的一个service实例建立一个长连接。
    
```
<dubbo:service interface="com.foo.BarService" executes="10" />
<dubbo:service interface="com.foo.BarService">
    <dubbo:method name="sayHello" executes="10" />
</dubbo:service>
```
    
客户端并发执行或占用连接的请求数，这里应该是指的单个socket长连接运行一个service/method的最大线程数，以免单个socket端请求量太大占用了太多线程资源。
    <dubbo:service interface="com.foo.BarService" actives="5" />
    <dubbo:reference interface="com.foo.BarService" actives="5" />
    
    
## 连接控制(connections -> [service, reference], accepts -> [protocol])
Dubbo 协议缺省每个provider节点的每个service的consumer使用单一长连接（如果service有多个provider节点，实际上consumer会拥有多个长连接），如果数据量较大，可以使用多个连接。
    
```
<dubbo:service connections="1"/>
<dubbo:reference connections="1"/>
```
    
为防止被大量连接撑挂，可在provider端限制接收连接数`accepts`，以实现服务提供方自我保护。
    
```
// 限制一个节点provider接受的来自所有consumer的长连接总数不能超过 100 个
<dubbo:protocol name="dubbo" accepts="100" />
// 限制客户端服务使用连接不能超过 1个
// 如果是长连接，比如 Dubbo 协议，connections 表示一个consumer节点可以对该服务单个节点provider建立的长连接数
<dubbo:service interface="com.foo.BarService" connections="10" />
<dubbo:reference interface="com.foo.BarService" connections="10" />
```
    

## 并发控制(executes, actives -> [service, method, reference, refer-menthod])
- `executes`: 服务器端并发执行 或 占用线程池线程数，这里指的是服务端的worker线程池里，同时在运行一个service/method的线程数，这些请求可能来自多个consumer。默认一个consumer客户端只能对一个provider节点的一个service实例建立单一长连接。
    
```
<dubbo:service interface="com.foo.BarService" executes="10" />
<dubbo:service interface="com.foo.BarService">
    <dubbo:method name="sayHello" executes="10" />
</dubbo:service>
```
    
- `actives`: 客户端并发执行或占用连接的请求数，这里指的是当consumer端拥有同一个service的多个长连接(通常是多个provider节点)时，同一时刻同一个service可以用来发送请求的长连接数。
    
```
<dubbo:service interface="com.foo.BarService" actives="5" />
<dubbo:reference interface="com.foo.BarService" actives="5" />
```
    

## 粘滞连接(sticky -> [reference])
粘滞连接用于有状态服务，尽可能让客户端总是向同一提供者发起调用，除非该提供者挂了，再连另一台。
    
`<dubbo:reference id="xxxService" interface="com.xxx.XxxService" sticky="true" />`
    

## 令牌验证(token -> [provider, protocol, service])
通过令牌验证在注册中心控制权限，以决定要不要下发令牌给消费者，可以防止消费者绕过注册中心访问提供者，另外通过注册中心可灵活改变授权方式，而不需修改或升级提供者。
    
```
<!--随机token令牌，使用UUID生成-->
<dubbo:provider interface="com.foo.BarService" token="true" />
<!--固定token令牌，相当于密码-->
<dubbo:provider interface="com.foo.BarService" token="123456" />
<!--固定token令牌，相当于密码-->
<dubbo:protocol name="dubbo" token="123456" />
<!--固定token令牌，相当于密码-->
<dubbo:service interface="com.foo.BarService" token="123456" />
```
    

## 服务降级
可以通过服务降级功能临时屏蔽某个出错的非关键服务，并定义降级后的返回策略。
    
向注册中心写入动态配置覆盖规则：
    
```
RegistryFactory registryFactory = ExtensionLoader.getExtensionLoader(RegistryFactory.class).getAdaptiveExtension();
Registry registry = registryFactory.getRegistry(URL.valueOf("zookeeper://10.20.153.10:2181"));
registry.register(URL.valueOf("override://0.0.0.0/com.foo.BarService?category=configurators&dynamic=false&application=foo&mock=force:return+null"));
```
    
`mock=force:return+null` 表示消费方对该服务的方法调用都直接返回 null 值，不发起远程调用。用来屏蔽不重要服务不可用时对调用方的影响。
    
`mock=fail:return+null` 表示消费方对该服务的方法调用在失败后，再返回 null 值，不抛异常。用来容忍不重要服务不稳定时对调用方的影响。
    

## 序列化(serialization, optimizer -> [protocol])
在Dubbo中使用高效的Java序列化（Kryo和FST）
    
```
<dubbo:protocol name="dubbo" serialization="kryo"/>
<dubbo:protocol name="dubbo" serialization="fst"/>
```
    
要让Kryo和FST完全发挥出高性能，最好将那些需要被序列化的类注册到dubbo系统中。
    
```
// 注册被序列化类
public class SerializationOptimizerImpl implements SerializationOptimizer {
    public Collection<Class> getSerializableClasses() {
        List<Class> classes = new LinkedList<Class>();
        classes.add(BidRequest.class);
        classes.add(BidResponse.class);
        return classes;
    }
}

<dubbo:protocol name="dubbo" serialization="kryo" optimizer="org.apache.dubbo.demo.SerializationOptimizerImpl"/>
```
    

## zookeeper 注册中心

- 服务提供者启动时: 向 `/dubbo/com.xx.xxService/providers` 目录下写入自己的 URL 地址。
- 服务消费者启动时: 订阅 `/dubbo/com.xx.xxService/providers` 目录下的提供者 URL 地址，并向 `/dubbo/com.xx.xxService/consumers` 目录下写入自己的 URL 地址。
- 监控中心启动时:   订阅 `/dubbo/com.xx.xxService` 目录下的所有提供者和消费者 URL 地址。
    
`zookeeper`支持以下功能：
1. 当provider出现断电等异常停机时，注册中心能自动删除提供者信息
2. 当注册中心重启时，能自动恢复注册数据，以及订阅请求
3. 当设置 `<dubbo:registry check="false" />` 时，记录失败注册和订阅请求，后台定时重试
4. 可通过 `<dubbo:registry username="admin" password="1234" />` 设置 zookeeper 登录信息
5. 可通过 `<dubbo:registry group="dubbo" />` 设置 zookeeper 的根节点，不设置将使用无根树
    

Dubbo 支持 zkclient 和 curator 两种 Zookeeper 客户端实现：
1. `<dubbo:registry address="zookeeper://10.20.153.10:2181" client="zkclient" />`
2. `<dubbo:registry address="zookeeper://10.20.153.10:2181?client=zkclient" />`
3. `dubbo.registry.client=zkclient`
    
Zookeeper 单机配置：
    
```
<dubbo:registry address="zookeeper://10.20.153.10:2181" />
<dubbo:registry protocol="zookeeper" address="10.20.153.10:2181" />
```
    
Zookeeper 集群配置：
    
```
<dubbo:registry address="zookeeper://10.20.153.10:2181?backup=10.20.153.11:2181,10.20.153.12:2181" />
<dubbo:registry protocol="zookeeper" address="10.20.153.10:2181,10.20.153.11:2181,10.20.153.12:2181" />
```
    
    
