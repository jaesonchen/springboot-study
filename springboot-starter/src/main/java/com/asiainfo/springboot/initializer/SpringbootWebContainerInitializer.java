package com.asiainfo.springboot.initializer;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import com.asiainfo.springboot.starter.Application;

/**   
 * @Description: springboot 应用的web 容器初始化入口
 * 
 * @author chenzq  
 * @date 2019年7月8日 下午9:31:27
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
public class SpringbootWebContainerInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(Application.class);
    }
}
