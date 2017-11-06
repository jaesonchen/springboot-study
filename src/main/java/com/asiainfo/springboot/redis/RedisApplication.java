package com.asiainfo.springboot.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO
 * 
 * @author       zq
 * @date         2017年11月2日  上午10:15:29
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
@SpringBootApplication
@ComponentScan("com.asiainfo.springboot.redis")
@RestController
public class RedisApplication {

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	@RequestMapping("/set/{key}/{value}")
	Object set(@PathVariable("key") String key, @PathVariable("value") String value) {
		this.redisTemplate.opsForValue().set(key, value);
		return "success";
	}
	
	@RequestMapping("/get/{key}")
	Object get(@PathVariable("key") String key) {
		return this.redisTemplate.opsForValue().get(key);
	}
	
	/** 
	 * TODO
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(new Object[] {RedisApplication.class});
		app.setAdditionalProfiles(new String[] {"redis"});
		app.run(args);
	}
}
