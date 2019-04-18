package com.asiainfo.springboot.mvc;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * springmvc的webapp下的web资源打包在META-INF/resources下
 * springmvc的static目录默认在META-INF/resources/static 下
 * 
 * @author       zq
 * @date         2017年10月22日  下午11:14:57
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
@Configuration
public class StaticConfiguration extends WebMvcConfigurerAdapter {
    
    //自定义静态资源文件路径
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("classpath:/static/");
    }
}
