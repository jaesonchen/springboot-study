package com.asiainfo.springboot.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年10月20日  上午11:36:26
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public interface UserRepository extends JpaRepository<User, Long> {

	User findByUserId(long userId);
	
	List<User> findByUserName(String userName);
	
	@Query("from User u where u.userName=:name")
	List<User> findUser(@Param("name") String name);
	
	@Query(value="select * from user where user_name=:name", nativeQuery=true)
	List<User> findUserNative(@Param("name") String name);
	
	@Modifying
	@Query(value="update user set user_name=:name where user_id=:id", nativeQuery=true)
	int updateNative(@Param("id") long id, @Param("name") String name);
}
