package com.asiainfo.springboot.properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * application.properties查找顺序：
 * 	启动类所在目录的/config目录
 * 	启动类所在目录
 * 	classpath中的/config目录
 *  classpath 根目录
 * 
 * properties文件覆盖优先级(高->低)：
 * 	application-{profile}.properties(outside jar)
 * 	application-{profile}.properties(inside jar)
 *  application-defalut.properties
 * 	application.properties(outside jar)
 * 	application.properties(inside jar)
 * @Configuration
 * 
 * @author       zq
 * @date         2017年10月18日  下午5:15:20
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
@RestController
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@ComponentScan("com.asiainfo.springboot.properties")
public class PropertiesApplication {
	
	@Autowired
	PropertiesBean properties;
	
	@Autowired
	PropertiesValidator validator;

	@RequestMapping("/properties")
	String properties() {
		return properties.getName();
	}
	
	@RequestMapping("/validator")
	String validator() {
		return validator.getAddress();
	}
	
	/** 
	 * @Description: TODO
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(PropertiesApplication.class, args);
	}
}
