package com.asiainfo.springboot.security.controller;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.asiainfo.springboot.security.model.User;
import com.asiainfo.springboot.security.service.UserService;

import io.swagger.annotations.Api;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年7月28日 下午10:01:22
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
@Api(value = "用户管理", tags = "用户管理模块")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private PasswordEncoder encoder;
    
    @GetMapping("/register")
    public Object register(@RequestParam String username, @RequestParam String password, @RequestParam Long[] role) {
        
        User user = new User(username, encoder.encode(password));
        user.setRoles(userService.findByRoleIds(Arrays.asList(role)));
        userService.save(user);
        return user;
    }
    
    @GetMapping("/list")
    public Object list() {
        return this.userService.findAll();
    }
    
    @GetMapping("/{id}")
    public Object findById(@PathVariable Long id) {
        return this.userService.findById(id);
    }
}
