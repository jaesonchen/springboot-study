package com.asiainfo.springboot.security.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import com.asiainfo.springboot.security.handler.MyAuthenticationSuccessHandler;
import com.asiainfo.springboot.security.image.ValidateFilter;

/**   
 * @Description: @EnableGlobalMethodSecurity(prePostEnabled = true) 开启@PreAuthorize注解，spring-security默认禁用注解
 * 
 * @author chenzq  
 * @date 2019年7月28日 下午7:16:24
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService detailsService;
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(detailsService)
            .passwordEncoder(passwordEncoder());
    }
    
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/**/*.js", "/**/*.css", "/**/*.jpg", "/**/*.png", "/**/*.jpeg");
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        
        ValidateFilter validateFilter = new ValidateFilter();
        validateFilter.setAuthenticationFailureHandler(new SimpleUrlAuthenticationFailureHandler("/login?error"));
        http.addFilterBefore(validateFilter, UsernamePasswordAuthenticationFilter.class)
            .requestMatchers()
                .antMatchers("/**")
            .and()
            .authorizeRequests()
                .antMatchers("/swagger-ui.html", "/webjars/**", "/swagger-resources/**", "/v2/**").permitAll()
                .antMatchers("/user/register", "/code/image").permitAll()
                .anyRequest()
                .authenticated()
            .and()
                .formLogin()
                .loginPage("/login")
                .successHandler(new MyAuthenticationSuccessHandler())
                .permitAll()
            .and()
                .rememberMe()
                .tokenRepository(persistentTokenRepository())
                .rememberMeParameter("remember-me")
                .tokenValiditySeconds(24 * 60 * 60)
            .and()
                .logout()
                .logoutUrl("/logout")
                .permitAll()
            .and()
                .csrf()
                .disable();
    }

    @Autowired
    private DataSource dataSource;
    
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl persistentTokenRepository = new JdbcTokenRepositoryImpl();
        persistentTokenRepository.setDataSource(dataSource);
        // 第一次启动的时候自动建表（最好自己手动建表，源码中有语句的）
        // persistentTokenRepository.setCreateTableOnStartup(true);
        return persistentTokenRepository;
    }
}
