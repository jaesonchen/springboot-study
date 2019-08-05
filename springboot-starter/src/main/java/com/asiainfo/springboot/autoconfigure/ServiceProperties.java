package com.asiainfo.springboot.autoconfigure;

import java.io.Serializable;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年7月8日 下午2:36:37
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
@ConfigurationProperties(prefix = "service")
public class ServiceProperties implements Serializable {

    private static final long serialVersionUID = 1L;

    private String welcome;

    public String getWelcome() {
        return welcome;
    }
    public void setWelcome(String welcome) {
        this.welcome = welcome;
    }
    @Override
    public String toString() {
        return "ServiceProperties [welcome=" + welcome + "]";
    }
}
