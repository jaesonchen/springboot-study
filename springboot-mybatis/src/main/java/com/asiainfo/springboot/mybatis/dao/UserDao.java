package com.asiainfo.springboot.mybatis.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asiainfo.springboot.mybatis.model.Contact;
import com.asiainfo.springboot.mybatis.model.User;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年7月16日 下午6:17:31
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
@Mapper
public interface UserDao {

    public List<User> findAll();
    
    public User findById(Long id);
    
    public List<User> findByUserName(String userName);
    
    public int save(User user);
    
    public int addUserRole(User user);
    
    public int addContact(List<Contact> list);
     
    public int update(User user);
    
    public int delete(Long id);
    
    public int deleteUserRole(Long id);
    
    public int deleteContact(Long id);
}
