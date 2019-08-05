package com.asiainfo.springboot.web.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.asiainfo.springboot.web.formatter.PhoneNumberFormatter;
import com.asiainfo.springboot.web.interceptor.LoggerInterceptor;
import com.asiainfo.springboot.web.interceptor.LoginInterceptor;
import com.asiainfo.springboot.web.resolver.UserHandlerMethodArgumentResolver;

/**   
 * @Description: spring-boot-starter-web 使用WebMvcAutoConfiguration配置类自动配置springmvc。
 * - 由于@EnableWebMvc会引入DelegatingWebMvcConfiguration extends WebMvcConfigurationSupport，
 * - 而WebMvcAutoConfiguration 定义中有@ConditionalOnMissingBean(WebMvcConfigurationSupport.class)，
 * - 所以，如果在配置类上加上@EnableWebMvc，会使WebMvcAutoConfiguration失效，只会保持@EnableWebMvc配置的基本功能。
 * 
 * - springboot2 中WebMvcConfigurer 接口中都有default实现， WebMvcConfigurerAdapter已过时，可以直接实现接口覆盖指定的方法。
 * 
 * @author chenzq  
 * @date 2019年6月25日 下午5:08:58
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
@Configuration
public class SpringMvcConfig implements WebMvcConfigurer {
    
    // cors 跨域请求
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/ajax")
            .allowedOrigins("*")
            .allowedMethods("OPTIONS", "GET", "POST")
            .allowedHeaders("*")
            .allowCredentials(true);
    }

    // 拦截器，springboot2 以后会拦截静态资源，需要在这里排除掉
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoggerInterceptor()).addPathPatterns("/**")
            .excludePathPatterns("/favicon.ico", "/index", "/login", "/error", "/static/**", "/webjars/**", "/swagger-resources/**");
        registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/**")
            .excludePathPatterns("/favicon.ico", "/index", "/login", "/error", "/static/**", "/webjars/**", "/swagger-resources/**");
    }
    
    // 日期格式化
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(new DateFormatter("yyyy-MM-dd HH:mm:ss"));
        registry.addFormatter(new PhoneNumberFormatter());
    }

    // 请求参数解析
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new UserHandlerMethodArgumentResolver());
    }
    
    // 自动注册的静态资源"/"映射到 {"classpath:/META-INF/resources/", "classpath:/resources/", "classpath:/static/", "classpath:/public/" };
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("classpath:/static/");
    }
}
