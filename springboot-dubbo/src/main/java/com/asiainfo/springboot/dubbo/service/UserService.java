package com.asiainfo.springboot.dubbo.service;

import java.util.List;

import com.asiainfo.springboot.dubbo.model.User;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年7月14日 上午10:04:35
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
public interface UserService {

    User getById(Long id);
    
    List<User> findAll();
    
    boolean check();
}
