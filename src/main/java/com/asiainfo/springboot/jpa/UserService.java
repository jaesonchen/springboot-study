package com.asiainfo.springboot.jpa;

import java.util.List;

import org.springframework.data.domain.Sort;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年10月20日  下午4:28:24
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public interface UserService {
	
	User findById(long id);
	
	List<User> findByUserName(String userName);
	
	List<User> findUser(String name);
    
	List<User> findUserNative(String name);
    
    Long save(User user);
    
    int update(long id, String name);
    
    boolean delete(long id);
    
    List<User> findByPage(int page);
    
    List<User> findByPage(int page, Sort.Direction sort);
}
