package com.asiainfo.springboot.interceptor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.asiainfo.springboot.interceptor")
public class SessionApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(new Object[] {SessionApplication.class});
		app.setAdditionalProfiles(new String[] {"mvc"});
		app.run(args);
	}
}
