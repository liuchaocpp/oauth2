package com.jxf.oauth2.server.service;


import com.jxf.oauth2.server.entity.CustomUserDetails;
import com.jxf.oauth2.server.entity.UserEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class CustomUserDetailService implements UserDetailsService {

    private final Map<String, UserEntity> userEntities=new HashMap<>();

    @PostConstruct
    private void initUserEntities(){
        userEntities.put("jack",UserEntity
                .builder().id(1L).username("jack")
                .password("$2a$10$tzZ6KTwmIXwGDJ6B0yzxxOOVHmwo1ul0.XbCc772SVDXm8VMAI6E2").description("jack love")
                .authorities(Collections.singletonList("p1"))
                .build());
        userEntities.put("tom",UserEntity
                .builder().id(2L).username("tom")
                .password("$2a$10$tzZ6KTwmIXwGDJ6B0yzxxOOVHmwo1ul0.XbCc772SVDXm8VMAI6E2").description("tom love")
                .authorities(Collections.singletonList("p2"))
                .build());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userEntities.get(username);
        if(userEntity==null){
            throw new UsernameNotFoundException("用户未找到");
        }
        CustomUserDetails customUserDetails = new CustomUserDetails();
        customUserDetails.setUserEntity(userEntity);
        customUserDetails.setAuthorities(userEntity.getAuthorities());
        return customUserDetails;
    }
}
