package com.asiainfo.springboot.web.model;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年7月11日 下午1:47:42
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
public class PhoneNumber {

    private String areaCode;
    private String phoneNumber;
    
    public String getAreaCode() {
        return areaCode;
    }
    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    @Override
    public String toString() {
        return "PhoneNumber [areaCode=" + areaCode + ", phoneNumber=" + phoneNumber + "]";
    }
}
