package com.asiainfo.springboot.mybatis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**   
 * @Description: DataSourceAutoConfiguration
 *               MybatisAutoConfiguration
 *               DataSourceTransactionManagerAutoConfiguration
 *               PersistenceExceptionTranslationAutoConfiguration
 * 
 * @author chenzq  
 * @date 2019年7月15日 下午2:22:23
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
@SpringBootApplication
public class Application {
    
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
