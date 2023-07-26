package com.madeyepeople.pocketpt.domain.account.controller;

import com.madeyepeople.pocketpt.global.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;


@RestController
@RequestMapping("/api/v1")
@Slf4j
@RequiredArgsConstructor
public class LoginController {

    private final SecurityUtil securityUtil;

    @GetMapping("/main")
    public String main() {
        return "메인 페이지 입니다!";
    }

    @GetMapping("/auth")
    public Long auth() {
        Long id = securityUtil.getLoginUsername();
        return id;
    }

    @GetMapping("/test-logout")
    public String logout() {
        return "로그아웃 되었습니다.";
    }
}
