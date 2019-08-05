package com.asiainfo.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**   
 * @Description: ConfigurationPropertiesAutoConfiguration
 *               PropertyPlaceholderAutoConfiguration
 * 
 * @author chenzq  
 * @date 2019年7月9日 下午5:01:20
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
@SpringBootApplication
public class Application {
    
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        app.setAdditionalProfiles(new String[] { "prop", "dev" });
        app.run(args);
    }
}
