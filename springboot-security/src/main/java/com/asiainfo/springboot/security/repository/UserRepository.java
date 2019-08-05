package com.asiainfo.springboot.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.asiainfo.springboot.security.model.User;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年7月20日 下午5:50:19
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    public User findByUsername(String username);
}
