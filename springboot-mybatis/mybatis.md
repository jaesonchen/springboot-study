# mybatis 语法

## select

```
<select
    id="selectById"             - 在命名空间中唯一的标识符，可以被用来引用这条语句。
    parameterType="int"         - 将会传入这条语句的参数类的完全限定名或别名。这个属性是可选的，因为 MyBatis 可以通过类型处理器（TypeHandler）
                                - 推断出具体传入语句的参数，默认值为未设置（unset）。
    resultType="hashmap"        - 从这条语句中返回的期望类型的类的完全限定名或别名。 注意如果返回的是集合，那应该设置为集合包含的类型，而不是集合本身。
                                - 可以使用 resultType 或 resultMap，但不能同时使用。
    resultMap="personResultMap" - 外部 resultMap 的命名引用。结果集的映射是 MyBatis 最强大的特性，如果你对其理解透彻，许多复杂映射的情形都能迎刃而解。
    flushCache="false"          - 将其设置为 true 后，只要语句被调用，都会导致本地缓存和二级缓存被清空，默认值：false。
    useCache="true"             - 将其设置为 true 后，将会导致本条语句的结果被二级缓存缓存起来，默认值：对 select 元素为 true。
    timeout="10"                - 在抛出异常之前，驱动程序等待数据库返回请求结果的秒数。默认值为未设置（unset）（依赖驱动）。
    fetchSize="256"             - 这是一个给驱动的提示，尝试让驱动程序每次批量返回的结果行数和这个设置值相等。 默认值为未设置（unset）（依赖驱动）。
    statementType="PREPARED"        - STATEMENT，PREPARED 或 CALLABLE 中的一个。这会让 MyBatis 分别使用 Statement，PreparedStatement 或 
                                    - CallableStatement，默认值：PREPARED。
    resultSetType="FORWARD_ONLY"    - FORWARD_ONLY，SCROLL_SENSITIVE, SCROLL_INSENSITIVE 或 DEFAULT（等价于 unset） 中的一个，默认值为 unset。
    databaseId="mysql">             - 如果配置了数据库厂商标识（databaseIdProvider），MyBatis 会加载所有的不带 databaseId 或匹配当前 
                                    - databaseId 的语句；如果带或者不带的语句都有，则不带的会被忽略。
```
    
示例：`#{id}` 这就告诉 MyBatis 创建一个预处理语句（PreparedStatement）参数。
    
```
`PreparedStatement ps = conn.prepareStatement(selectById);
ps.setInt(1,id);`

<select id="selectById" parameterType="int" resultType="hashmap">
    SELECT * FROM PERSON WHERE ID = #{id}
</select>
```
    

## insert, update 和 delete
插入语句的配置规则更加丰富，在插入语句里面有一些额外的属性和子元素用来处理主键的生成，而且有多种生成方式。
    
```
<insert / update / delete
    id="insertAuthor"
    parameterType="domain.blog.Author"
    statementType="PREPARED"
    timeout="20"
    flushCache="true"   - 将其设置为 true 后，只要语句被调用，都会导致本地缓存和二级缓存被清空，默认值：true（对于 insert、update 和 delete 语句）。
    keyProperty="id"    - （仅对 insert 和 update 有用）唯一标记一个属性，MyBatis 会通过 getGeneratedKeys 的返回值或者通过 insert 语句的 selectKey 
                        -  子元素设置它的键值，默认值：未设置（unset）。如果希望得到多个生成的列，也可以是逗号分隔的属性名称列表。
    keyColumn="id"      - （仅对 insert 和 update 有用）通过生成的键值设置表中的列名，这个设置仅在某些数据库（ PostgreSQL）是必须的，
                        -  当主键列不是表中的第一列的时候需要设置。如果希望使用多个生成的列，也可以设置为逗号分隔的属性名称列表。
    useGeneratedKeys="true">    - （仅对 insert 和 update 有用）这会令 MyBatis 使用 JDBC 的 getGeneratedKeys 方法来取出由数据库内部生成的主键
                                - （比如： MySQL 这样的关系数据库的自动递增字段），true/false, 默认值：false。
                                - 如果你的数据库支持自动生成主键的字段（比如 MySQL 和 SQL Server），那么你可以设置 useGeneratedKeys=”true”，
                                - 然后再用keyProperty="id" 设置到目标属性上就 OK 了。
```
    
示例：
    
```
<insert id="insertAuthor" useGeneratedKeys="true" keyProperty="id" keyColumn="id">
    insert into Author (username,password,email,bio)
    values (#{username},#{password},#{email},#{bio})
</insert>

# 如果你的数据库还支持多行插入, 你也可以传入一个 Author 数组或集合，并返回自动生成的主键。
<insert id="insertAuthor" useGeneratedKeys="true" keyProperty="id">
    insert into Author (username, password, email, bio) values 
    <foreach item="item" collection="list" separator=",">
        (#{item.username}, #{item.password}, #{item.email}, #{item.bio})
    </foreach>
</insert>
```
    
## selectKey
对于不支持自动生成类型的数据库或可能不支持自动生成主键的 JDBC 驱动，MyBatis 有另外一种方法来生成主键。
    
```
<selectKey              - selectKey 语句结果应该被设置的目标属性。如果希望得到多个生成的列，也可以是逗号分隔的属性名称列表。
    keyProperty="id"    - 匹配属性的返回结果集中的列名称。如果希望得到多个生成的列，也可以是逗号分隔的属性名称列表。
    resultType="int"    - 结果的类型。MyBatis 通常可以推断出来，但是为了更加精确，写上也不会有什么问题。MyBatis 
                        - 允许将任何简单类型用作主键的类型，包括字符串。
    order="BEFORE"      - 这可以被设置为 BEFORE 或 AFTER。如果设置为 BEFORE，那么它会首先生成主键，设置 keyProperty 然后执行插入语句。
                        - 如果设置为 AFTER，那么先执行插入语句，然后是 selectKey 中的语句，这和 Oracle 数据库的行为相似。
    statementType="PREPARED"> - 支持 STATEMENT，PREPARED 和 CALLABLE 语句的映射类型。
```
    
```
# selectKey 元素中的语句将会首先运行，Author 的 id 会被设置，然后插入语句会被调用。
<insert id="insertAuthor">
    <selectKey keyProperty="id" resultType="int" order="BEFORE">
        select CAST(RANDOM()*1000000 as INTEGER) a from SYSIBM.SYSDUMMY1
    </selectKey>
    insert into Author
        (id, username, password, email, bio, favourite_section)
    values
        (#{id}, #{username}, #{password}, #{email}, #{bio}, #{favouriteSection,jdbcType=VARCHAR})
</insert>
```
    

## sql 
这个元素可以被用来定义可重用的 SQL 代码段，这些 SQL 代码可以被包含在其他语句中。它可以（在加载的时候）被静态地设置参数。 在不同的包含语句中可以设置不同的值到参数占位符上。
    
```
<sql id="userColumns"> ${user_alias}.user_id, ${user_alias}.user_name, ${user_alias}.password </sql>
<sql id="deptColumns"> ${dept_alias}.dept_id, ${dept_alias}.dept_name </sql>

<select id="selectUsers" resultType="map">
    select
        <include refid="userColumns"><property name="user_alias" value="t1"/></include>, 
        <include refid="deptColumns"><property name="dept_alias" value="t2"/></include>
    from user t1 
    join department t2 on t1.dept_id = t2.dept_id
</select>
```
    

## 参数#{}
参数是 MyBatis 非常强大的元素。大约 90% 的情况下你使用的都是简单参数。原始类型或简单数据类型（比如 Integer 和 String）因为没有相关属性，它会完全用参数值来替代。
    
```
<select id="selectUsers" resultType="User">
    select id, username, password
    from users
    where id = #{id}
</select>
```
    
如果传入一个复杂的对象，行为就会有一点不同了，User 类型的参数对象传递到了语句中，id、username 和 password 属性将会被查找，然后将它们的值传入预处理语句的参数中。
    
```
<insert id="insertUser" parameterType="User">
    insert into users (id, username, password)
    values (#{id}, #{username}, #{password})
</insert>
```
    
参数也可以指定一个特殊的数据类型。大多时候你只须简单地指定属性名，其他的事情 MyBatis 会自己去推断，顶多要为可能为空的列指定 jdbcType。
    
`#{property, javaType=int, jdbcType=NUMERIC}`
    
javaType 几乎总是可以根据参数对象的类型确定下来，除非该对象是一个 HashMap。这个时候，你需要显式指定 javaType 来确保正确的类型处理器（TypeHandler）被使用。JDBC 要求，如果一个列允许 null 值，并且会传递值 null 的参数，就必须要指定 JDBC Type。
    
要更进一步地自定义类型处理方式，你也可以指定一个特殊的类型处理器类（或别名）。`#{age, javaType=int, jdbcType=NUMERIC, typeHandler=com.xxx.MyTypeHandler}` 。
    
对于数值类型，还有一个小数保留位数的设置，来指定小数点后保留的位数。`#{height, javaType=double, jdbcType=NUMERIC, numericScale=2}`
    

## 字符串替换 ${}
默认情况下,使用 #{} 格式的语法会导致 MyBatis 创建 PreparedStatement 参数占位符并安全地设置参数（就像使用 ? 一样）。不过有时你就是想直接在 SQL 语句中插入一个不转义的字符串， `${}`字符串替换将会非常有用。用这种方式接受用户的输入，并将其用于语句中的参数是不安全的，会导致潜在的 SQL 注入攻击，因此要么不允许用户输入这些字段，要么自行转义并检验。
    
`${column}` 会被直接替换，而 `#{name}` 会被使用 `?` 预处理。
    
```
@Select("select * from user where ${column} = #{value}")
User findByColumn(@Param("column") String column, @Param("value") String value);

// 调用
User userOfId1 = userMapper.findByColumn("id", 1L);
User userOfNameKid = userMapper.findByColumn("name", "kid");
User userOfEmail = userMapper.findByColumn("email", "noone@nowhere.com");
```
    

## resultMap
resultMap 元素是 MyBatis 中最重要最强大的元素。它可以让你从 90% 的 JDBC ResultSets 数据提取代码中解放出来，并在一些情形下允许你进行一些 JDBC 不支持的操作。resultMap 的设计思想是，对于简单的语句根本不需要配置显式的结果映射，而对于复杂一点的语句只需要描述它们的关系就行了。
    
```
# HashMap 映射
<select id="selectUsers" resultType="map">
    select id, user_name as userName, password
    from user
    where id = #{id}
</select>

# JavaBean 映射
<select id="selectUsers" resultType="User">
```
    
类型别名是你的好帮手。使用它们，你就可以不用输入类的完全限定名称了。
    
```
<!-- mybatis-config.xml 中 -->
<typeAliases>
    <typeAlias type="com.asiainfo.mybatis.entity.User" alias="User"/>
    <package name="com.asiainfo.mybatis.entity"/>
</typeAliases>
```
    
如果列名和属性名没有精确匹配，可以在 SELECT 语句中对列使用别名（这是一个基本的 SQL 特性）来匹配标签。也可以使用外部的 resultMap ，这也是解决列名不匹配的另外一种方式。
    
```
<resultMap id="userResultMap" type="User">
    <id property="id" column="id" />
    <result property="userName" column="user_name"/>
    <result property="password" column="password"/>
</resultMap>

<select id="selectUsers" resultMap="userResultMap">
```
    

## 高级结果映射
 MyBatis 创建时的一个思想是：数据库不可能永远是你所想或所需的那个样子。 我们希望每个数据库都具备良好的第三范式或 BCNF 范式，可惜它们不总都是这样。
      
```
<resultMap 
    id="detailedBlogResultMap"  - 当前命名空间中的一个唯一标识，用于标识一个结果映射。
    type="Blog"                 - 类的完全限定名, 或者一个类型别名
    autoMapping="false">        - MyBatis将会为本结果映射开启或者关闭自动映射。 会覆盖全局属性 autoMappingBehavior。默认值：未设置（unset）。
    
    <constructor>               - 用于在实例化类时，注入结果到构造方法中
        <idArg column="blog_id" javaType="int"/>    - idArg ID 参数；标记出作为 ID 的结果可以帮助提高整体性能
        <arg column="blog_name" javaType="String"/> -  将被注入到构造方法的一个普通结果
    </constructor>
    
    <result                         - 注入到字段或 JavaBean 属性的普通结果
        property="title"            - 映射到列结果的字段或属性。如果用来匹配的 JavaBean 存在给定名字的属性，那么它将会被使用。
                                    - 否则 MyBatis 将会寻找给定名称的字段。可以映射到一些复杂的东西上：address.zipcode
        column="blog_title"         - 数据库中的列名，或者是列的别名。
        javaType                    - 一个 Java 类的完全限定名，或一个类型别名，如果你映射到的是 HashMap，
                                    - 那么你应该明确地指定 javaType 来保证行为与期望的相一致。
        jdbcType                    - JDBC 类型，只需要在可能执行插入、更新和删除的且允许空值的列上指定 JDBC 类型。
                                    - 这是 JDBC 的要求而非 MyBatis 的要求。
        typeHandler />                - 使用这个属性，你可以覆盖默认的类型处理器。
        
    <association property="author" javaType="Author">       - 一个复杂类型的关联；许多结果将包装成这种类型，关联本身可以是一个 resultMap 元素，
                                                            - 或者从别处引用
        <id property="id" column="author_id"/>              - id 元素表示的结果将是对象的标识属性，这会在比较对象实例时用到，可以帮助提高整体性能
        <result property="username" column="author_username"/>
        <result property="password" column="author_password"/>
    </association>
    
    <collection property="posts" ofType="Post">             - 一个复杂类型的集合， 集合本身可以是一个 resultMap 元素，或者从别处引用
        <id property="id" column="post_id"/>
        <result property="subject" column="post_subject"/>
        <collection property="comments" ofType="Comment">
            <id property="id" column="comment_id"/>
            <result property="comment" column="comment"/>
        </collection>
        <discriminator javaType="int" column="draft">       - 使用结果值来决定使用哪个 resultMap
            <case value="1" resultType="DraftPost"/>        - case 基于某些值的结果映射，case 本身可以是一个 resultMap 元素，
                                                            - 因此可以具有相同的结构和元素，或者从别处引用
        </discriminator>
    </collection>
</resultMap>
```
    
    
## 关联（association）
嵌套 Select 查询：通过执行另外一个 SQL 映射语句来加载期望的复杂类型。这种方式虽然很简单，但在大型数据集或大型数据表上表现不佳。这个问题被称为“N+1 查询问题”。
    
```
<resultMap id="blogResult" type="Blog">
    <association property="author" column="author_id" javaType="Author" select="selectAuthor"/>
</resultMap>
<select id="selectBlog" resultMap="blogResult">
    SELECT * FROM BLOG WHERE ID = #{id}
</select>
<select id="selectAuthor" resultType="Author">
    SELECT * FROM AUTHOR WHERE ID = #{id}
</select>
```
    
嵌套结果映射：使用嵌套的结果映射来处理连接结果的重复子集。id 元素在嵌套结果映射中扮演着非常重要的角色。你应该总是指定一个或多个可以唯一标识结果的属性。
    
```
<select id="selectBlog" resultMap="blogResult">
    select
        B.id            as blog_id,
        B.title         as blog_title,
        B.author_id     as blog_author_id,
        A.id            as author_id,
        A.username      as author_username,
        A.password      as author_password,
        A.email         as author_email,
    from Blog B left outer join Author A on B.author_id = A.id
    where B.id = #{id}
</select>
<resultMap id="blogResult" type="Blog">
    <id property="id" column="blog_id" /> 
    <result property="title" column="blog_title"/>
    <association property="author" column="blog_author_id" javaType="Author" resultMap="authorResult"/>
</resultMap>
<resultMap id="authorResult" type="Author">
    <id property="id" column="author_id"/> 
    <result property="username" column="author_username"/>
    <result property="password" column="author_password"/>
    <result property="email" column="author_email"/>
</resultMap>
```
    

## 集合(collection)
集合大部分类似于关联，多了一个重要属性 ofType 指明集合内的元素类型。
    
### 集合的Select 查询：
```
<resultMap id="blogResult" type="Blog">
    <collection property="posts" javaType="ArrayList" column="id" ofType="Post" select="selectPostsForBlog"/>
</resultMap>
<select id="selectBlog" resultMap="blogResult">
    SELECT * FROM BLOG WHERE ID = #{id}
</select>
<select id="selectPostsForBlog" resultType="Post">
    SELECT * FROM POST WHERE BLOG_ID = #{id}
</select>
```
    
### 集合的嵌套结果映射
id 元素在嵌套结果映射中扮演着非常重要的角色。你应该总是指定一个或多个可以唯一标识结果的属性。
    
```
<resultMap id="blogResult" type="Blog">
    <id property="id" column="blog_id" />
    <result property="title" column="blog_title"/>
    <collection property="posts" ofType="Post">
        <id property="id" column="post_id"/>
        <result property="subject" column="post_subject"/>
        <result property="body" column="post_body"/>
    </collection>
</resultMap>
```
    

## 鉴别器(discriminator)
有时候，一个数据库查询可能会返回多个不同的结果集（但总体上还是有一定的联系的）。 鉴别器就是被设计来应对这种情况的，它很像 Java 语言中的 switch 语句。一个鉴别器的定义需要指定 column 和 javaType 属性。column 指定了 MyBatis 查询被比较值的地方。 而 javaType 用来确保使用正确的相等测试。
    
```
<resultMap id="vehicleResult" type="Vehicle">
    <id property="id" column="id" />
    <result property="year" column="year"/>
    <result property="color" column="color"/>
    <discriminator javaType="int" column="vehicle_type">
        <case value="1" resultType="carResult">
            <result property="doorCount" column="door_count" />
        </case>
        <case value="2" resultType="truckResult">
            <result property="boxSize" column="box_size" />
            <result property="extendedCab" column="extended_cab" />
        </case>
    </discriminator>
</resultMap>
```
    

## 常用的jdbc类型
```
   TINYINT  VARCHAR BINARY      BLOB        NVARCHAR
   SMALLINT DOUBLE  CLOB        NCHAR       DECIMAL
   INTEGER  NUMERIC DATE        BOOLEAN     TIME
   FLOAT    CHAR    TIMESTAMP   BIGINT      NULL
```
    

## 动态 SQL
MyBatis 的强大特性之一便是它的动态 SQL。如果你有使用 JDBC 或其它类似框架的经验，你就能体会到根据不同条件拼接 SQL 语句的痛苦。MyBatis 3 大大精简了元素种类，现在只需学习原来一半的元素便可。MyBatis 采用功能强大的基于 OGNL 的表达式来淘汰其它大部分元素。

### if
```
<select id="findActiveBlogLike" resultType="Blog">
    SELECT * FROM BLOG WHERE state = ‘ACTIVE’
    <if test="title != null">
        AND title like #{title}
    </if>
    <if test="author != null and author.name != null">
        AND author_name like #{author.name}
    </if>
</select>
```
    
### choose (when, otherwise)
有时我们不想应用到所有的条件语句，而只想从中择其一项。针对这种情况，MyBatis 提供了 choose 元素，它有点像 Java 中的 switch 语句。
    
```
<select id="findActiveBlogLike" resultType="Blog">
    SELECT * FROM BLOG WHERE state = ‘ACTIVE’
    <choose>
        <when test="title != null">
            AND title like #{title}
        </when>
        <when test="author != null and author.name != null">
          AND author_name like #{author.name}
        </when>
        <otherwise>
          AND featured = 1
        </otherwise>
    </choose>
</select>
```
    
### trim (where, set)
- where 元素只会在至少有一个子元素的条件返回 SQL 子句的情况下才去插入“WHERE”子句。而且，若语句的开头为“AND”或“OR”，where 元素也会将它们去除。
    
```
<select id="findActiveBlogLike" resultType="Blog">
    SELECT * FROM BLOG
    <where>
        <if test="state != null">state = #{state}</if>
        <if test="title != null"> AND title like #{title}</if>
        <if test="author != null and author.name != null"> AND author_name like #{author.name}</if>
    </where>
</select>
```
   
- set 元素会动态前置 SET 关键字，同时也会删掉无关的逗号
    
```
<update id="updateAuthorIfNecessary">
    update Author
    <set>
        <if test="username != null">username=#{username},</if>
        <if test="password != null">password=#{password},</if>
        <if test="email != null">email=#{email}</if>
    </set>
    where id=#{id}
</update>
```
    
- trim 加入指定的符号，删除多余的符号，比如逗号
    
```
<update id="updateAuthorIfNecessary">
    update Author
    <trim prefix="SET" suffixOverrides=",">
        <if test="username != null">username=#{username},</if>
        <if test="password != null">password=#{password},</if>
        <if test="email != null">email=#{email}</if>
    </trim>
    where id=#{id}
</update>
```
    

## 缓存
MyBatis 内置了一个强大的事务性查询缓存机制，它可以非常方便地配置和定制。 默认情况下，只启用了本地的会话缓存，它仅仅对一个会话中的数据进行缓存。 要启用全局的二级缓存，只需要在你的 SQL 映射文件中添加一行：`<cache/>`。
    
这个简单语句的效果如下:
1. 映射语句文件中的所有 select 语句的结果将会被缓存。
2. 映射语句文件中的所有 insert、update 和 delete 语句会刷新缓存。
3. 缓存会使用最近最少使用算法（LRU, Least Recently Used）算法来清除不需要的缓存。
4. 缓存不会定时进行刷新（也就是说，没有刷新间隔）。
5. 缓存会保存列表或对象（无论查询方法返回哪种）的 1024 个引用。
6. 缓存会被视为读/写缓存，这意味着获取到的对象并不是共享的，可以安全地被调用者修改，而不干扰其他调用者或线程所做的潜在修改。
    
缓存只作用于 cache 标签所在的映射文件中的语句。如果你混合使用 Java API 和 XML 映射文件，在共用接口中的语句将不会被默认缓存。你需要使用 @CacheNamespaceRef 注解指定缓存作用域。
    
- LRU – 最近最少使用：移除最长时间不被使用的对象。
- FIFO – 先进先出：按对象进入缓存的顺序来移除它们。
    
```
<cache
    eviction="FIFO"               - 默认的清除策略是 LRU
    flushInterval="60000"         - 刷新间隔
    size="512"                    - 引用数目
    readOnly="true"/>             - 只读的缓存会给所有调用者返回缓存对象的相同实例。 因此这些对象不能被修改。这就提供了可观的性能提升。
                                    - 而可读写的缓存会（通过序列化）返回缓存对象的拷贝。 速度上会慢一些，但是更安全，因此默认值是 false。
```
    
使用自定义缓存: type 属性指定的类必须实现 org.mybatis.cache.Cache 接口，且提供一个接受 String 参数作为 id 的构造器。
    
`<cache type="com.domain.something.MyCustomCache"/>`
    
