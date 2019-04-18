package com.asiainfo.springboot.json;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO
 * 
 * @author       zq
 * @date         2017年10月29日  下午2:39:21
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
@RestController
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@ComponentScan("com.asiainfo.springboot.json")
public class JsonApplication {

	@RequestMapping("/map")
	Map<String, Object> map() {

		Map<String, Object> result = new HashMap<>();
		result.put("name", "chenzq");
		result.put("age", 22);
		result.put("manager", true);
		result.put("list", null);
		return result;
	}
	
	@RequestMapping("/user")
	User user() {
		User user = new User();
		return user;
	}
	
	/** 
	 * TODO
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(JsonApplication.class, args);
	}
}
class User implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;
	
	private String name;
	private int age;
	private boolean mananger;
	private List<String> address;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public boolean isMananger() {
		return mananger;
	}
	public void setMananger(boolean mananger) {
		this.mananger = mananger;
	}
	public List<String> getAddress() {
		return address;
	}
	public void setAddress(List<String> address) {
		this.address = address;
	}
}
