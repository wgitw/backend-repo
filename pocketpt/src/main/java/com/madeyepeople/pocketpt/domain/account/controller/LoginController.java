package com.madeyepeople.pocketpt.domain.account.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @GetMapping("/main")
    public String goLogin(){
        return "여기는 main 페이지 입니다.";
    }

    @GetMapping("/test")
    public String test(){
        return "테스트 페이지 입니다.";
    }
}
