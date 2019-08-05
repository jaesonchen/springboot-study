package com.asiainfo.springboot.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**   
 * @Description: 需要在META-INF/spring.factories中配置org.springframework.boot.autoconfigure.EnableAutoConfiguration=com.asiainfo.springboot.autoconfigure.ServiceAutoConfigure
 * 
 * @author chenzq  
 * @date 2019年7月8日 下午2:34:40
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
@Configuration
@ConditionalOnClass({ Service.class })
@EnableConfigurationProperties(ServiceProperties.class)
//@AutoConfigureAfter({ Service.class })
public class ServiceAutoConfigure {

    @Configuration
    @ConditionalOnClass(Repository.class)
    protected static class ServiceConfiguration {
        
        @Bean
        @ConditionalOnMissingBean
        public Service service(ServiceProperties properties) {
            Service service = new Service();
            service.setWelcome(properties.getWelcome());
            service.setRepository(new Repository());
            return service;
        }
    }
}
