package com.asiainfo.springboot.initializer;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

import com.asiainfo.springboot.application.Application;

/**
 * 
 * @Description: springboot提供的web程序初始化的入口
 * 
 * @author       zq
 * @date         2017年10月22日  下午8:59:24
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class ServletInitializer extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Application.class);
	}
}
