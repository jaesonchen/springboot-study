package com.asiainfo.springboot.security.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asiainfo.springboot.security.model.Role;
import com.asiainfo.springboot.security.model.User;
import com.asiainfo.springboot.security.repository.RoleRepository;
import com.asiainfo.springboot.security.repository.UserRepository;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年7月29日 下午12:38:05
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public List<Role> findByRoleIds(List<Long> list) {
        return roleRepository.findAllById(list);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).get();
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }
}
