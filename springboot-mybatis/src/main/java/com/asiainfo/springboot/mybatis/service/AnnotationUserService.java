package com.asiainfo.springboot.mybatis.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asiainfo.springboot.mybatis.annotation.AnnotationUserDao;
import com.asiainfo.springboot.mybatis.model.Contact;
import com.asiainfo.springboot.mybatis.model.User;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年7月19日 下午3:47:22
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
@Service
@Transactional
public class AnnotationUserService {

    @Autowired
    private AnnotationUserDao userDao;
    
    public List<User> findByUserName(String userName) {
        return this.userDao.findByUserName(userName);
    }
    
    public User save(User user) {
        // 保存user
        userDao.save(user);
        // 保存contact
        for (Contact contact : user.getContacts()) {
            contact.setUserId(user.getId());
        }
        userDao.batchSave(user.getContacts());
        return user;
    }
    
    public int update(long id, String userName, String password) {
        return userDao.update(id, userName, password);
    }
    
    public int delete(long id) {
        userDao.deleteContact(id);
        userDao.deleteUserRole(id);
        return userDao.delete(id);
    }
}
