package com.asiainfo.springboot.web.interceptor;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @Description: log操作日志拦截
 * 
 * @author chenzq  
 * @date 2019年7月10日 上午10:57:19
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved.
 */
public class LoggerInterceptor implements HandlerInterceptor {
	
    final Logger logger = LoggerFactory.getLogger(getClass());
    
    public static final String REQUEST_ENTITY = "_request_entity";
    
    final ThreadLocal<Long> local = new ThreadLocal<>();
    final ObjectMapper mapper = new ObjectMapper();

    // 进入之前开始记录日志实体和开始时间
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
    	
        // 记录开始时间
        local.set(System.currentTimeMillis());
        // 记录请求信息
        Map<String, Object> map = new HashMap<>();
        map.put("uri", request.getRequestURI());
        map.put("ip", request.getRemoteAddr());
        map.put("method", request.getMethod());
        // 获取请求参数
        String data = mapper.writeValueAsString(request.getParameterMap());
        map.put("request", data);
        request.setAttribute(REQUEST_ENTITY, map);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    // 返回之前记录操作日志
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) throws Exception {
    	
        try {
            // 响应状态码
            int status = response.getStatus();
            @SuppressWarnings("unchecked")
    		Map<String, Object> map = (Map<String, Object>) request.getAttribute(REQUEST_ENTITY);
            map.put("time", System.currentTimeMillis() - local.get());
            map.put("status", status);
            // 记录操作日志
            logger.info("操作日志：{}", mapper.writeValueAsString(map));
        } finally {
            local.remove();
        }
    }
}
