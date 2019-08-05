package com.asiainfo.springboot.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**   
 * @Description: ServletWebServerFactoryAutoConfiguration(Tomcat)
 *               HttpEncodingAutoConfiguration(CharacterEncodingFilter)
 *               MultipartAutoConfiguration
 *               ErrorMvcAutoConfiguration
 *               DispatcherServletAutoConfiguration
 *               WebMvcAutoConfiguration
 *               
 *               JacksonAutoConfiguration
 *               GsonAutoConfiguration
 *               HttpMessageConvertersAutoConfiguration
 *               RestTemplateAutoConfiguration
 *               
 *               ThymeleafAutoConfiguration
 *               FreeMarkerAutoConfiguration
 * 
 * @author chenzq  
 * @date 2019年7月10日 下午3:33:39
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
@SpringBootApplication
public class Application {
    
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
