package com.asiainfo.springboot.mybatis.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asiainfo.springboot.mybatis.model.Role;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年7月16日 下午8:56:58
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
@Mapper
public interface RoleDao {
    
    public List<Role> findAll();

    public Role findById(Long id);
    
    public List<Role> findByIds(Long[] array);
    
    public int save(Role role);
    
    public int update(Role role);
    
    public int delete(Long id);
}
