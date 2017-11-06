package com.asiainfo.springboot.jpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年10月20日  下午4:18:13
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
@Entity
public class User implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;
	
	@Id
	//GenerationType.TABLE / GenerationType.IDENTITY / GenerationType.SEQUENCE / GenerationType.AUTO
	//@SequenceGenerator(name = "user_generator", sequenceName = "user_sequence", initialValue = 1)
	//@GeneratedValue(generator = "user_generator")
    //@GeneratedValue(strategy = GenerationType.AUTO)
	private long userId;
	@Column(nullable = false)
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
