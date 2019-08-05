package com.asiainfo.springboot.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**   
 * @Description: filter注册测试
 * 
 * @author chenzq  
 * @date 2019年7月10日 下午6:05:48
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
public class MyFilter implements Filter {

    final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("MyFilter init ...");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        logger.info("MyFilter start, uri: {}", ((HttpServletRequest) request).getRequestURI());
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
