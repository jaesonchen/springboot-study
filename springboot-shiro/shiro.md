# Shiro 
Apache Shiro 是 Java 的一个安全框架。目前，使用 Apache Shiro 的人越来越多，因为它相当简单，对比 Spring Security，可能没有 Spring Security 做的功能强大，但是在实际工作时可能并不需要那么复杂的东西，所以使用小而简单的 Shiro 就足够了。
    
Shiro 可以帮助我们完成：认证、授权、加密、会话管理、与 Web 集成、缓存等。
    

## Subject
主体，代表了当前 “用户”，这个用户不一定是一个具体的人，与当前应用交互的任何东西都是 Subject，如网络爬虫，机器人等；即一个抽象概念；所有 Subject 都绑定到 SecurityManager，与 Subject 的所有交互都会委托给 SecurityManager；可以把 Subject 认为是一个门面；SecurityManager 才是实际的执行者。
    
## SecurityManager
安全管理器；即所有与安全有关的操作都会与 SecurityManager 交互；且它管理着所有 Subject；可以看出它是 Shiro 的核心，它负责与后边介绍的其他组件进行交互，如果学习过 SpringMVC，你可以把它看成 DispatcherServlet 前端控制器。
    
## Realm
域，Shiro 从从 Realm 获取安全数据（如用户、角色、权限），就是说 SecurityManager 要验证用户身份，那么它需要从 Realm 获取相应的用户进行比较以确定用户身份是否合法；也需要从 Realm 得到用户相应的角色 / 权限进行验证用户是否能进行操作；可以把 Realm 看成 DataSource，即安全数据源。
    
最简单的一个 Shiro 应用：

- 应用代码通过 Subject 来进行认证和授权，而 Subject 又委托给 SecurityManager。

- 我们需要给 Shiro 的 SecurityManager 注入 Realm，从而让 SecurityManager 能得到合法的用户及其权限进行判断。
    

从以上也可以看出，Shiro 不提供维护用户 / 权限，而是通过 Realm 让开发人员自己注入。
    

## Authenticator
认证器，负责主体认证的，这是一个扩展点，如果用户觉得 Shiro 默认的不好，可以自定义实现；其需要认证策略（Authentication Strategy），即什么情况下算用户认证通过了。
    

## Authorizer
授权器，或者访问控制器，用来决定主体是否有权限进行相应的操作；即控制着用户能访问应用中的哪些功能；
    

## SessionManager
Session需要有人去管理它的生命周期，这个组件就是 SessionManager；而 Shiro 并不仅仅可以用在 Web 环境，也可以用在如普通的 JavaSE 环境、EJB 等环境；Shiro 抽象了一个自己的 Session 来管理主体与应用之间交互的数据；如果想把两台服务器的会话数据放到一个地方，这个时候就可以实现自己的分布式会话（如把数据放到 redis 服务器）。
    

## CacheManager
缓存控制器，来管理如用户、角色、权限等的缓存的；因为这些数据基本上很少去改变，放到缓存中后可以提高访问的性能
    

## Cryptography
密码模块，Shiro 提高了一些常见的加密组件用于如密码加密 / 解密的。
    



# 身份验证
身份验证，即在应用中谁能证明他就是他本人。一般提供如身份 ID 一些标识信息来表明他就是他本人，如提供身份证，用户名 / 密码来证明。
    
在 shiro 中，用户需要提供 `principals` （身份）和` credentials`（证明）给 shiro，从而应用能验证用户身份：
    
- `principals`：身份，即主体的标识属性，可以是任何东西，如用户名、邮箱等，唯一即可。一个主体可以有多个 principals，但只有一个 Primary principals，一般是用户名 / 密码 / 手机号。

- `credentials`：证明 / 凭证，即只有主体知道的安全值，如密码 / 数字证书等。
    
最常见的 `principals` 和 `credentials` 组合就是用户名 / 密码了。另外两个相关的概念是之前提到的 Subject 及 Realm，分别是主体及验证主体的数据源。
    
```
Subject subject = SecurityUtils.getSubject();
UsernamePasswordToken token = new UsernamePasswordToken("zhang", "123");
try {
    // 登录，即身份验证
    subject.login(token);
} catch (AuthenticationException e) {
    // 身份验证失败
    //
}
Assert.assertEquals(true, subject.isAuthenticated()); // 断言用户已经登录
// 退出
subject.logout();
```
    

## 身份验证异常
如果身份验证失败请捕获 `AuthenticationException` 或其子类，常见的如： `DisabledAccountException`（禁用的帐号）、`LockedAccountException`（锁定的帐号）、`UnknownAccountException`（错误的帐号）、`ExcessiveAttemptsException`（登录失败次数过多）、`IncorrectCredentialsException` （错误的凭证）、`ExpiredCredentialsException`（过期的凭证）等。
    

## 身份认证流程
1. 首先调用 `Subject.login(token)` 进行登录，其会自动委托给 `Security Manager`。

2. `SecurityManager` 负责真正的身份验证逻辑；它会委托给 `Authenticator` 进行身份验证。

3. `Authenticator` 才是真正的身份验证者，Shiro API 中核心的身份认证入口点，此处可以自定义自己的实现。

4. `Authenticator` 可能会委托给相应的 `AuthenticationStrategy` 进行多 `Realm` 身份验证，默认 `ModularRealmAuthenticator` 会调用 `AuthenticationStrategy` 进行多 `Realm` 身份验证。

5. `Authenticator` 会把相应的 `token` 传入 `Realm`，从 `Realm` 获取身份验证信息，如果没有返回 / 抛出异常表示身份验证失败了。此处可以配置多个 Realm，将按照相应的顺序及策略进行访问。
     


# 授权
授权，也叫访问控制，即在应用中控制谁能访问哪些资源（如访问页面/编辑数据/页面操作等）。在授权中需了解的几个关键对象：主体（`Subject`）、资源（`Resource`）、权限（`Permission`）、角色（`Role`）。
    

## 主体
主体，即访问应用的用户，在 Shiro 中使用 Subject 代表该用户。用户只有授权后才允许访问相应的资源。
    
## 资源
在应用中用户可以访问的任何东西，比如访问 JSP 页面、查看/编辑某些数据、访问某个业务方法、打印文本等等都是资源。用户只要授权后才能访问。
    
## 权限
安全策略中的原子授权单位，通过权限我们可以表示在应用中用户有没有操作某个资源的权力。即权限表示在应用中用户能不能访问某个资源，如： 访问用户列表页面，查看/新增/修改/删除用户数据（即很多时候都是 CRUD（增查改删）式权限控制）。
    

## 角色
角色代表了操作集合，可以理解为权限的集合，一般情况下我们会赋予用户角色而不是权限，即这样用户可以拥有一组权限，赋予权限时比较方便。典型的如：项目经理、技术总监、CTO、开发工程师等都是角色，不同的角色拥有一组不同的权限。









