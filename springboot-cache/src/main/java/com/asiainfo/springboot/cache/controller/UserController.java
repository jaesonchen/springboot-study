package com.asiainfo.springboot.cache.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.asiainfo.springboot.cache.model.User;
import com.asiainfo.springboot.cache.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年7月19日 下午9:46:19
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
@Api(value = "用户管理", tags = "用户管理模块")
@RequestMapping("/users")
@RestController
public class UserController {
    
    @Autowired
    private UserService service;

    @ApiOperation(value = "用户列表", notes = "返回用户列表")
    @GetMapping
    public List<User> findAll() {
        return service.findAll();
    }
    
    @ApiOperation(value = "新增", notes = "新增用户")
    @PostMapping
    public User save(@RequestParam String userName, @RequestParam String password) {
        User user = new User(userName, password);
        service.save(user);
        return user;
    }
    
    @ApiOperation(value = "读取", notes = "返回用户信息")
    @GetMapping("/{id}")
    public User getById(@PathVariable Long id) {
        return service.findById(id);
    }
    
    @ApiOperation(value = "更新", notes = "更新用户信息")
    @PutMapping("/{id}")
    public User update(@PathVariable Long id, @RequestParam String password) {
        return service.update(id, password);
    }
    
    @ApiOperation(value = "删除", notes = "删除用户信息")
    @DeleteMapping("/{id}")
    public User delete(@PathVariable Long id) {
        return service.delete(id);
    }
}
