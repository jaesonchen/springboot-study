package com.asiainfo.springboot.jdbc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**   
 * @Description: AopAutoConfiguration
 *               DataSourceAutoConfiguration
 *               JdbcTemplateAutoConfiguration
 *               DataSourceTransactionManagerAutoConfiguration
 *               PersistenceExceptionTranslationAutoConfiguration
 * 
 * @author chenzq  
 * @date 2019年7月12日 下午2:21:56
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
@SpringBootApplication
public class Application {
    
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
