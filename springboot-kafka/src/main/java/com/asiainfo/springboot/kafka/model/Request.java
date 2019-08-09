package com.asiainfo.springboot.kafka.model;

import java.io.Serializable;
import java.util.Date;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年8月7日 下午7:33:56
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
public class Request implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;
    private String username;
    private Date date;
    
    public Request() {
        super();
    }
    public Request(Long id, String username, Date date) {
        super();
        this.id = id;
        this.username = username;
        this.date = date;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    @Override
    public String toString() {
        return "Request [id=" + id + ", username=" + username + ", date=" + date + "]";
    }
}
