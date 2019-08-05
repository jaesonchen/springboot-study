package com.asiainfo.springboot.web.resolver;

import javax.servlet.ServletException;

import org.springframework.core.MethodParameter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.asiainfo.springboot.web.annotation.UserParam;
import com.asiainfo.springboot.web.model.User;

/**   
 * @Description: 参数解析器
 * 
 * @author chenzq  
 * @date 2019年7月10日 下午1:18:36
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
public class UserHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        if (parameter.getParameterType().isAssignableFrom(User.class) 
                && parameter.hasParameterAnnotation(UserParam.class)) {
            return true;
        }
        return false;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        String userId = webRequest.getParameter("user_id");
        if (!StringUtils.isEmpty(userId)) {
            // 从数据库中查询
            return new User(Long.parseLong(userId), "chenzq", "1234");
        }
        // 解析失败时，抛出异常
        throw new ServletException("user_id is empty!");
    }
}
