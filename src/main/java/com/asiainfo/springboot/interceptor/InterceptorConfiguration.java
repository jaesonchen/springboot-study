package com.asiainfo.springboot.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Interceptor注册
 * 
 * @author       zq
 * @date         2017年10月22日  下午10:31:57
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
@Configuration
public class InterceptorConfiguration extends WebMvcConfigurerAdapter {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoggerInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(new SessionInterceptor()).addPathPatterns("/**");
    }
}
