package com.asiainfo.springboot.jdbc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.asiainfo.springboot.jdbc.model.User;
import com.asiainfo.springboot.jdbc.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年7月12日 下午2:24:16
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
    public User getByid(@PathVariable long id) {
        return service.getById(id);
    }
    
    @ApiOperation(value = "删除", notes = "删除用户")
    @DeleteMapping("/{id}")
    public String delete(@PathVariable long id) {
        service.delete(id);
        return "success";
    }
}
