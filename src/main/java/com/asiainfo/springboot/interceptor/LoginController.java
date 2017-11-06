package com.asiainfo.springboot.interceptor;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年10月22日  下午10:42:46
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
@RestController
public class LoginController {

    @RequestMapping(value = "/login")
    public String login(LoginParam param, HttpServletRequest request) {

    	System.out.println(param);
        //将用户写入session
        request.getSession().setAttribute("_session_user", param);
        return "登录成功";
    }
}
