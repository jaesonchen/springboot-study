package com.asiainfo.springboot.jpa.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.asiainfo.springboot.jpa.model.QueryParam;
import com.asiainfo.springboot.jpa.model.User;
import com.asiainfo.springboot.jpa.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年7月12日 下午5:19:08
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved.
 */
@Api(value = "用户管理", tags = "用户管理模块")
@RestController
@RequestMapping("/users")
public class UserController {
    
    @Autowired
    private UserService service;
    
    @ApiOperation(value = "列表", notes = "用户列表")
    @GetMapping
    public List<User> findAll() {
        return service.findAll();
    }
    
    @ApiOperation(value = "新增", notes = "新增用户")
    @PostMapping
    public User save(User user) {
        return service.save(user);
    }
    
    @ApiOperation(value = "查询", notes = "查询用户")
    @GetMapping("/{id}")
    public User findById(@PathVariable long id) {
        return service.findById(id);
    }
    
    @ApiOperation(value = "删除", notes = "删除用户")
    @DeleteMapping("/{id}")
    public String delete(@PathVariable long id) {
        service.delete(id);
        return "success";
    }
    
    @ApiOperation(value = "查询2", notes = "用户名查询")
    @GetMapping("/byname")
    public User findByUsername(@RequestParam String username) {
        return service.findByUsername(username);
    }
    
    @ApiOperation(value = "列表", notes = "用户列表")
    @GetMapping("/page")
    public Page<User> findAll(@RequestParam int page, @RequestParam int size) {
        return service.findAll(page, size);
    }
    
    @ApiOperation(value = "列表", notes = "用户列表")
    @PostMapping("/page")
    public Page<User> findAll(@RequestParam int page, @RequestParam int size, QueryParam param) {
        return service.findAll(page, size, param);
    }
}
