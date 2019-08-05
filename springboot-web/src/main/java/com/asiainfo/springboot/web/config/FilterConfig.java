package com.asiainfo.springboot.web.config;

import javax.servlet.Filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.asiainfo.springboot.web.filter.MyFilter;

/**   
 * @Description: filter 注册方式：@ServletComponentScan + @WebFilter 和 FilterRegistrationBean
 * 
 * @author chenzq  
 * @date 2019年7月10日 下午6:07:12
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<?> myFilterRegistration() {

        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new MyFilter());
        registration.addUrlPatterns("/*");
        registration.addInitParameter("total", "1000");
        registration.setName("myFilter");
        registration.setOrder(1);
        return registration;
    }
}
