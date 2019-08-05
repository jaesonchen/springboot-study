package com.asiainfo.springboot.web.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

/**   
 * @Description: cors filter 自定义实现
 * 
 * @ServletComponentScan("com.asiainfo.springboot.web.filter")
 * 
 * @author chenzq  
 * @date 2019年6月29日 下午2:04:59
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
@WebFilter(urlPatterns = "/*", filterName = "myCorsFilter", 
    initParams = { 
            @WebInitParam(name = "origin", value = "*"), 
            @WebInitParam(name = "methods", value = "GET, POST, OPTIONS"), 
            @WebInitParam(name = "headers", value = "*"), 
            @WebInitParam(name = "credentials", value = "true"), 
            @WebInitParam(name = "maxAge", value = "3600") })
public class MyCorsFilter implements Filter {
    
    final Logger logger = LoggerFactory.getLogger(getClass());
    
    // cors response header
    public static final String ACCESS_CONTROL_ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";
    public static final String ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";
    public static final String ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";
    public static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
    public static final String ACCESS_CONTROL_MAX_AGE = "Access-Control-Max-Age";
    public static final String VARY = "Vary";
    // cors request header
    public static final String ACCESS_CONTROL_REQUEST_HEADERS = "Access-Control-Request-Headers";
    public static final String ACCESS_CONTROL_REQUEST_METHOD = "Access-Control-Request-Method";
    public static final String ORIGIN = "Origin";
    // all allow
    public static final String ALL = "*";
    // url pattern
    private static final String HTTP_PATTERN = "(?i)(http|https):";
    private static final String USERINFO_PATTERN = "([^@\\[/?#]*)";
    private static final String HOST_IPV4_PATTERN = "[^\\[/?#:]*";
    private static final String HOST_IPV6_PATTERN = "\\[[\\p{XDigit}\\:\\.]*[%\\p{Alnum}]*\\]";
    private static final String HOST_PATTERN = "(" + HOST_IPV6_PATTERN + "|" + HOST_IPV4_PATTERN + ")";
    private static final String PORT_PATTERN = "(\\d*(?:\\{[^/]+?\\})?)";
    private static final String PATH_PATTERN = "([^?#]*)";
    private static final String LAST_PATTERN = "(.*)";
    public static final Pattern HTTP_URL_PATTERN = Pattern.compile(
            "^" + HTTP_PATTERN + "(//(" + USERINFO_PATTERN + "@)?" + HOST_PATTERN + "(:" + PORT_PATTERN + ")?" + ")?" +
                    PATH_PATTERN + "(\\?" + LAST_PATTERN + ")?");
    
    // filter 自定义参数
    private List<String> allowedOrigins;
    private List<String> allowedMethods;
    private List<String> allowedHeaders;
    private Boolean allowCredentials;
    private Long maxAge;
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        
        logger.info("MyCorsFilter init ...");
        String origin = filterConfig.getInitParameter("origin");
        if (!StringUtils.isEmpty(origin)) {
            this.allowedOrigins = new ArrayList<>();
            this.allowedOrigins.addAll(Arrays.asList(origin.split("\\s*,\\s*")));
        }
        
        String methods = filterConfig.getInitParameter("methods");
        if (!StringUtils.isEmpty(methods)) {
            this.allowedMethods = new ArrayList<>();
            this.allowedMethods.addAll(Arrays.asList(methods.split("\\s*,\\s*")));
        }
        
        String headers = filterConfig.getInitParameter("headers");
        if (!StringUtils.isEmpty(headers)) {
            this.allowedHeaders = new ArrayList<>();
            this.allowedHeaders.addAll(Arrays.asList(headers.split("\\s*,\\s*")));
        }
        
        String credentials = filterConfig.getInitParameter("credentials");
        if (!StringUtils.isEmpty(credentials)) {
            this.allowCredentials = Boolean.valueOf(credentials);
        }
        
        String maxAge = filterConfig.getInitParameter("maxAge");
        if (!StringUtils.isEmpty(maxAge)) {
            this.maxAge = Long.valueOf(maxAge);
        }
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        
        logger.info("cors begin filter ......");
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        // cors请求
        if (isCorsRequest(request)) {
            // 处理失败的cors请求直接返回
            if (!processRequest(request, response)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid CORS request!");
                return;
            }
            // 处理成功的预检请求直接返回
            if (isPreFlightRequest(request)) {
                response.setStatus(HttpServletResponse.SC_OK);
                return;
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub
    }
    
    // 处理请求
    protected boolean processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        
        // 是否已处理cors
        if (responseHasCors(response)) {
            logger.info("Skip CORS processing: response already contains 'Access-Control-Allow-Origin' header!");
            return true;
        }
        // 是否同源请求
        if (isSameOrigin(request)) {
            logger.info("Skip CORS processing: request is from same origin!");
            return true;
        }
        
        // 添加Vary，用于缓存算法
        response.setHeader(VARY, StringUtils.collectionToCommaDelimitedString(
                Arrays.asList(ORIGIN, ACCESS_CONTROL_REQUEST_METHOD, ACCESS_CONTROL_REQUEST_HEADERS)));
        
        // allowOrigin
        String requestOrigin = request.getHeader(ORIGIN);
        String allowOrigin = checkOrigin(requestOrigin);
        if (StringUtils.isEmpty(allowOrigin)) {
            logger.info("Rejecting CORS request because '" + requestOrigin + "' origin is not allowed!");
            return false;
        }
        
        // 是否预检请求
        boolean preFlightRequest = isPreFlightRequest(request);
        // allowMethods
        String requestMethod = preFlightRequest ? request.getHeader(ACCESS_CONTROL_REQUEST_METHOD) : request.getMethod();
        List<String> allowMethods = checkMethod(requestMethod);
        if (ObjectUtils.isEmpty(allowMethods)) {
            logger.info("Rejecting CORS request because '" + requestMethod + "' request method is not allowed!");
            return false;
        }
        
        // allowHeaders
        // requestHeaders + Content-Type + Content-Length
        List<String> requestHeaders = preFlightRequest ? Arrays.asList(request.getHeader(ACCESS_CONTROL_REQUEST_HEADERS).split("\\s*,\\s*")) : null;
        List<String> allowHeaders = preFlightRequest ? checkHeaders(requestHeaders) : null;
        if (preFlightRequest && ObjectUtils.isEmpty(allowHeaders)) {
            logger.info("Rejecting CORS request because '" + requestHeaders + "' request headers are not allowed");
            return false;
        }
        
        response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, allowOrigin);
        if (preFlightRequest) {
            response.setHeader(ACCESS_CONTROL_ALLOW_METHODS, StringUtils.collectionToCommaDelimitedString(allowMethods));
            response.setHeader(ACCESS_CONTROL_ALLOW_HEADERS, StringUtils.collectionToCommaDelimitedString(allowHeaders));
        }
        if (Boolean.TRUE.equals(this.allowCredentials)) {
            response.setHeader(ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
        }
        if (preFlightRequest && this.maxAge != null) {
            response.setHeader(ACCESS_CONTROL_MAX_AGE, this.maxAge.toString());
        }
        
        response.flushBuffer();
        return true;
    }
    
    // 是否跨域请求
    protected boolean isCorsRequest(HttpServletRequest request) {
        return (request.getHeader(ORIGIN) != null);
    }
    
    // 是否跨域请求的预检请求
    protected boolean isPreFlightRequest(HttpServletRequest request) {
        return (isCorsRequest(request) && "OPTIONS".equals(request.getMethod()) &&
                request.getHeader(ACCESS_CONTROL_REQUEST_METHOD) != null);
    }
    
    // 是否已有其他的cors 过滤器进行过处理
    protected boolean responseHasCors(HttpServletResponse response) {
        return (response.getHeader(ACCESS_CONTROL_ALLOW_ORIGIN) != null);
    }
    
    // response的allow-Origin
    protected String checkOrigin(String requestOrigin) {
        
        if (!StringUtils.hasText(requestOrigin)) {
            return null;
        }
        if (ObjectUtils.isEmpty(this.allowedOrigins)) {
            return null;
        }
        // 开启cookie时不能返回 * ，否则Set-Cookie失效
        if (this.allowedOrigins.contains(ALL)) {
            if (this.allowCredentials != Boolean.TRUE) {
                return ALL;
            } else {
                return requestOrigin;
            }
        }
        for (String allowedOrigin : this.allowedOrigins) {
            if (requestOrigin.equalsIgnoreCase(allowedOrigin)) {
                return requestOrigin;
            }
        }
        return null;
    }
    
    // response的allow-method
    protected List<String> checkMethod(String requestMethod) {
        
        if (!StringUtils.hasText(requestMethod)) {
            return null;
        }
        if (ObjectUtils.isEmpty(this.allowedMethods)) {
            return null;
        }
        if (this.allowedMethods.contains(ALL)) {
            return Collections.singletonList(ALL);
        }
        if (this.allowedMethods.contains(requestMethod)) {
            return this.allowedMethods;
        }
        return null;
    }
    
    // response的allow-headers
    protected List<String> checkHeaders(List<String> requestHeaders) {
        
        if (ObjectUtils.isEmpty(requestHeaders)) {
            return null;
        }
        if (ObjectUtils.isEmpty(this.allowedHeaders)) {
            return null;
        }
        if (this.allowedHeaders.contains(ALL)) {
            return requestHeaders;
        }
        List<String> headers = new ArrayList<>();
        for (String header : requestHeaders) {
            if (this.allowedHeaders.contains(header)) {
                headers.add(header);
            }
        }
        return headers;
    }
    
    // 是否同源请求
    protected boolean isSameOrigin(HttpServletRequest request) {
        
        String origin = request.getHeader(ORIGIN);
        if (origin == null) {
            return true;
        }
        
        String scheme;
        String host;
        int port;
        Matcher matcher = HTTP_URL_PATTERN.matcher(origin);
        if (matcher.matches()) {
            scheme = matcher.group(1);
            host = matcher.group(5);
            port = (matcher.group(7) == null) ? ("http".equals(scheme) ? 80 : 443) : Integer.parseInt(matcher.group(7));
            return request.getScheme().equals(scheme) && request.getServerName().equals(host) && request.getServerPort() == port;
        }
        return false;
    }
}
