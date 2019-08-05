package com.asiainfo.springboot.mybatis.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.asiainfo.springboot.mybatis.dao.DepartmentDao;
import com.asiainfo.springboot.mybatis.model.Department;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年7月16日 下午9:47:04
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
@Api(value = "部门管理", tags = "部门管理模块")
@RestController
@RequestMapping("/dept")
public class DeptController {

    @Autowired
    private DepartmentDao deptDao;
    
    @ApiOperation(value = "列表", notes = "部门列表")
    @GetMapping
    public List<Department> findAll() {
        return deptDao.findAll();
    }
    
    @ApiOperation(value = "部门", notes = "获取部门")
    @GetMapping("/{id}")
    public Department findById(@PathVariable Long id) {
        return deptDao.findById(id);
    }
    
    @ApiOperation(value = "新增", notes = "新增部门")
    @PostMapping
    public Department save(Department dept) {
        deptDao.save(dept);
        return dept;
    }
    
    @ApiOperation(value = "删除", notes = "删除部门")
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        deptDao.delete(id);
        return "success";
    }
}
