package com.asiainfo.springboot.security.image;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年7月29日 下午4:30:49
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
public class ValidateFilter extends OncePerRequestFilter {

    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();
    private AuthenticationFailureHandler authenticationFailureHandler;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if ("/login".equals(request.getRequestURI()) && "post".equalsIgnoreCase(request.getMethod())) {
            try {
                // 1. 进行验证码的校验
                validate(new ServletWebRequest(request));
            } catch (AuthenticationException e) {
                // 2. 如果校验不通过，调用SpringSecurity的校验失败处理器
                authenticationFailureHandler.onAuthenticationFailure(request, response, e);
                return;
            }
        }
        // 3. 校验通过
        filterChain.doFilter(request, response);
    }
    
    // 验证码校验
    private void validate(ServletWebRequest request) throws AuthenticationException {
        
        try {
            // 1. 获取请求中的验证码
            String codeInRequest = ServletRequestUtils.getStringParameter(request.getRequest(), "imageCode");
            
            // 2. 校验空值情况
            if (StringUtils.isEmpty(codeInRequest)) {
                throw new ValidateException("验证码不能为空");
            }
            
            // 3. 获取服务器session池中的验证码
            //ImageCode codeInSession = (ImageCode) request.getAttribute(ValidateController.SESSION_KEY, RequestAttributes.SCOPE_SESSION);
            ImageCode codeInSession = (ImageCode) sessionStrategy.getAttribute(request, ValidateController.SESSION_KEY);
            if (null == codeInSession) {
                throw new ValidateException("验证码不存在");
            }
    
            // 4. 校验服务器session池中的验证码是否过期
            if (codeInSession.isExpried()) {
                throw new ValidateException("验证码过期了");
            }
            
            // 5. 请求验证码校验
            if (!codeInRequest.equals(codeInSession.getCode())) {
                throw new ValidateException("验证码不匹配");
            }
        } catch (ServletRequestBindingException e) {
            throw new ValidateException("验证码读取异常");
        } finally {
            // 6. 移除已完成校验的验证码
            request.removeAttribute(ValidateController.SESSION_KEY, RequestAttributes.SCOPE_SESSION);
        }
    }

    public void setAuthenticationFailureHandler(AuthenticationFailureHandler authenticationFailureHandler) {
        this.authenticationFailureHandler = authenticationFailureHandler;
    }
}
