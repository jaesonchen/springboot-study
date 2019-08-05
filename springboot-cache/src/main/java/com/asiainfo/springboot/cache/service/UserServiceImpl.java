package com.asiainfo.springboot.cache.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import com.asiainfo.springboot.cache.model.User;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年7月19日 下午9:24:37
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
@Service
@CacheConfig(cacheNames = "userCache")
public class UserServiceImpl implements UserService {

    final Logger logger = LoggerFactory.getLogger(getClass());
    
    private Map<Long, User> map = new ConcurrentHashMap<>();
    private AtomicLong index = new AtomicLong();
    
    @Cacheable(key = "'findAll'", unless = "#result == null")
    @Override
    public List<User> findAll() {
        logger.info("findAll execute!");
        return new ArrayList<>(map.values());
    }

    // @Cacheable(keyGenerator = "keyGenerator", unless = "#result == null")
    @Cacheable(key = "'user'.concat(#id.toString())", unless = "#result == null")
    @Override
    public User findById(Long id) {
        logger.info("findById execute!, id = {}", id);
        return map.get(id);
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
                    @CacheEvict(key = "'user'+ #id.toString()", beforeInvocation = true)})
    @Override
    public User delete(Long id) {
        logger.info("delete execute!, id = {}", id);
        return map.remove(id);
    }
}
