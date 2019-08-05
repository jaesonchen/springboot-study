package com.asiainfo.springboot.mybatis.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年7月16日 下午4:43:25
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
@JsonIgnoreProperties(value = {"handler"})
public class Contact implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long userId;
    private String address;
    private int zipCode;
    
    public Contact() {
    }
    public Contact(String address, int zipCode) {
        this.address = address;
        this.zipCode = zipCode;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public int getZipCode() {
        return zipCode;
    }
    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
    }
    @Override
    public String toString() {
        return "Contact [id=" + id + ", userId=" + userId + ", address=" + address + ", zipCode=" + zipCode + "]";
    }
}
