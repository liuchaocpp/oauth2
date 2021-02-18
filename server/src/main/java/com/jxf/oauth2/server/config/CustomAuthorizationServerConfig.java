package com.jxf.oauth2.server.config;


import com.jxf.oauth2.server.service.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableAuthorizationServer //开启 安全服务器
public class CustomAuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private AuthorizationCodeServices authorizationCodeServices;

    @Autowired
    private JwtAccessTokenConverter accessTokenConverter;

    @Autowired
    private ClientDetailsService clientDetailsService;



    /**
     * 这个类主要是用于token令牌的管理
     *  1. 存储策略 比如 内存方式 jdbc方式 jwt方式
     *  2. 令牌生成策略 可以对令牌进行增强
     */
    @Bean
    public AuthorizationServerTokenServices authorizationServerTokenServices(){
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setClientDetailsService(clientDetailsService);
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setTokenStore(tokenStore);
        //令牌加强 使用jwt
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Collections.singletonList(accessTokenConverter));
        tokenServices.setTokenEnhancer(tokenEnhancerChain);

        return tokenServices;
    }


    /**
     * 基于内存的令牌存储
     */
//    @Bean
//    public TokenStore tokenStore(){
//        return new InMemoryTokenStore();
//    }

    /**
     * 基于内存的授权码模式的 code
     */
//    @Bean
//    public AuthorizationCodeServices authorizationCodeServices(){
//        return new InMemoryAuthorizationCodeServices();
//    }

    /**
     * 这个方法主要是用于授权模式扩展,默认只有四种
     * 但是如果由其他需求的话,比如 手机验证码登陆
     * 可以将自定义的AbstractTokenGranter 实现类注入进来
     */
    private TokenGranter tokenGranter(final AuthorizationServerEndpointsConfigurer endpoints) {
        List<TokenGranter> granters = new ArrayList<TokenGranter>(Collections.singletonList(endpoints.getTokenGranter()));// 获取默认的granter集合
        granters.add(new SmsCodeTokenGranter(authenticationManager,endpoints.getTokenServices(), endpoints.getClientDetailsService(), endpoints.getOAuth2RequestFactory()));
        return new CompositeTokenGranter(granters);
    }

    /**
     *  配置安全策略
     *
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                .allowFormAuthenticationForClients()  //允许表单验证
                .checkTokenAccess("permitAll()") //允许对 token令牌的 验证 远程模式需要
                .tokenKeyAccess("permitAll()"); // 非对称加密获取公钥
    }

    /**
     * 基于内存的客户端设置
     * http://localhost:8080/oauth/authorize?client_id=c1&response_type=code&scope=all&redirect_uri=https://www.baidu.com
     */
//    @Override
//    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//        clients.inMemory() // 基于内存
//                .withClient("c1") // 客户端 ID
//                .secret(passwordEncoder.encode("123456")) // 客户端密钥 并进行加密
//                .resourceIds("res1") // 资源服务id
//                .authorizedGrantTypes("authorization_code","password","implicit","client_credentials","refresh_token") // 允许的认证模式 可以扩展
//                .scopes("all") // 请求域
//                .autoApprove(false)
//                .redirectUris("https://www.baidu.com") // 授权模式请求code的回调地址
//                .accessTokenValiditySeconds(7200) // 令牌保存时间
//                .refreshTokenValiditySeconds(7200);// 刷新令牌时间
//
//    }

    /**
     * 基于数据库 客户端配置
     */

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetailsService);
    }

    @Bean
    public UserDetailsService userService(){
        return new CustomUserDetailService();
    }


    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {

        endpoints
                .authenticationManager(authenticationManager) //password 模式需要
                .authorizationCodeServices(authorizationCodeServices) // 授权码模式需要
                .tokenGranter(tokenGranter(endpoints)) // 扩展授权模式
                .tokenServices(authorizationServerTokenServices()) //token的服务策略
                .userDetailsService(userService()) //  刷新令牌需要使用
                .allowedTokenEndpointRequestMethods(HttpMethod.POST);

    }

}
