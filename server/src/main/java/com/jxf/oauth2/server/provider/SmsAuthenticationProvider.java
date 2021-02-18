package com.jxf.oauth2.server.provider;


import com.jxf.oauth2.server.entity.UserEntity;
import com.jxf.oauth2.server.token.SmsAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.stream.Collectors;


/**
 *  验证 手机 和 短信是否 匹配
 *  从redis中取出前面请求发送的验证码 进行匹配
 *  匹配失败 就抛出异常
 *  匹配成功 就从数据中查出 用户的信息 封装成 UserDetails
 *  并通过它构造一个 SmsAuthenticationToken 表示认证成功
 */
@Component
public class SmsAuthenticationProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        SmsAuthenticationToken authenticationToken = (SmsAuthenticationToken) authentication;
        String mobile = (String) authenticationToken.getPrincipal();
        String code = authenticationToken.getCode();
        checkSmsCode(mobile,code);

        UserEntity userEntity = UserEntity
                .builder().id(2L).username("tom")
                .password("$2a$10$tzZ6KTwmIXwGDJ6B0yzxxOOVHmwo1ul0.XbCc772SVDXm8VMAI6E2").description("tom love")
                .authorities(Collections.singletonList("p2"))
                .build();

        SmsAuthenticationToken authenticationResult = new SmsAuthenticationToken(userEntity,
                userEntity.getAuthorities().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
        authenticationResult.setDetails(authenticationToken.getDetails());
        return authenticationResult;
    }

    private void checkSmsCode(String mobile, String code) {

        System.out.println("获取code");

        System.out.println("打印code========>"+code);

        if(!code.equals("123")) {
            throw new BadCredentialsException("输入的验证码不正确");
        }

    }


    @Override
    public boolean supports(Class<?> authentication) {
        return (SmsAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
