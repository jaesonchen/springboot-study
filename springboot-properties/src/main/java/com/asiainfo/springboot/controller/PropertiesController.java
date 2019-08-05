package com.asiainfo.springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.asiainfo.springboot.profile.ProfileBean;
import com.asiainfo.springboot.properties.DataSourceProperties;
import com.asiainfo.springboot.properties.PoolProperties;
import com.asiainfo.springboot.properties.SecretProperties;
import com.asiainfo.springboot.xml.Service;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年7月13日 下午2:32:42
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
@RestController
public class PropertiesController {

    @Autowired
    private DataSourceProperties datasource;
    @Autowired
    private SecretProperties secret;
    @Autowired
    private PoolProperties pool;
    @Autowired
    private Service service;
    @Autowired(required = false)
    private ProfileBean profile;
    
    @GetMapping("/datasource")
    public Object datasource() {
        System.out.println(datasource.getClass());
        return datasource;
    }
    
    @GetMapping("/secret")
    public Object secret() {
        return secret;
    }
    
    @GetMapping("/pool")
    public Object pool() {
        return pool;
    }
    
    @GetMapping("/hello")
    public String hello() {
        return service.hello();
    }
    
    @GetMapping("/profile")
    public String profile() {
        return profile == null ? "null" : profile.getProfile();
    }
}
