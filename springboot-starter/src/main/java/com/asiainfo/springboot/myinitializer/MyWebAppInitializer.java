package com.asiainfo.springboot.myinitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * @Description: 自定义启动初始化接口，只在war或者jar部署到容器的WEB-INF/lib下时有效
 * 
 * @author chenzq  
 * @date 2019年7月8日 下午7:16:35
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved.
 */
public interface MyWebAppInitializer {

    void onStartup(ServletContext servletContext) throws ServletException;
}
