package com.asiainfo.springboot.interceptor;

import java.io.Serializable;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年10月22日  下午10:45:18
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class LoginParam implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;
	private String name;
	private String password;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Override
	public String toString() {
		return "LoginParam [name=" + name + ", password=" + password + "]";
	}
}
