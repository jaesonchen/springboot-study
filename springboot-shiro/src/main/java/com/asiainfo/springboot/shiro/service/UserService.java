package com.asiainfo.springboot.shiro.service;

import com.asiainfo.springboot.shiro.model.User;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年7月20日 下午5:49:44
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
public interface UserService {

    public User findUserByName(String username);
    
    public void saveUser(User user);
}
