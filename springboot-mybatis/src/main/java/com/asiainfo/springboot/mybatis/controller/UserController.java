package com.asiainfo.springboot.mybatis.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.asiainfo.springboot.mybatis.dao.DepartmentDao;
import com.asiainfo.springboot.mybatis.dao.RoleDao;
import com.asiainfo.springboot.mybatis.dao.UserDao;
import com.asiainfo.springboot.mybatis.model.Contact;
import com.asiainfo.springboot.mybatis.model.User;
import com.asiainfo.springboot.mybatis.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年7月16日 下午7:04:32
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
@Api(value = "用户管理", tags = "用户管理模块")
@RestController
@RequestMapping("/users")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private UserDao userDao;
    
    @Autowired
    private DepartmentDao deptDao;
    
    @Autowired
    private RoleDao roleDao;

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
    
    @ApiOperation(value = "新增用户", notes = "新增用户")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "userName", value = "用户名", required = true, dataType = "string", paramType = "query"), 
        @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "string", paramType = "query"), 
        @ApiImplicitParam(name = "deptId", value = "部门", required = true, dataType = "long", example = "0", paramType = "query"), 
        @ApiImplicitParam(name = "role", value = "角色", required = true, allowMultiple = true, dataType = "long", paramType = "query")})
    @PostMapping
    public User save(User user, @RequestParam Long deptId, @RequestParam Long[] role) {
        user.setDept(deptDao.findById(deptId));
        user.setRoles(roleDao.findByIds(role));
        List<Contact> contacts = new ArrayList<>();
        contacts.add(new Contact("haidian", 1234));
        contacts.add(new Contact("changping", 1111));
        user.setContacts(contacts);
        return userService.save(user);
    }
    
    @ApiOperation(value = "删除", notes = "删除用户")
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        userService.delete(id);
        return "success";
    }
}
