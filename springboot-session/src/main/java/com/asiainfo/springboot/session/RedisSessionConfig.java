package com.asiainfo.springboot.session;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * @Description: 自定义spring-session序列化方式
 * 
 * @author chenzq  
 * @date 2019年7月20日 下午1:24:07
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved.
 */
@Configuration
public class RedisSessionConfig {
	
    @Bean("springSessionDefaultRedisSerializer")
    public RedisSerializer<Object> defaultRedisSerializer() {
        return new GenericJackson2JsonRedisSerializer();
    }
}
