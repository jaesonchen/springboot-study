package com.asiainfo.springboot.session;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**   
 * @Description: SessionAutoConfiguration / SessionRepositoryFilter / SessionRepositoryRequestWrapper / DelegatingFilterProxy
 * 
 * @author chenzq  
 * @date 2019年7月15日 下午3:15:00
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
@SpringBootApplication
public class Application {
    
    // java --server.port=8081 Application
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}