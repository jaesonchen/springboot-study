package com.asiainfo.springboot.profile;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Profile 可以用来修饰 @Component 和 @Configuration
 * 
 * spring.profiles.active
 * 1. 配置在application.properties(application.yml)中：  spring.profiles.active=dev,test
 * 2. springboot 启动命令行指定： --spring.profiles.active=dev,test
 * 3. 在SpringApplication.run之前，手动设置profile
 * 
 * @author       zq
 * @date         2017年10月18日  下午4:58:52
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */

@RestController
@Configuration
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@ComponentScan("com.asiainfo.springboot.profile")
@Profile({"dev", "release"})
public class ProfileApplication {

	@Value("${name}")
	String name;
	
	@RequestMapping("/")
	String home() {
		return "Hello " + name;
	}
	
	/** 
	 * @Description: TODO
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		SpringApplication app = new SpringApplication(new Object[] {ProfileApplication.class});
		app.setAdditionalProfiles(new String[] {"release"});
		app.run(args);
	}
}
