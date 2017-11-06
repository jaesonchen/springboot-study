package com.asiainfo.springboot.properties;

import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

/**
 * 1.
	environments:
	    dev:
	        url: http://dev.bar.com
	        name: Developer Setup
	    prod:
	        url: http://foo.bar.com
	        name: My Cool App
    -->
	environments.dev.url=http://dev.bar.com
	environments.dev.name=Developer Setup
	environments.prod.url=http://foo.bar.com
	environments.prod.name=My Cool App
 * 
 * 2.
	my:
		servers:
	    	- dev.bar.com
	    	- foo.bar.com
	-->    	
	my.servers[0]=dev.bar.com
	my.servers[1]=foo.bar.com
 * 
 * @author       zq
 * @date         2017年10月19日  下午4:59:32
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
@ConfigurationProperties(prefix="validator")
@Validated
@Component
public class PropertiesValidator {

	@NotNull
	private String name;
	
	@NotNull
	private String address;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
}
