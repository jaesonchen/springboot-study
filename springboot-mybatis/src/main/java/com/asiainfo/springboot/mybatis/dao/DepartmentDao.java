package com.asiainfo.springboot.mybatis.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asiainfo.springboot.mybatis.model.Department;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年7月16日 下午8:56:49
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
@Mapper
public interface DepartmentDao {

    public List<Department> findAll();
    
    public Department findById(Long id);
    
    public int save(Department dept);
    
    public int update(Department dept);
    
    public int delete(Long id);
}
