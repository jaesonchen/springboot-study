package com.asiainfo.springboot.jdbc.service;

import java.util.List;

import com.asiainfo.springboot.jdbc.model.User;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年7月12日 下午1:07:48
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
public interface UserService {

    public List<User> findAll();
    
    public User getById(Long id);
    
    public User save(User user);
    
    public void delete(Long id); 
}
