package com.asiainfo.springboot.jpa;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年10月20日  下午4:28:43
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository repository;
	
	/* 
	 * @Description: TODO
	 * @param id
	 * @return
	 * @see com.asiainfo.springboot.jpa.UserService#findById(long)
	 */
	@Override
	public User findById(long id) {
		return repository.findByUserId(id);
	}

	/* 
	 * @Description: TODO
	 * @param userName
	 * @return
	 * @see com.asiainfo.springboot.jpa.UserService#findByUserName(java.lang.String)
	 */
	@Override
	public List<User> findByUserName(String userName) {
		return repository.findByUserName(userName);
	}

	/* 
	 * @Description: TODO
	 * @param name
	 * @return
	 * @see com.asiainfo.springboot.jpa.UserService#findUser(java.lang.String)
	 */
	@Override
	public List<User> findUser(String name) {
		return repository.findUser(name);
	}

	/* 
	 * @Description: TODO
	 * @param name
	 * @return
	 * @see com.asiainfo.springboot.jpa.UserService#findUserNative(java.lang.String)
	 */
	@Override
	public List<User> findUserNative(String name) {
		return repository.findUserNative(name);
	}
	
	/* 
	 * @Description: TODO
	 * @param user
	 * @see com.asiainfo.springboot.jpa.UserService#save(com.asiainfo.springboot.jpa.User)
	 */
	@Override
	public Long save(User user) {
		return repository.save(user).getUserId();
	}

	/* 
	 * @Description: TODO
	 * @param id
	 * @param name
	 * @return
	 * @see com.asiainfo.springboot.jpa.UserService#update(long, java.lang.String)
	 */
	@Override
	public int update(long id, String name) {
		return repository.updateNative(id, name);
	}

	/* 
	 * @Description: TODO
	 * @param id
	 * @see com.asiainfo.springboot.jpa.UserService#delete(long)
	 */
	@Override
	public boolean delete(long id) {
		if (!repository.exists(id)) {
			return false;
		}
		repository.delete(id);
		return true;
	}

	/* 
	 * @Description: TODO
	 * @param userName
	 * @param pageNum
	 * @return
	 * @see com.asiainfo.springboot.jpa.UserService#findByPage(int)
	 */
	@Override
	public List<User> findByPage(int page) {
		return repository.findAll(new PageRequest(page, 10)).getContent();
	}
	
	/* 
	 * @Description: TODO
	 * @param page
	 * @param sort
	 * @return
	 * @see com.asiainfo.springboot.jpa.UserService#findByPage(int, org.springframework.data.domain.Sort.Direction)
	 */
	@Override
	public List<User> findByPage(int page, Direction sort) {
		return repository.findAll(new PageRequest(page, 10, new Sort(sort, "userName"))).getContent();
	}
}
