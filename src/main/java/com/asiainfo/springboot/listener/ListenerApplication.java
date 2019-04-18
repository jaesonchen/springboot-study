package com.asiainfo.springboot.listener;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.ComponentScan;

/**
 * SpringApplication 添加事件监听
 * 
 * @author       zq
 * @date         2017年10月19日  下午4:17:34
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@ComponentScan("com.asiainfo.springboot.listener")
public class ListenerApplication {

	/** 
	 * @Description: TODO
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		SpringApplication app = new SpringApplication(new Object[] {ListenerApplication.class});
		//监听器
		app.addListeners(new ApplicationListener<ApplicationReadyEvent>() {

			@Override
			public void onApplicationEvent(ApplicationReadyEvent event) {
				System.out.println("Application Ready!");
			}
		});
		//不启用web context，非web在run之后会直接退出
		app.setWebEnvironment(false);
		app.run(args);
	}
}
