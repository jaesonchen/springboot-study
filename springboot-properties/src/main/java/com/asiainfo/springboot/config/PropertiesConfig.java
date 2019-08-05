package com.asiainfo.springboot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import com.asiainfo.springboot.properties.DataSourceProperties;
import com.asiainfo.springboot.properties.PoolProperties;
import com.asiainfo.springboot.properties.SecretProperties;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年7月9日 下午4:20:23
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
@Configuration
@EnableConfigurationProperties(DataSourceProperties.class)
@PropertySource(value = { "classpath:pool.properties" })
public class PropertiesConfig {

    @Value("${service.id:1000}")
    private String serviceId;
    
    @Autowired
    private Environment env;
    
    @Bean
    @ConfigurationProperties(prefix="service.secret")
    public SecretProperties secretProperties() {
        return new SecretProperties();
    }
    
    @Bean
    public PoolProperties poolProperties() {
        PoolProperties pool = new PoolProperties();
        pool.setId(this.serviceId);
        pool.setTimeout(Long.parseLong(env.getProperty("service.pool.timeout", "6000")));
        pool.setTotal(Integer.parseInt(env.getProperty("service.pool.total", "100")));
        pool.setMaxIdle(Integer.parseInt(env.getProperty("service.pool.maxIdle", "20")));
        pool.setMinIdle(Integer.parseInt(env.getProperty("service.pool.minIdle", "10")));
        return pool;
    }
}
