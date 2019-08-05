package com.asiainfo.springboot.dubbo.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年7月14日 上午10:29:06
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
@SpringBootApplication
public class ProviderApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ProviderApplication.class);
        app.setAdditionalProfiles(new String[] { "provider" });
        app.run(args);
    }
}
