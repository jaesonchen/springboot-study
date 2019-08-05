# Mybatis 乐观锁、悲观锁
1. 悲观锁是指用for update行锁定保证事务安全，这样每次都要申请锁，并发时代价大。

2. 乐观锁是指更新时不进行锁定，通过增加version之类得字段来保证事务安全，类似于CAS中Atomic使用timestamp来判断ABA。
    

# 多数据版本支持
mybatis的sql多数据版本使用`databaseId` 属性标注，如果需要支持多个类型数据库，需要在`mapper.xml`中定义多个同名的方法映射。
    
## 注册DatabaseIdProvider
```
@Bean
public VendorDatabaseIdProvider databaseIdProvider() {
    VendorDatabaseIdProvider provider = new VendorDatabaseIdProvider();
    Properties prop = new Properties();
    prop.put("Oracle", "oracle");
    prop.put("MySQL", "mysql");
    provider.setProperties(prop);
    return provider;
}
```
    

## mapper.xml里定义多数据库版本方法
```
<select id="findById" parameterType="long" resultMap="userResult" databaseId="mysql">
    select 
        <include refid="selectUserColumnList" />
    from user
    where id=#{id}
</select>
```
    

# like 查询参数注入
`like` 模糊查询不能直接使用 `like '%#{userName}%'`的方法注入参数。
    
## 使用${}注入
`${}`是参数直接注入，但是会导致sql注入问题。
    
`select * from user where userName like '%${userName}%'`
    

## 使用concat连接符
`select * from user where userName like concat('%', #{userName}, '%')`
    

## 使用 || 连接符
`select * from user where userName like '%' || #{userName} || '%'`
    

## 在内存中拼接%
```
String userName = new StringBuilder("%").append(userName).append("%").toString();

select * from user where userName like #{userName}
```
    

# enum枚举类型转换
MyBatis内置了两个枚举转换器分别是：`org.apache.ibatis.type.EnumTypeHandler`和`org.apache.ibatis.type.EnumOrdinalTypeHandler`。
    
## EnumTypeHandler
这是默认的枚举转换器，`EnumTypeHandler`将使用枚举实例名称来和对应的枚举类之间做转换。即将`RoleType.DATA`转换成字符串`DATA`存入数据库 或是 从数据库中查询到的字符串`DATA`对应到枚举类`RoleType.DATA`上。
    

## EnumOrdinalTypeHandler
`EnumOrdinalTypeHandler`将使用枚举实例的 `ordinal` 值（序数值，从0开始）来和枚举类之间做转换。
    
全局指定 `typeHandler`：
    
```
<typeHandlers>
    <typeHandler handler="org.apache.ibatis.type.EnumOrdinalTypeHandler" javaType="com.xx.enums.RoleType"/>
</typeHandlers>
```
    

## 自定义枚举处理器
枚举处理器也是处理器（`typeHandler`）的一种，主要操作便是实现`org.apache.ibatis.type.TypeHandler`或继承更为方便的`org.apache.ibatis.type.BaseTypeHandler`类。
    
我们选择继承`BaseTypeHandler`来完成工作，`BaseTypeHandler`需要实现4个方法：

- `public abstract void setNonNullParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException`
    
用于定义设置参数时，该如何把 Java 类型的参数转换为对应的数据库类型，`jdbcType`通过mapper中的`#{roleType, jdbcType=VARCHAR})`指定，通常不需要指定。
    
- `public abstract T getNullableResult(ResultSet rs, String columnName) throws SQLException`
    
用于定义通过字段名称获取字段数据时，如何把数据库类型转换为对应的 Java 类型
    
- `public abstract T getNullableResult(ResultSet rs, int columnIndex) throws SQLException`
    
用于定义通过字段索引获取字段数据时，如何把数据库类型转换为对应的 Java 类型
    
- `public abstract T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException`
    
用定义调用存储过程后，如何把数据库类型转换为对应的 Java 类型。
    
枚举类型定义：
    
```
public interface EnumValue<T> {
    public T getValue();
}

public enum RoleType implements EnumValue<String> {

    FUNC("101", "功能权限"), DATA("102", "数据权限");
    
    private String code;
    private String desc;
    private RoleType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public String getCode() {
        return code;
    }
    public String getDesc() {
        return desc;
    }
    @Override
    public String getValue() {
        return code;
    }
}
```
    
枚举转换器：
    
```
@MappedJdbcTypes(JdbcType.VARCHAR)
@MappedTypes({ RoleType.class })
public class MyEnumTypeHandler<E extends Enum<E>> extends BaseTypeHandler<E> {

    private Class<E> type;
    
    public MyEnumTypeHandler(Class<E> type) {
        if (type == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        }
        this.type = type;
    }
    
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
        // jdbcType由 #{roleType, jdbcType=VARCHAR} 指定，如果没有指定为null
        if (jdbcType == null) {
            System.out.println("jdbcType = null");
            ps.setString(i, getValue(parameter));
        } else {
            System.out.println("jdbcType = " + jdbcType);
            ps.setObject(i, getValue(parameter), jdbcType.TYPE_CODE);
        }
    }

    @Override
    public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String str = rs.getString(columnName);
        return str == null ? null : codeOf(type, str);
    }

    @Override
    public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String str = rs.getString(columnIndex);
        return str == null ? null : codeOf(type, str);
    }

    @Override
    public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String str = cs.getString(columnIndex);
        return str == null ? null : codeOf(type, str);
    }
    
    // 转换为enum
    @SuppressWarnings("unchecked")
    public E codeOf(Class<E> enumClass, String code) {
        if (EnumValue.class.isAssignableFrom(enumClass)) {
            for (E e : enumClass.getEnumConstants()) {
                if (((EnumValue<String>) e).getValue().equals(code)) {
                    return e;
                }
            }
        }
        return Enum.valueOf(enumClass, code);
    }
    
    // 从enum获取写入值
    @SuppressWarnings("unchecked")
    public String getValue(E parameter) {
        if (parameter instanceof EnumValue) {
            return ((EnumValue<String>) parameter).getValue();
        }
        return parameter.name();
    }
}
```
    
可以使用`@MappedTypes({ RoleType.class })`定义多个相同类型的映射枚举类型，在springboot启动时会自动为每个枚举类型注册一个转换器实例。
    
springboot自动注册类型转换器：
`mybatis.type-handlers-package = com.asiainfo.springboot.mybatis.handler`
    

# 多参数传递
    
## 顺序传参
`#{}`里面的数字代表你传入参数的顺序，从0开始。
    
```
public User findByName(String deptId, String userName);

<select id="findByName" resultMap="userResult">
    select 
        <include refid="selectUserColumnList" />
    from user
    where dept_id=#{0} and user_name=#{1}
</select>
```
    
## @Param注解传参
`#{}`里面的名称对应的是注解` @Param`括号里面修饰的名称。
    
```
public User findByName(@Param("deptId") String deptId, @Param("userName") String userName);

<select id="findByName" resultMap="userResult">
    select 
        <include refid="selectUserColumnList" />
    from user
    where dept_id=#{deptId} and user_name=#{userName}
</select>
```
    
## java.util.Map传参
`#{}`里面的名称对应的是 Map里面的key名称。
    
```
public User findByName(Map<String, Object> params);

<select id="findByName" parameterType="java.util.Map" resultMap="userResult">
    select 
        <include refid="selectUserColumnList" />
    from user
    where dept_id=#{deptId} and user_name=#{userName}
</select>
```
   
## Java Bean传参
`#{}`里面的名称对应的是 User类里面的成员属性。
    
```
public User findByName(User user);

<select id="findByName" parameterType="User" resultMap="userResult">
    select 
        <include refid="selectUserColumnList" />
    from user
    where dept_id=#{deptId} and user_name=#{userName}
</select>
```
    

# foreach 处理 list、array、map
`foreach` 可以在SQL语句中迭代一个集合。通常用在构建in 或者 批量插入的values。
    
`foreach`元素的属性主要有`item, index, collection, open, separator, close`。
    
- `item` 表示集合中每一个元素进行迭代时的别名

- `index` 指定一个名字，用于表示在迭代过程中，每次迭代到的位置

- `open` 表示该语句以什么开始

- `separator` 表示在每次进行迭代之间以什么符号作为分隔符

- `close` 表示以什么结束

- `collection` 属性是在使用`foreach`的时候最关键的也是最容易出错的，该属性是必须指定的，但是在不同情况下，该属性的值是不一样的，主要有一下3种情况： 

    - 如果传入的是单参数且参数类型是一个List的时候，collection属性值为list
    - 如果传入的是单参数且参数类型是一个array数组的时候，collection的属性值为array
    - 如果传入的参数是多个的时候，我们就需要把它们封装成一个Map或者Java Bean了，当然单参数也可以封装成map，实际上如果你在传入参数的时候，在MyBatis里面也是会把它封装成一个Map的，map的key就是参数名，所以这个时候collection属性值就是传入的List或array对象在自己封装的map里面的key
    
```
<!-- list -->
public List<User> findByIds(List<String> list);

<select id="findByIds" parameterType="java.util.List" resultMap="userResult">
    select * from user 
    where id in 
    <foreach collection="list" item="id" index="index" open="(" separator="," close=")">
        #{id}
    </foreach>
</select>

<!-- array -->
public List<User> findByIds(String[] ids);

<select id="findByIds" resultMap="userResult">
    select * from user 
    where id in 
    <foreach collection="array" item="id" index="index" open="(" separator="," close=")">
        #{id}
    </foreach>
</select>

<!-- map -->
params.put("ids", new String[] {"1", "2", "3"});
public List<User> findByIds(Map<String, Object> params);

<select id="findByIds" parameterType="java.util.Map" resultMap="userResult">
    select * from user 
    where id in 
    <foreach collection="ids" item="id" index="index" open="(" separator="," close=")">
        #{id}
    </foreach>
</select>
```
    

# 拦截器
MyBatis 允许你在已映射语句执行过程中的某一点进行拦截调用。MyBatis 允许使用插件来拦截的方法调用包括：
    
- `Executor (update, query, flushStatements, commit, rollback, getTransaction, close, isClosed)`
- `ParameterHandler (getParameterObject, setParameters)`
- `ResultSetHandler (handleResultSets, handleOutputParameters)`
- `StatementHandler (prepare, parameterize, batch, update, query)`
    
拦截器实现：
    
```
@Intercepts({
        @Signature(type = StatementHandler.class, method = "query", args = { Statement.class, ResultHandler.class }),
        @Signature(type = StatementHandler.class, method = "update", args = { Statement.class }) })
public class MyInterceptor implements Interceptor {

    final Logger logger = LoggerFactory.getLogger(getClass());
    
    Properties properties;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        BoundSql boundSql = statementHandler.getBoundSql();
        logger.info("执行SQL: {}", boundSql.getSql());

        long beginTime = System.currentTimeMillis();
        Object result = invocation.proceed();
        logger.info("执行时间: {}ms", (System.currentTimeMillis() - beginTime));
        return result;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        logger.info("注入属性: {}", properties);
        this.properties = properties;
    }
}
```
    
拦截器注册：
    
```
    @Bean
    public MyInterceptor myInterceptor() {
        MyInterceptor myInterceptor = new MyInterceptor();
        Properties properties = new Properties();
        properties.setProperty("welcome", "hello mybatis");
        myInterceptor.setProperties(properties);
        return myInterceptor;
    }
```
    

# 分页实现

## 内存分页
全量查询，在内存中分页。
    
## sql分页
方法参数中传入offset, pageSize, 在sql语句里通过 limit offset, pageSize进行物理分页。在嵌套查询时，pagehelper拦截插件无法实现分页，可以考虑使用sql分页。
    
`select * from user limit #{offset}, #{pageSize}`
    
## RowBounds分页
逻辑分页会将所有的结果都查询到，然后根据RowBounds中提供的offset和limit值来获取最后的结果。
    
```
// 使用selectList
List<User> list = session.selectList("com.asiainfo.springboot.mybatis.dao.UserDao.findAll", null, new RowBounds(0, 5));

// 接口中加入RowBounds参数
public List<User> findAll(RowBounds rowBounds);
List<User> list = userDao.findAll(new RowBounds(0, 5));
```
    
## 拦截器分页 pagehelper
引入依赖：
    
```
<dependency>
    <groupId>com.github.pagehelper</groupId>
    <artifactId>pagehelper-spring-boot-starter</artifactId>
    <version>1.2.12</version>
</dependency>
```

分页使用：
    
```
PageHelper.startPage(1, 10);
List<User> list = userDao.findAll();
PageInfo<User> page = new PageInfo<>(list);
```
    
拦截器分页方式的限制：无法对嵌套查询进行正确的分页，查询出来的总数和行内容是left/right join的行，对于连接查询pagehelper只适合mapper.xml配置为select类型的collection，select查询会多进行一次查询。mabatis的注解配置好像只支持select查询。
    
对于需要嵌套查询的方法，数据量小时可以使用内存分页和RowBounds逻辑分页。数据量大时，最好使用sql分页。
    
```
select 
    u.id, u.user_name, u.password, 
    c.id as contact_id, c.address, c.zip_code
from (select * from user limit #{offset}, #{pageSize}) u
left join contact c on u.id=c.user_id
```
    
