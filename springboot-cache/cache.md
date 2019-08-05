# 常用注解

## @EnableCaching
开启缓存注解。
    
```
@Configuration
@EnableCaching
public class RedisCacheConfig {

    @Bean
    public RedisCacheConfiguration redisCacheConfiguration() {
        return RedisCacheConfiguration
                .defaultCacheConfig()
                .serializeKeysWith(
                        RedisSerializationContext
                                .SerializationPair
                                .fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(
                        RedisSerializationContext
                                .SerializationPair
                                .fromSerializer(new GenericJackson2JsonRedisSerializer()))
                .entryTtl(Duration.ofMinutes(30))   // 缓存过期时间30m
                .disableCachingNullValues();        // 不缓存null结果，配合@Cacheable和@CachePut 的 unless = "#result == null"使用
    }
}
```
    

## 自定义缓存key生成器

```
    @Bean
    public KeyGenerator keyGenerator() {
        return new KeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... params) {
                StringBuilder sb = new StringBuilder();
                sb.append(target.getClass().getName());
                sb.append("::").append(method.getName());
                for (Object obj : params) {
                    sb.append("::").append(obj.toString());
                }
                return sb.toString();
            }
        };
    }
```
    
引用keyGenerator: 
    
```
    @Cacheable(keyGenerator = "keyGenerator", unless = "#result == null")
    @Override
    public User findById(Long id) {
        logger.info("findById execute!, id = {}", id);
        return map.get(id);
    }
```
    

## @Cacheable 设置缓存
用于查询操作。如果有缓存时，不会执行方法而是直接返回缓存中的值；如果没有缓存时，则会执行方法并将返回值缓存（`unless` 参数可以配置返回为null时不缓存）。
    
```
    @Cacheable(key = "'user'.concat(#id.toString())", unless = "#result == null")
    @Override
    public User findById(Long id) {
        logger.info("findById execute!, id = {}", id);
        return map.get(id);
    }
```
    

## @CachePut
执行方法并将结果缓存起来，更新和插入操作。
    
```
    @CachePut(key = "'user'.concat(#id.toString())", unless = "#result == null")
    @Override
    public User update(Long id, String password) {
        logger.info("update execute!, id = {}, password = {}", id, password);
        if (map.containsKey(id)) {
            map.get(id).setPassword(password);
        }
        return map.get(id);
    }
```
    

## @CacheEvict
根据key删除缓存中的数据。
- `allEntries=true` 移除所有缓存项
- `beforeInvocation=true` 在方法执行之前先删除缓存项
    
```
    @CacheEvict(key = "'user'.concat(#id.toString())", beforeInvocation = true)
    @Override
    public User delete(Long id) {
        logger.info("delete execute!, id = {}", id);
        return map.remove(id);
    }
```
    

## @CacheConfig
该注解是可以将缓存分类，它是类级别的注解方式，指定使用的缓存（相当于key的`prefix`），可以简化注解的配置。不配置该项，则每个注解都要配置value。
    
```
@Service
@CacheConfig(cacheNames = "userCache")
public class UserServiceImpl implements UserService {
```
    

## @Caching
这个注解将其他注解方式融合在一起了，我们可以根据需求来自定义注解，并将前面三个注解应用在一起。
    
```
    @Cacheable(key = "'findAll'", unless = "#result == null")
    @Override
    public List<User> findAll() {
        logger.info("findAll execute!");
        return new ArrayList<>(map.values());
    }
    
    @CacheEvict(key = "'findAll'", beforeInvocation = true)
    @Override
    public User save(User user) {
        logger.info("save execute!, user = {}", user);
        user.setId(index.incrementAndGet());
        map.put(user.getId(), user);
        return user;
    }
    
    @Caching(
            put = { @CachePut(key = "'user' + #p0.toString()", unless = "#result == null") }, 
            evict = { @CacheEvict(key = "'findAll'", beforeInvocation = true) })
    @Override
    public User update(Long id, String password) {
        logger.info("update execute!, id = {}, password = {}", id, password);
        if (map.containsKey(id)) {
            map.get(id).setPassword(password);
        }
        return map.get(id);
    }

    @Caching(
            evict = {
                    @CacheEvict(key = "'findAll'", beforeInvocation = true), 
                    @CacheEvict(key = "'user' + #id.toString()", beforeInvocation = true)})
    @Override
    public User delete(Long id) {
        logger.info("delete execute!, id = {}", id);
        return map.remove(id);
    }
```
    

