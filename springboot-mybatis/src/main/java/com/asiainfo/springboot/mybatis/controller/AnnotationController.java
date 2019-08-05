package com.asiainfo.springboot.mybatis.controller;

import java.util.ArrayList;
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

import com.asiainfo.springboot.mybatis.annotation.AnnotationUserDao;
import com.asiainfo.springboot.mybatis.model.Contact;
import com.asiainfo.springboot.mybatis.model.User;
import com.asiainfo.springboot.mybatis.service.AnnotationUserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年7月19日 下午3:47:02
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
@Api(value = "注解", tags = "注解模块")
@RestController
@RequestMapping("/annotation")
public class AnnotationController {

    @Autowired
    private AnnotationUserService userService;
    
    @Autowired
    private AnnotationUserDao userDao;
    
    @ApiOperation(value = "列表", notes = "用户列表")
    @GetMapping
    public List<User> findAll() {
        return userDao.findAll();
    }
    
    @ApiOperation(value = "用户", notes = "获取用户")
    @GetMapping("/{id}")
    public User findById(@PathVariable Long id) {
        return userDao.findById(id);
    }
    
    @ApiOperation(value = "用户", notes = "获取用户")
    @GetMapping("/byName/{name}")
    public List<User> findByName(@PathVariable String name) {
        return userService.findByUserName(name);
    }
    
    @ApiOperation(value = "新增用户", notes = "新增用户")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "userName", value = "用户名", required = true, dataType = "string", paramType = "query"), 
        @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "string", paramType = "query"), 
        @ApiImplicitParam(name = "deptId", value = "部门", required = true, dataType = "long", example = "0", paramType = "query")})
    @PostMapping
    public User save(User user, @RequestParam Long deptId) {
        user.setDept(userDao.findByDeptId(deptId));
        List<Contact> contacts = new ArrayList<>();
        contacts.add(new Contact("haidian", 1234));
        contacts.add(new Contact("chaoyang", 1111));
        user.setContacts(contacts);
        return userService.save(user);
    }
    
    @ApiOperation(value = "更新", notes = "更新用户")
    @PutMapping("/{id}")
    public String update(@PathVariable Long id, @RequestParam String userName, @RequestParam String password) {
        userService.update(id, userName, password);
        return "success";
    }
    
    @ApiOperation(value = "删除", notes = "删除用户")
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        userService.delete(id);
        return "success";
    }
}
