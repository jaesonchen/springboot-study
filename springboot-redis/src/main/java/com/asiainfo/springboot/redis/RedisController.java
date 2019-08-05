package com.asiainfo.springboot.redis;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年7月20日 下午1:22:45
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
@RestController
public class RedisController {
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    @Autowired
    private StringRedisTemplate stringTemplate;
    
    @RequestMapping("/set/{key}/{value}")
    public String set(@PathVariable("key") String key, @PathVariable("value") String value) {
        this.stringTemplate.opsForValue().set(key, value);
        return "success";
    }
    
    @RequestMapping("/get/{key}")
    public String get(@PathVariable("key") String key) {
        return this.stringTemplate.opsForValue().get(key);
    }
    
    @RequestMapping("/object/get/{id}")
    public Object getObject(@PathVariable("id") Long id) {
        return this.redisTemplate.opsForValue().get(String.valueOf(id));
    }
    
    @RequestMapping("/object/set/{id}")
    public Object setObject(@PathVariable("id") Long id) {
        this.redisTemplate.opsForValue().set(String.valueOf(id), new User(id, "chenzq", "1234"));
        return "success";
    }
    
    @RequestMapping("/hash/get/{id}")
    public Object getHash(@PathVariable("id") Long id) {
        return this.redisTemplate.opsForHash().get("users", String.valueOf(id));
    }
    
    @RequestMapping("/hash/set/{id}")
    public Object setHash(@PathVariable("id") Long id) {
        this.redisTemplate.opsForHash().put("users", String.valueOf(id), new User(id, "jaeson", "asdf"));;
        return "success";
    }
    
    @RequestMapping("/get")
    public List<Object> pipelineGet() {
        
        final RedisSerializer<String> stringSerializer = this.redisTemplate.getStringSerializer();
        final RedisSerializer<?> valueSerializer = this.redisTemplate.getValueSerializer();
        List<String> list = Arrays.asList("20001", "20002", "20003", "20004", "20005");
        return this.redisTemplate.executePipelined(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                for (String key : list) {
                    connection.get(stringSerializer.serialize(key));
                }
                return null;
            }
        }, valueSerializer);
    }
    
    @RequestMapping("/set")
    public List<Object> pipelineSet() {
        
        final RedisSerializer<String> stringSerializer = this.redisTemplate.getStringSerializer();
        @SuppressWarnings("unchecked")
        final RedisSerializer<Object> valueSerializer = (RedisSerializer<Object>) this.redisTemplate.getValueSerializer();
        Map<String, Object> map = new HashMap<>();
        map.put("20001", "chenzq1");
        map.put("20002", "chenzq2");
        map.put("20003", "chenzq3");
        map.put("20004", "chenzq4");
        map.put("20005", "chenzq5");
        return this.redisTemplate.executePipelined(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    byte[] rawKey = stringSerializer.serialize(entry.getKey());
                    byte[] rawValue = valueSerializer.serialize(entry.getValue());
                    connection.setEx(rawKey, 300, rawValue);
                }
                return null;
            }
        }, valueSerializer);
    }
}
