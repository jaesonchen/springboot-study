package com.asiainfo.springboot.jpa.model;

/**   
 * @Description: 分页查询条件
 * 
 * @author chenzq  
 * @date 2019年7月15日 下午4:26:38
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
public class QueryParam {

    private String username;
    private String password;
    
    public QueryParam(String username, String password) {
        this.username = username;
        this.password = password;
    }
    public static QueryParam username(String username) {
        return new QueryParam(username, null);
    }
    public static QueryParam password(String password) {
        return new QueryParam(null, password);
    }
    public static QueryParam of(String username, String password) {
        return new QueryParam(username, password);
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
        return "QueryParam [username=" + username + ", password=" + password + "]";
    }
}
