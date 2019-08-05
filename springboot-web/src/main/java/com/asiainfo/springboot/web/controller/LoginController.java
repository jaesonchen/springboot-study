package com.asiainfo.springboot.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asiainfo.springboot.web.annotation.UserParam;
import com.asiainfo.springboot.web.interceptor.LoginInterceptor;
import com.asiainfo.springboot.web.model.User;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

/**   
 * @Description: HandlerInterceptor、@ExceptionHandler 测试
 * 
 * @author chenzq  
 * @date 2019年7月10日 下午3:28:50
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
@Api(value = "用户管理", tags = "用户管理模块")
@Controller
public class LoginController {

    @ApiIgnore
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("name", "Guest");
        return "index";
    }
    
    @ApiIgnore
    @GetMapping("/login")
    public String loginView() {
        return "login";
    }
    
    @ApiOperation(value = "登陆", notes = "用户登陆")
    @PostMapping("/login")
    public String login(@Valid User user, BindingResult bindingResult, 
            HttpServletRequest request, Model model) {
        if (bindingResult.hasErrors()) {
            for (FieldError error : bindingResult.getFieldErrors()) {
                model.addAttribute(error.getField(), error.getDefaultMessage());
            }
            return "login";
        }
        request.getSession().setAttribute(LoginInterceptor.SESSION_USER, user);
        model.addAttribute("name", user.getUsername());
        return "index";
    }
    
    @ApiOperation(value = "查找", notes = "查找用户")
    @ApiImplicitParam(name = "user_id", value = "用户id", required = true, dataType = "string", example = "1001", paramType = "query")
    @GetMapping("/user")
    @ResponseBody
    public User getById(@UserParam User user) {
        return user;
    }
}
