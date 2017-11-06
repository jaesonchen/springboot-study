package com.asiainfo.springboot.schedule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * TODO
 * 
 * @author       zq
 * @date         2017年11月6日  下午3:34:29
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
@SpringBootApplication
@ComponentScan("com.asiainfo.springboot.schedule")
@EnableScheduling
public class ScheduleApplication {

	/** 
	 * TODO
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(ScheduleApplication.class, args);
	}
}
