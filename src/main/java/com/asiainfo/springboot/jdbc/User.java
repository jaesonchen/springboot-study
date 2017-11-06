package com.asiainfo.springboot.jdbc;

import java.io.Serializable;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年10月20日  上午10:46:42
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class User implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;
	private long userId;
	private String userName;
	
	public User() {}
	public User(long userId, String userName) {
		this.userId = userId;
		this.userName = userName;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	@Override
	public String toString() {
		return "User [userId=" + userId + ", userName=" + userName + "]";
	}
}
