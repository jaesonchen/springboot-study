package com.asiainfo.springboot.web.model;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**   
 * @Description: @valid注解
 * 
 * @author chenzq  
 * @date 2019年7月10日 下午1:20:28
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private long id;
    @NotEmpty(message = "用户名不能为空")
    private String username;
    @NotNull(message = "密码不能为空")
    @Size(min = 4, max = 10, message = "密码长度必须在4 - 10之间")
    private String password;
    
    public User() { }
    public User(long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    @Override
    public String toString() {
        return "User [id=" + id + ", username=" + username + ", password=" + password + "]";
    }
}
