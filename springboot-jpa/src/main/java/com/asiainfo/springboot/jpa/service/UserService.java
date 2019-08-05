package com.asiainfo.springboot.jpa.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.asiainfo.springboot.jpa.model.QueryParam;
import com.asiainfo.springboot.jpa.model.User;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年7月12日 下午1:07:48
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
public interface UserService {
    
    public List<User> findByPassword(String password);

    public List<User> findAll();
    
    public User findById(Long id);
    
    public User findByUsername(String username);
    
    public User save(User user);
    
    public void delete(Long id);
    
    public Page<User> findAll(Integer page, Integer size);
    
    public Page<User> findAll(Integer page, Integer size, QueryParam param);
}
