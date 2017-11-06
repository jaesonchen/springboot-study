package com.asiainfo.springboot.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @EnableAutoConfiguration只应该设置一个，建议添加到主@Configuration 类上。
 * exclude={DataSourceAutoConfiguration.class} 用于disable 某个@Configuration类。
 * @ComponentScan 不指定扫描范围时，以当前类所在的包为base package
 * 
 * 
 * @SpringBootApplication 等价于 @Configuration、 @EnableAutoConfiguration、 @ComponentScan
 * 
 * @author       zq
 * @date         2017年10月17日  下午4:10:06
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */

@RestController
@Configuration
@EnableAutoConfiguration
@ComponentScan("com.asiainfo.springboot.application")
public class Application {
	
	@RequestMapping("/")
	String home() {
		return "Hello World!";
	}
	
	public static void main(String[] args) throws Exception {
		SpringApplication.run(Application.class, args);
	}
}
