package com.asiainfo.springboot.security.service;

import java.util.List;

import com.asiainfo.springboot.security.model.Role;
import com.asiainfo.springboot.security.model.User;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年7月29日 下午12:37:46
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
public interface UserService {

    User findById(Long id);
    
    List<User> findAll();
    
    void save(User user);
    
    List<Role> findByRoleIds(List<Long> list);
}
