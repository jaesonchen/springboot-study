package com.asiainfo.springboot.redis;

import java.io.Serializable;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年7月19日 下午1:33:37
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String userName;
    private String password;
    
    public User() {
    }
    public User(Long id, String userName, String password) {
        this.id = id;
        this.userName = userName;
        this.password = password;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    @Override
    public String toString() {
        return "User [id=" + id + ", userName=" + userName + ", password=" + password + "]";
    }
}
