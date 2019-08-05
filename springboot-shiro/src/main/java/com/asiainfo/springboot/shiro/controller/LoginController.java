package com.asiainfo.springboot.shiro.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年7月20日 下午6:14:00
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
@Api(value = "用户登陆", tags = "用户登陆模块")
@RestController
@RequestMapping
public class LoginController {
    
    @GetMapping("/403")
    public Object forbidden() {
        return "Here is 403 page";
    }
    
    @GetMapping("/login")
    public String login() {
        return "Here is login page";
    }
    
    @PostMapping("/login")
    public Object doLogin(@RequestParam String username, @RequestParam String password, @RequestParam Boolean rememberMe) {
        
        UsernamePasswordToken token = new UsernamePasswordToken(username, password, rememberMe);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
        } catch (IncorrectCredentialsException ice) {
            return "password error!";
        } catch (UnknownAccountException uae) {
            return "username error!";
        } catch (LockedAccountException lae) {
            return "user locked!";
        } catch (AuthenticationException ae) {
            return "Authentication failure!";
        }
        return "SUCCESS";
    }
}
