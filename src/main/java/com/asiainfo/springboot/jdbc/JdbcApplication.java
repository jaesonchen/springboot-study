package com.asiainfo.springboot.jdbc;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * spring data-source支持
 * 
 * @author       zq
 * @date         2017年10月20日  下午3:56:46
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
@SpringBootApplication(exclude={HibernateJpaAutoConfiguration.class, JpaRepositoriesAutoConfiguration.class})
@ComponentScan("com.asiainfo.springboot.jdbc")
@RestController
public class JdbcApplication {

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	@Qualifier("secondaryJdbcTemplate")
	JdbcTemplate secondaryJdbcTemplate;
	
	@RequestMapping("/user/{id}")
	User getUser(@PathVariable("id") long userId) {
		List<User> list = this.jdbcTemplate.query("select user_id, user_name from mcd_user where user_id=?", 
				new Object[] {userId}, new BeanPropertyRowMapper<User>(User.class));
		return null == list || list.isEmpty() ? new User() : list.get(0);
	}
	
	@RequestMapping("/user1/{id}")
	User getUser1(@PathVariable("id") long userId) {
		List<User> list = this.secondaryJdbcTemplate.query("select user_id, user_name from mcd_user where user_id=?", 
				new Object[] {userId}, new BeanPropertyRowMapper<User>(User.class));
		return null == list || list.isEmpty() ? new User() : list.get(0);
	}
	
	/** 
	 * @Description: TODO
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(new Object[] {JdbcApplication.class});
		app.setAdditionalProfiles(new String[] {"db"});
		app.run(args);
	}
}
