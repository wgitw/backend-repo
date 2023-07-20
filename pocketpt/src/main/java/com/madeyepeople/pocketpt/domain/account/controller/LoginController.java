package com.madeyepeople.pocketpt.domain.account.controller;

import com.madeyepeople.pocketpt.domain.account.social.AccountPrincipal;
import com.madeyepeople.pocketpt.global.util.SecurityUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class LoginController {

    private SecurityUtil securityUtil;

    @GetMapping("/main")
    public String goLogin(HttpServletRequest request){

        return "메인 페이지 입니다!";
    }

    @GetMapping("/auth")
    public String test(){
        String email = securityUtil.getLoginUsername();
        return email;
    }

    @GetMapping("/done")
    public String done(@CookieValue String accessToken, @CookieValue String refreshToken) {
        return "accessToken: " + accessToken + "\n" + "refreshToken: " + refreshToken;
    }
}
