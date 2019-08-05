# 认证服务器配置
认证服务器作用就是作为统一令牌发放并校验的地方。
    
```
// Spring Security配置类
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin()
            .and()
                .authorizeRequests()
                .anyRequest()
                .authenticated();
    }
}


// 用户登录认证服务
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new User(username, passwordEncoder.encode("1234"), AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER"));
    }
}
```
    
认证服务器配置，Token使用JWT：
    
```
@Configuration
@EnableAuthorizationServer
public class SsoAuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserDetailService userDetailService;

    @Bean
    public TokenStore jwtTokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
        accessTokenConverter.setSigningKey("test_key");
        return accessTokenConverter;
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("app-a")
                .secret(passwordEncoder.encode("app-a-1234"))
                .authorizedGrantTypes("refresh_token","authorization_code")
                .accessTokenValiditySeconds(3600)
                .scopes("all")
                .autoApprove(true)
                .redirectUris("http://127.0.0.1:9090/")
            .and()
                .withClient("app-b")
                .secret(passwordEncoder.encode("app-b-1234"))
                .authorizedGrantTypes("refresh_token","authorization_code")
                .accessTokenValiditySeconds(7200)
                .scopes("all")
                .autoApprove(true)
                .redirectUris("http://127.0.0.1:9091/");
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints.tokenStore(jwtTokenStore())
                .accessTokenConverter(jwtAccessTokenConverter())
                .userDetailsService(userDetailService);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security.tokenKeyAccess("isAuthenticated()"); // 获取密钥需要身份认证
    }
}
```
    
这里分配了两个客户端配置，分别为app-a和app-b，因为使用默认的Spring Security登录页面来进行认证，所以需要开启authorization_code类型认证支持。
    


# 客户端配置
在客户端SpringBoot入口类上添加@EnableOAuth2Sso注解，开启SSO的支持。
    
```
@EnableOAuth2Sso
@SpringBootApplication
public class SsoApplicaitonOne {

    public static void main(String[] args) {
        new SpringApplicationBuilder(SsoApplicaitonOne.class).run(args);
    }
}

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {

}
```
    
配置文件application.yml：
    
```
security:
  oauth2:
    client:
      client-id: app-a
      client-secret: app-a-1234
      user-authorization-uri: http://127.0.0.1:8080/oauth/authorize
      access-token-uri: http://127.0.0.1:8080/oauth/token
    resource:
      jwt:
        key-uri: http://127.0.0.1:8080/oauth/token_key
```
    
`security.oauth2.client.client-id`和`security.oauth2.client.client-secret`指定了客户端id和密码，这里和认证服务器里配置的client一致；`user-authorization-uri`指定为认证服务器的`/oauth/authorize`地址，`access-token-uri`指定为认证服务器的`/oauth/token`地址，`jwt.key-uri`指定为认证服务器的`/oauth/token_key`地址。

















