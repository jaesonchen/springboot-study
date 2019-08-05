package com.asiainfo.springboot.jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.asiainfo.springboot.jpa.model.User;

/**
 * @Description: JpaSpecificationExecutor 带查询条件的分页
 * 
 * @author chenzq  
 * @date 2019年7月12日 下午5:18:50
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved.
 */
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

	User findByUsername(String username);
	
	User findByUsernameAndPassword(String username, String password);
	
	@Query("from User u where u.username=:name")
	User findUser(@Param("name") String username);
	
	@Query(value = "select * from user where password=:pass", nativeQuery = true)
	List<User> findByPassword(@Param("pass") String password);
	
	@Modifying
	@Query(value = "update user set username=:name where id=:id", nativeQuery = true)
	int update(@Param("id") long id, @Param("name") String username);
}
