package com.madeyepeople.pocketpt.domain.account.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class DefaultUrlController {
    @GetMapping("/")
    public String defaultPage() {
        return "OAuth 인증 후 redirect url을 입력하지 않아 server의 base url로 redirect 됩니다.";
    }
}
