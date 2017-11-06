package com.asiainfo.springboot.jpa;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年10月20日  上午11:37:21
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */

@SpringBootApplication
@ComponentScan("com.asiainfo.springboot.jpa")
@RestController
public class JpaApplication {
	
	@Autowired
	UserService service;
	
	@RequestMapping("/user/{id}")
	User getUser(@PathVariable("id") long userId) {
		return this.service.findById(userId);
	}
	
	@RequestMapping("/user/name/{name}")
	List<User> getUserByName(@PathVariable("name") String userName) {
		return this.service.findByUserName(userName);
	}
	
	@RequestMapping("/user/name1/{name}")
	List<User> getUserByName1(@PathVariable("name") String userName) {
		return this.service.findUser(userName);
	}
	
	@RequestMapping("/user/name2/{name}")
	List<User> getUserByName2(@PathVariable("name") String userName) {
		return this.service.findUserNative(userName);
	}
	
	@RequestMapping("/user/save/{id}/{name}")
	String saveUser(@PathVariable("id") long userId, @PathVariable("name") String userName) {
		return String.valueOf(this.service.save(new User(userId, userName)));
	}
	
	@RequestMapping("/user/update/{id}/{name}")
	String updateUser(@PathVariable("id") long userId, @PathVariable("name") String userName) {
		return String.valueOf(this.service.save(new User(userId, userName)));
	}
	
	@RequestMapping("/user/update1/{id}/{name}")
	String updateUser1(@PathVariable("id") long userId, @PathVariable("name") String userName) {
		return String.valueOf(this.service.update(userId, userName));
	}
	
	@RequestMapping("/user/delete/{id}")
	String deleteUser(@PathVariable("id") long userId) {
		return this.service.delete(userId) ? "success" : "failure";
	}

	@RequestMapping("/user/page/{page}")
	List<User> page(@PathVariable("page") int page) {
		return this.service.findByPage(page);
	}
	
	@RequestMapping("/user/sort/{page}")
	List<User> sort(@PathVariable("page") int page) {
		return this.service.findByPage(page, Sort.Direction.ASC);
	}
	
	/** 
	 * @Description: TODO
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(new Object[] {JpaApplication.class});
		app.setAdditionalProfiles(new String[] {"jpa"});
		app.run(args);
	}
}
