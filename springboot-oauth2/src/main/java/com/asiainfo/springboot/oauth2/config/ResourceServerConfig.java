package com.asiainfo.springboot.oauth2.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年7月30日 下午1:59:46
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    public static final String RESOURCE_ID = "weixin";
    
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId(RESOURCE_ID);
    }
    
    @Override
    public void configure(HttpSecurity http) throws Exception {
        
        http.requestMatchers()
                .antMatchers("/api/**")
            .and()
                .authorizeRequests()
                .antMatchers("/api/**")
                .authenticated();
    }
}
