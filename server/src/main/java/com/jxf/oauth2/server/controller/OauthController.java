package com.jxf.oauth2.server.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OauthController {

    @GetMapping("/auth/redirect")
    public void authenticate(@RequestParam String code){
        System.out.println(code);
    }

    //https://www.baidu.com
    //http://localhost:8080/oauth/authorize?client_id=c1&response_type=code&scope=all&redirect_uri=https://www.baidu.com

    //https://b.com/oauth/authorize?
    //  response_type=code&
    //  client_id=CLIENT_ID&
    //  redirect_uri=CALLBACK_URL&
    //  scope=read
}
