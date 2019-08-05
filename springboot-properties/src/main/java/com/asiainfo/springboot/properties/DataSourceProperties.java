package com.asiainfo.springboot.properties;

import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**   
 * @Description: @ConfigurationProperties 批量注入，不支持SpringEL，支持JSR303进行配置文件值及校验 @Validated
 * 
 * @author chenzq  
 * @date 2019年7月9日 下午3:26:31
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
@ConfigurationProperties(prefix = "service.datasource")
@Validated
public class DataSourceProperties {

    @NotNull
    private String driver;
    @NotNull
    private String url;
    @NotNull
    private String username;
    @NotNull
    private String password;
    
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getDriver() {
        return driver;
    }
    public void setDriver(String driver) {
        this.driver = driver;
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
        return "DataSourceProperties [url=" + url + ", driver=" + driver + ", username=" + username + ", password="
                + password + "]";
    }
}
