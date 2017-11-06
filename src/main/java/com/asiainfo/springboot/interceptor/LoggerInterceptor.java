package com.asiainfo.springboot.interceptor;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * 
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年10月22日  下午11:00:15
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class LoggerInterceptor implements HandlerInterceptor {
	
    public static final String START_TIME = "_start_time";
    public static final String REQUEST_ENTITY = "_request_entity";
    public static final String RESPONSE_ENTITY = "_response_entity";

    /**
     * 进入SpringMVC的Controller之前开始记录日志实体
     * @param request 请求对象
     * @param response 响应对象
     * @param o
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
    	
        Map<String, Object> map = new HashMap<>();
        //获取请求参数信息
        String data = JSON.toJSONString(request.getParameterMap(),
                SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.WriteMapNullValue);

        map.put("uri", request.getRequestURI());
        map.put("ip", request.getRemoteAddr());
        map.put("method", request.getMethod());
        map.put("type", request.getHeader("X-Requested-With"));
        map.put("request", data);

        request.setAttribute(START_TIME, System.currentTimeMillis());
        request.setAttribute(REQUEST_ENTITY, map);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) throws Exception {
    	
        //获取请求错误码
        int status = response.getStatus();
        long currentTime = System.currentTimeMillis();
        //请求开始时间
        long time = Long.valueOf(request.getAttribute(START_TIME).toString());
        @SuppressWarnings("unchecked")
		Map<String, Object> map = (Map<String, Object>) request.getAttribute(REQUEST_ENTITY);
        map.put("time", String.valueOf((currentTime - time)));
        map.put("status", status);
        map.put("response", JSON.toJSONString(request.getAttribute(RESPONSE_ENTITY),
                SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.WriteMapNullValue));

        //执行将日志写入数据库
    }
}
