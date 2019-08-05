package com.asiainfo.springboot.shiro.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresGuest;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.asiainfo.springboot.shiro.model.User;
import com.asiainfo.springboot.shiro.service.UserService;
import com.asiainfo.springboot.shiro.util.PasswordHelper;

import io.swagger.annotations.Api;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年7月20日 下午6:14:10
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
@Api(value = "用户管理", tags = "用户管理模块")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    
    @GetMapping("/index")
    public Object index() {
        Subject subject = SecurityUtils.getSubject();
        return subject.getPrincipal();
    }

    @GetMapping("/admin")
    public Object admin() {
        return "Welcome Admin";
    }

    // delete
    @GetMapping("/removable")
    public Object removable() {
        return "removable";
    }

    // insert & update
    @GetMapping("/renewable")
    public Object renewable() {
        return "renewable";
    }
    
    @RequiresGuest
    @GetMapping("/register")
    public Object register(@RequestParam String username, @RequestParam String password) {
        
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        PasswordHelper.encryptPassword(user);
        userService.saveUser(user);
        return user;
    }
    
    @GetMapping
    @RequiresAuthentication
    public Object userList() {
        Subject subject = SecurityUtils.getSubject();
        return subject.getPrincipal();
    }

    @PostMapping("/add")
    @RequiresPermissions(value = { "user:add", "user:edit" }, logical = Logical.OR)
    public Object userAdd() {
        return "user Add";
    }

    @DeleteMapping("/del")
    @RequiresRoles("admin")
    public Object userDel() {
        return "user Del";
    }
}
