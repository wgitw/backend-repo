package com.madeyepeople.pocketpt.domain.account.controller;

import com.madeyepeople.pocketpt.domain.account.social.AccountPrincipal;
import com.madeyepeople.pocketpt.global.util.SecurityUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1")
@Slf4j
@RequiredArgsConstructor
public class LoginController {

    private final SecurityUtil securityUtil;

    @GetMapping("/main")
    public String goLogin(HttpServletRequest request) {

        return "메인 페이지 입니다!";
    }

    @GetMapping("/auth")
    public Long test() {
        Long id = securityUtil.getLoginUsername();
        return id;
    }

    @GetMapping("/done")
    public String done(@CookieValue String accessToken, @CookieValue String refreshToken) {
        return "accessToken: " + accessToken + "\n" + "refreshToken: " + refreshToken;
    }

    @GetMapping("/test-logout")
    public String logout() {
        return "로그아웃 되었습니다.";
    }

    @Value("${cors.frontend}")
    private String corsFrontend;

    @GetMapping("/cookie-test")
    public String cookieTest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Cookie cookie_refresh = new Cookie("hello", "san");
        cookie_refresh.setPath("/");
        cookie_refresh.setSecure(true);
        cookie_refresh.setAttribute("SameSite", "None");
        response.addCookie(cookie_refresh);

        log.error(cookie_refresh.getAttributes().toString());

        response.setHeader(HttpHeaders.AUTHORIZATION, "authorization test");
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, corsFrontend);
        response.sendRedirect(corsFrontend);

        return "test done";
    }

}
