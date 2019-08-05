package com.asiainfo.springboot.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年7月20日 下午3:02:22
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
@Api(value = "sesson管理", tags = "sesson管理模块")
@RestController
@RequestMapping("/session")
public class SessionCrontroller {

    final Logger logger = LoggerFactory.getLogger(getClass());
    
    @ApiOperation(value = "登陆", notes = "写入session")
    @GetMapping("/login")
    public String login(@RequestParam String userName, @RequestParam String password, HttpServletRequest request) {
        HttpSession session = request.getSession();
        logger.info("session implement = {}", session.getClass());
        logger.info("sessionId = {}", session.getId());
        User user = new User(1001L, userName, password);
        session.setAttribute("user", user);
        return "success";
    }
    
    @ApiOperation(value = "登出", notes = "删除session")
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        logger.info("session implement = {}", session.getClass());
        logger.info("sessionId = {}", session.getId());
        session.removeAttribute("user");
        return "success";
    }
    
    @ApiOperation(value = "访问", notes = "访问session属性")
    @GetMapping("/home")
    public Object home(HttpServletRequest request) {
        HttpSession session = request.getSession();
        logger.info("session implement = {}", session.getClass());
        logger.info("sessionId = {}", session.getId());
        return session.getAttribute("user");
    }
}
