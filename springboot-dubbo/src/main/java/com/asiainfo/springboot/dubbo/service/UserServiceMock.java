package com.asiainfo.springboot.dubbo.service;

import java.util.List;

import com.asiainfo.springboot.dubbo.model.User;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年7月14日 下午12:18:42
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
public class UserServiceMock implements UserService {

    @Override
    public User getById(Long id) {
        return null;
    }

    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public boolean check() {
        return false;
    }
}
