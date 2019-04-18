package com.asiainfo.springboot.servlet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * servlet 配置
 * 
 * @author       zq
 * @date         2017年10月22日  下午5:34:48
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@ComponentScan("com.asiainfo.springboot.servlet")
public class ServletApplication {

	/** 
	 * @Description: TODO
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(ServletApplication.class, args);
	}
}
