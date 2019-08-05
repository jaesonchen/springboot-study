# 令牌存储
默认令牌是存储在内存中的，我们可以将它保存到第三方存储中，比如Redis。
    

## redis 存储
```
@Configuration
public class TokenStoreConfig {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Bean
    public TokenStore redisTokenStore (){
        return new RedisTokenStore(redisConnectionFactory);
    }
}

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private TokenStore redisTokenStore;

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints.authenticationManager(authenticationManager)
            .tokenStore(redisTokenStore);
    }

    ......
}
```
    

## JWT替换默认令牌
使用JWT替换默认的令牌（默认令牌使用UUID生成）只需要指定TokenStore为JwtTokenStore即可。
    
```
@Configuration
public class JWTokenConfig {

    @Bean
    public TokenStore jwtTokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
        accessTokenConverter.setSigningKey("test_key"); // 签名密钥
        return accessTokenConverter;
    }
}


@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
    @Autowired
    private TokenStore jwtTokenStore;
    @Autowired
    private JwtAccessTokenConverter jwtAccessTokenConverter;

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints.authenticationManager(authenticationManager)
                .tokenStore(jwtTokenStore)
                .accessTokenConverter(jwtAccessTokenConverter);
    }

    ......
}
```
   
系统将返回如下格式令牌：
    
```
{
    "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1NjE1MzI1MDEsInVzZXJfbmFtZSI6Im1yYmlyZCIsImF1dGhvcml0aWVzIjpbImFkbWluIl0sImp0aSI6IjJkZjY4MGNhLWFmN2QtNGU4Ni05OTdhLWI1ZmVkYzQxZmYwZSIsImNsaWVudF9pZCI6InRlc3QxIiwic2NvcGUiOltdfQ.dZ4SeuU3VWnSJKy5vELGQ0YkVRddcEydUlJAVovlycg",
    "token_type": "bearer",
    "expires_in": 3599,
    "scope": "all",
    "jti": "2df680ca-af7d-4e86-997a-b5fedc41ff0e"
}
```
    

## 拓展JWT
如果想在JWT中添加一些额外的信息，我们需要实现TokenEnhancer（Token增强器）：
    
```
// 我们在Token中添加了message: hello world信息
public class JWTokenEnhancer implements TokenEnhancer {
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken oAuth2AccessToken, OAuth2Authentication oAuth2Authentication) {
        Map<String, Object> info = new HashMap<>();
        info.put("message", "hello world");
        ((DefaultOAuth2AccessToken) oAuth2AccessToken).setAdditionalInformation(info);
        return oAuth2AccessToken;
    }
}


// 然后在JWTokenConfig里注册该Bean：
@Configuration
public class JWTokenConfig {
    ......

    @Bean
    public TokenEnhancer tokenEnhancer() {
        return new JWTokenEnhancer();
    }
}


// 最后在认证服务器里配置该增强器：
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private TokenStore jwtTokenStore;
    @Autowired
    private JwtAccessTokenConverter jwtAccessTokenConverter;
    @Autowired
    private TokenEnhancer tokenEnhancer;

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
        List<TokenEnhancer> enhancers = new ArrayList<>();
        enhancers.add(tokenEnhancer);
        enhancers.add(jwtAccessTokenConverter);
        enhancerChain.setTokenEnhancers(enhancers);

        endpoints.tokenStore(jwtTokenStore)
                .accessTokenConverter(jwtAccessTokenConverter)
                .tokenEnhancer(enhancerChain);
    }
    ......
}
```


