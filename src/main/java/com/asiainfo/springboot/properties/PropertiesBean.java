package com.asiainfo.springboot.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年10月19日  下午4:33:20
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
@Component
public class PropertiesBean {

	@Value("${name}")
    private String name;

	public String getName() {
		return name;
	}
}
