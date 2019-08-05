package com.asiainfo.springboot.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * springboot redis 自动配置，如果不指定key/value序列化方式，则可以不用任何配置，springboot自动注入RedisTemplate<Object, Object> 和 StringRedisTemplate
 * 
 * @author       zq
 * @date         2017年11月1日  下午5:37:36
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
@Configuration
public class RedisConfig {

	final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Bean
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
		
	    RedisTemplate<String, Object> template = new RedisTemplate<>();
	    template.setConnectionFactory(factory);
	    RedisSerializer<?> stringSerializer = new StringRedisSerializer();
	    RedisSerializer<?> valueSerializer = new GenericJackson2JsonRedisSerializer();
	    template.setKeySerializer(stringSerializer);
	    template.setValueSerializer(valueSerializer);
	    template.setHashKeySerializer(stringSerializer);
	    template.setHashValueSerializer(valueSerializer);
	    template.afterPropertiesSet();
	    return template;
	}
}
