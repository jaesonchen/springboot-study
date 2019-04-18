package com.asiainfo.springboot.interceptor;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@ComponentScan("com.asiainfo.springboot.interceptor")
@Controller
public class InterceptorApplication {

    final Logger logger = LoggerFactory.getLogger(getClass());
    public static final String SESSION_USER = "_session_user";
    
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index() {
        return "index";
    }
    
    @RequestMapping(value = "/login_view", method = RequestMethod.GET)
    public String loginView() {
        return "login";
    }
    
    @RequestMapping(value = "/login")
    @ResponseBody
    public String login(LoginParam param, HttpServletRequest request) {

        logger.info("login: {}", param);
        request.getSession().setAttribute(SESSION_USER, param);
        return "登录成功";
    }
    
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(new Object[] {InterceptorApplication.class});
		app.setAdditionalProfiles(new String[] {"mvc"});
		app.run(args);
	}
}
