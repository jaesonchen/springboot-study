package com.asiainfo.springboot.web.exception;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;

/**   
 * @Description: springboot默认的/error由 BasicErrorController类处理
 * 
 * @author chenzq  
 * @date 2019年7月10日 下午4:03:30
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    final Logger logger = LoggerFactory.getLogger(getClass());
    
    public static final Integer ERROR = 100;
    
    private final ObjectMapper mapper = new ObjectMapper();
    
    // RuntimeException异常返回自定义/custom_error
    @ExceptionHandler(value = RuntimeException.class)
    public ModelAndView htmlErrorHandler(HttpServletRequest request, Exception ex) throws Exception {
        logger.info("exception catch: {}", ex.getMessage());
        ModelAndView mav = new ModelAndView();
        mav.addObject("url", request.getRequestURL());
        mav.addObject("exception", getCause(ex));
        mav.setViewName("custom_error");
        return mav;
    }
    
    // 自定义异常返回json
    @ExceptionHandler(value = MyException.class)
    public String jsonErrorHandler(HttpServletResponse response, Exception ex) throws Exception {
        logger.info("exception catch: {}", ex.getMessage());
        Map<String, Object> map = new HashMap<>();
        map.put("code", ERROR);
        map.put("message", getCause(ex).getMessage());
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(mapper.writeValueAsString(map));
        return null;
    }
    
    // 获取异常的触发点
    protected Throwable getCause(Exception ex) {
        Throwable error = ex;
        while (error.getCause() != null) {
            error = error.getCause();
        }
        return error;
    }
}
