package com.madeyepeople.pocketpt.domain.account.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class LoginController {

    @GetMapping("/main")
    public String goLogin(@RequestBody String requestBody){
        log.error(requestBody);
        String temp = requestBody.toString();
        log.error(temp);
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
