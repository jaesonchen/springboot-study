package com.asiainfo.springboot.mvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年10月22日  下午9:07:10
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */

@Controller
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@ComponentScan("com.asiainfo.springboot.mvc")
public class MvcApplication {

	@RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index() {
        return "index";
    }
	
	/** 
	 * @Description: TODO
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(new Object[] {MvcApplication.class});
		app.setAdditionalProfiles(new String[] {"mvc"});
		app.run(args);
	}
}
