package com.asiainfo.springboot.dubbo.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年7月14日 上午11:07:43
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
@SpringBootApplication
public class ConsumerApplication {
    
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ConsumerApplication.class);
        app.setAdditionalProfiles(new String[] { "consumer" });
        app.run(args);
    }
}
