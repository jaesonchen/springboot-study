package com.asiainfo.springboot.mybatis.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.asiainfo.springboot.mybatis.dao.RoleDao;
import com.asiainfo.springboot.mybatis.model.Role;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年7月16日 下午9:47:35
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
@Api(value = "角色管理", tags = "角色管理模块")
@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleDao roleDao;
    
    @ApiOperation(value = "列表", notes = "角色列表")
    @GetMapping
    public List<Role> findAll() {
        return roleDao.findAll();
    }
    
    @ApiOperation(value = "角色", notes = "获取角色")
    @GetMapping("/{id}")
    public Role findById(@PathVariable Long id) {
        return roleDao.findById(id);
    }
    
    @ApiOperation(value = "新增", notes = "新增角色")
    @PostMapping
    public Role save(Role role) {
        roleDao.save(role);
        return role;
    }
    
    @ApiOperation(value = "删除", notes = "删除角色")
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        roleDao.delete(id);
        return "success";
    }
}
