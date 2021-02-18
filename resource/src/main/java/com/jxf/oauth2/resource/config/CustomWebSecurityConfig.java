package com.jxf.oauth2.resource.config;



import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


//
//@Configuration
//@EnableWebSecurity
//public class CustomWebSecurityConfig extends WebSecurityConfigurerAdapter {
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.csrf().disable()
//                .authorizeRequests()
//                .antMatchers("/r1").hasAuthority("p1")
//                .antMatchers("/r2").hasAuthority("p2")
//                .anyRequest().permitAll();
//    }
//}
