package com.asiainfo.springboot.oauth2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**   
 * @Description: 
 * 
 * code:
 * http://localhost:8080/oauth/authorize?response_type=code&client_id=aiqiyi&scope=all&redirect_uri=http://baidu.com
 * 
 * authorization_code:
 * http://localhost:8080/oauth/token?grant_type=authorization_code&code=zVh42T&client_id=aiqiyi&client_secret=secret&redirect_uri=http://baidu.com
 * http://localhost:8080/api?access_token=bb282428-1426-46f6-9364-341def703ffe
 * 
 * refresh_token:
 * http://localhost:8080/oauth/token?grant_type=refresh_token&refresh_token=ce3dd10e-ec60-4399-9076-ee2140b04a61&client_id=aiqiyi&client_secret=secret
 * 
 * password:
 * http://localhost:8080/oauth/token?username=admin&password=1234&grant_type=password&client_id=aiqiyi&client_secret=secret
 * 
 * @author chenzq  
 * @date 2019年7月15日 下午3:15:00
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
@SpringBootApplication
public class Application {
    
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}