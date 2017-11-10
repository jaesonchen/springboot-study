package com.asiainfo.springboot.mybatis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO
 * 
 * @author       zq
 * @date         2017年11月10日  上午10:58:23
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
@SpringBootApplication
@ComponentScan("com.asiainfo.springboot.mybatis")
@RestController
public class MybatisApplication {

    @Autowired
    UserDao dao;
    
    @RequestMapping("/user/{name}")
    User getUser(@PathVariable("name") String userName) {
        return this.dao.findByName(userName);
    }
    
    /** 
     * TODO
     * 
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(new Object[] {MybatisApplication.class});
        app.setAdditionalProfiles(new String[] {"db"});
        app.run(args);
    }
}
