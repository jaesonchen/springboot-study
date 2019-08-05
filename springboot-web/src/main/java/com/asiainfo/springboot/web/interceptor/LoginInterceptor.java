package com.asiainfo.springboot.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.asiainfo.springboot.web.model.User;

/**
 * @Description: login登陆验证拦截
 * 
 * @author chenzq  
 * @date 2019年7月10日 上午11:20:07
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved.
 */
public class LoginInterceptor implements HandlerInterceptor {
    
    final Logger logger = LoggerFactory.getLogger(getClass());
    
    public static final String SESSION_USER = "_session_user";
    
    public static final String ROOT = "/";
    public static final String LOGIN = "/login";
    public static final String ERROR = "/error";
	
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
    	
        logger.info("requestURI: {}", request.getRequestURI());
        // index 和 登录不做拦截
        if (request.getRequestURI().equals(ROOT) 
                || request.getRequestURI().startsWith(LOGIN)
                || request.getRequestURI().startsWith(ERROR)) {
            logger.info("skip uri: {}", request.getRequestURI());
            return true;
        }
        // 验证session是否已登陆
        Object obj = request.getSession().getAttribute(SESSION_USER);
        if (null == obj) {
            logger.info("redirect to {}", LOGIN);
            response.sendRedirect(LOGIN);
            return false;
        }
        logger.info("user: {} has login.", ((User) obj).getUsername());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
    }
}
