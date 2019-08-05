package com.asiainfo.springboot.mybatis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asiainfo.springboot.mybatis.dao.UserDao;
import com.asiainfo.springboot.mybatis.model.Contact;
import com.asiainfo.springboot.mybatis.model.User;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年7月16日 下午10:38:05
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
@Service
@Transactional
public class UserService {

    @Autowired
    private UserDao userDao;
    
    public User save(User user) {
        // 保存user
        userDao.save(user);
        // 保存contact
        for (Contact contact : user.getContacts()) {
            contact.setUserId(user.getId());
        }
        userDao.addContact(user.getContacts());
        // 保存user_role
        userDao.addUserRole(user);
        return user;
    }
    
    public void delete(long id) {
        userDao.deleteContact(id);
        userDao.deleteUserRole(id);
        userDao.delete(id);
    }
}
