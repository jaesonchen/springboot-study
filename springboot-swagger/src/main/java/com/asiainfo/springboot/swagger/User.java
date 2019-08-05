package com.asiainfo.springboot.swagger;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年7月7日 下午6:30:00
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
@ApiModel(description = "用户模型")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户id")
    private long id;
    @ApiModelProperty(value = "登陆名")
    private String username;
    @ApiModelProperty(value = "密码")
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
