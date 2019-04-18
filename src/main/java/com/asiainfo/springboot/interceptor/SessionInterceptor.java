package com.asiainfo.springboot.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * login登陆验证拦截
 * 
 * @author       zq
 * @date         2017年10月22日  下午10:33:12
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class SessionInterceptor implements HandlerInterceptor {
    
    final Logger logger = LoggerFactory.getLogger(getClass());
    
    static final String LOGIN = "/login";
    static final String LOGIN_VIEW = "/login_view";
	
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
    	
        logger.info("requestURI: {}", request.getRequestURI());
        //登录不做拦截
        if (LOGIN.equals(request.getRequestURI()) || LOGIN_VIEW.equals(request.getRequestURI())) {
            logger.info("忽略登陆请求！");
            return true;
        }
        //验证session是否存在
        Object obj = request.getSession().getAttribute(InterceptorApplication.SESSION_USER);
        if (null == obj) {
            logger.info("未登陆，跳转到登陆页面！");
            response.sendRedirect(LOGIN_VIEW);
            return false;
        }
        logger.info("用户已登陆！");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
