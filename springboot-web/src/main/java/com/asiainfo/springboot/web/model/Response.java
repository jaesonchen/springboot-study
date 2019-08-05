package com.asiainfo.springboot.web.model;

import com.asiainfo.springboot.web.view.Views;
import com.fasterxml.jackson.annotation.JsonView;

/**   
 * @Description: @JsonView 视图管理
 * 
 * @author chenzq  
 * @date 2019年7月10日 下午7:56:21
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
public class Response {

    @JsonView(Views.Public.class)
    private long id;
    @JsonView(Views.Public.class)
    private String username;
    @JsonView(Views.Private.class)
    private String password;
    
    public Response(long id, String username, String password) {
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
        return "Response [id=" + id + ", username=" + username + ", password=" + password + "]";
    }
}
