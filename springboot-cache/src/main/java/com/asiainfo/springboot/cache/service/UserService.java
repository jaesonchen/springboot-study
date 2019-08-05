package com.asiainfo.springboot.cache.service;

import java.util.List;

import com.asiainfo.springboot.cache.model.User;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年7月19日 下午9:22:11
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
public interface UserService {

    public List<User> findAll();
    
    public User findById(Long id);
    
    public User save(User user);
    
    public User update(Long id, String password);
    
    public User delete(Long id);
}
