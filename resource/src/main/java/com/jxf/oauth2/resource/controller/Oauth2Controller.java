package com.jxf.oauth2.resource.controller;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Oauth2Controller {

    @GetMapping("/r1")
//    @PreAuthorize("hasAuthority('p5')")
    public String auth1(){
        return "访问资源1";
    }


    @GetMapping("/r2")
//    @PreAuthorize("hasAuthority('p5')")
    public String auth2(){
        return "访问资源2";
    }
}
