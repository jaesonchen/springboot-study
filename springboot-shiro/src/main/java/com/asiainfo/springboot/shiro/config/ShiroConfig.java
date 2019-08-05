package com.asiainfo.springboot.shiro.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.asiainfo.springboot.shiro.realm.UserRealm;
import com.asiainfo.springboot.shiro.util.PasswordHelper;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年7月20日 下午5:51:19
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
@Configuration
public class ShiroConfig {

    // shiro 权限拦截器
    // user: UserFilter 用户拦截器，用户已经身份验证 / RememberMe记住用户登录状态 都可
    // authc：FormAuthenticationFilter 基于表单的拦截器；如果没有登录会跳到相应的登录页面登录
    // roles：RolesAuthorizationFilter 角色授权拦截器，验证用户是否拥有所有角色，通过[ ]指定具体角色，这里的角色名称与数据库中配置一致
    // perms：PermissionsAuthorizationFilter 权限授权拦截器，验证用户是否拥有所有权限，通过[ ]指定具体权限，这里的权限名称与数据库中配置一致
    // logout: LogoutFilter 退出拦截器，主要属性：redirectUrl：退出成功后重定向的地址
    // anon ：AnonymousFilter 匿名拦截器，即不需要登录即可访问；一般用于静态资源过滤
    @Bean
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
        
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        // 没有登录时的跳转地址
        shiroFilterFactoryBean.setLoginUrl("/login");
        // 登录成功后的跳转地址
        shiroFilterFactoryBean.setSuccessUrl("/index");
        // 权限不足时的跳转地址，只对这里配置的filterChainDefinitionMap 生效，注解配置的权限需要配置全局的异常处理
        shiroFilterFactoryBean.setUnauthorizedUrl("/403");
        
        Map<String, String> filterChainDefinitionMap = new HashMap<>();
        filterChainDefinitionMap.put("/user/index", "user");
        filterChainDefinitionMap.put("/user/admin", "roles[admin]");
        filterChainDefinitionMap.put("/user/renewable", "perms[user:add,user:edit]");
        filterChainDefinitionMap.put("/user/removable", "perms[user:del]");
        filterChainDefinitionMap.put("/logout", "logout");
        filterChainDefinitionMap.put("/*", "anon");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        
        return shiroFilterFactoryBean;
    }

    // 密码散列算法
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName(PasswordHelper.ALGORITHM_NAME);   // 散列算法
        hashedCredentialsMatcher.setHashIterations(PasswordHelper.HASH_ITERATIONS);     // 散列次数
        return hashedCredentialsMatcher;
    }

    // 角色/权限 获取类
    @Bean
    public UserRealm shiroRealm() {
        UserRealm shiroRealm = new UserRealm();
        shiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return shiroRealm;
    }

    // 安全管理器
    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(shiroRealm());
        securityManager.setRememberMeManager(rememberMeManager());
        securityManager.setCacheManager(cacheManager());
        return securityManager;
    }
    
    // ehcache 缓存权限数据
    @Bean
    public EhCacheManager cacheManager() {
        EhCacheManager em = new EhCacheManager();
        em.setCacheManagerConfigFile("classpath:ehcache.xml");
        return em;
    }
    
    // rememberMe 功能配置
    public SimpleCookie rememberMeCookie() {
        // 设置cookie名称，对应login.html页面的 <input type="checkbox" name="rememberMe" />
        SimpleCookie cookie = new SimpleCookie("rememberMe");
        // 设置cookie的过期时间，单位秒
        cookie.setMaxAge(86400);
        return cookie;
    }
    
    // rememberMe cookie 管理对象
    public CookieRememberMeManager rememberMeManager() {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());
        // rememberMe cookie加密的密钥 
        cookieRememberMeManager.setCipherKey(Base64.decode("4AvVhmFLUs0KTA3Kprsdag=="));
        return cookieRememberMeManager;
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }
    
    // 开启shiro aop注解支持 @RequiresRoles / @RequiresPermissions，使用注解时没有权限会抛出异常，需要配置异常处理
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }
    
    // 在thymeleaf里使用shiro的标签
/*    @Bean
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }*/
}
