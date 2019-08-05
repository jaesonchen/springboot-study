package com.asiainfo.springboot.redis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * RedisAutoConfiguration / LettuceConnectionConfiguration, JedisConnectionConfiguration
 * 
 * spring1.5 默认使用的时Jedis
 * springboot2.0 开始默认使用lettuce，lettuce支持集群下的pipeline，Jedis下不支持pipeline需要自己扩展。
 * 
 * @author       zq
 * @date         2017年11月2日  上午10:15:29
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
@SpringBootApplication
public class Application {
	
	public static void main(String[] args) {
	    SpringApplication.run(Application.class, args);
	}
}
