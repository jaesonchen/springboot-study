package com.asiainfo.springboot.properties;

import java.util.List;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年7月9日 下午4:18:02
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
public class SecretProperties {

    private String accessKey;
    private String secretKey;
    private List<String> path;
    
    public String getAccessKey() {
        return accessKey;
    }
    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }
    public String getSecretKey() {
        return secretKey;
    }
    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
    public List<String> getPath() {
        return path;
    }
    public void setPath(List<String> path) {
        this.path = path;
    }
    @Override
    public String toString() {
        return "SecretProperties [accessKey=" + accessKey + ", secretKey=" + secretKey + ", path=" + path + "]";
    }
}
