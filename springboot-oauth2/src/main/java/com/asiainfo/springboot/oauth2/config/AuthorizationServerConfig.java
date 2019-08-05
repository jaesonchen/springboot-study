package com.asiainfo.springboot.oauth2.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年7月30日 下午1:56:26
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    // 支持 password grant type
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private PasswordEncoder encoder;
    
    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer
                .realm(ResourceServerConfig.RESOURCE_ID)
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()")
                .allowFormAuthenticationForClients();
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager);
        endpoints.allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);
    }
    
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        
        clients.inMemory()
                .withClient("aiqiyi")
                .secret(encoder.encode("secret"))
                .resourceIds(ResourceServerConfig.RESOURCE_ID)
                .authorizedGrantTypes("authorization_code", "refresh_token", "password")
                .authorities("ROLE_USER")
                .scopes("all")
                .redirectUris("http://baidu.com")
                .accessTokenValiditySeconds(3600)
                .refreshTokenValiditySeconds(3600 * 24);
    }
}
