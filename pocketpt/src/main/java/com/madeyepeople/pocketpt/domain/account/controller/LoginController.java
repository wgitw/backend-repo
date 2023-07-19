package com.madeyepeople.pocketpt.domain.account.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class LoginController {

    @GetMapping("/main")
    public String goLogin(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();

        String temp = "";

        for(Cookie c : cookies) {
            temp += (c.getName())+" : ";  // 쿠키 이름 가져오기
            temp += (c.getValue())+"\n";  // 쿠키 값 가져오기
        }
        return temp;
    }

    @GetMapping("/auth")
    public String test(){
        return "JWT 토큰 검증 통과";
    }

    @GetMapping("/done")
    public String done(@CookieValue String accessToken, @CookieValue String refreshToken) {
        return "accessToken: " + accessToken + "\n" + "refreshToken: " + refreshToken;
    }
}
