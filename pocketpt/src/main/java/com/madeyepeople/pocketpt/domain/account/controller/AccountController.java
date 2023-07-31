package com.madeyepeople.pocketpt.domain.account.controller;

import com.madeyepeople.pocketpt.domain.account.dto.request.CommonRegistrationRequest;
import com.madeyepeople.pocketpt.domain.account.dto.response.RegistrationResponse;
import com.madeyepeople.pocketpt.domain.account.service.AccountService;
import com.madeyepeople.pocketpt.global.result.ResultCode;
import com.madeyepeople.pocketpt.global.result.ResultResponse;
import com.madeyepeople.pocketpt.global.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;


@Validated
@RestController
@RequestMapping("/api/v1")
@Slf4j
@RequiredArgsConstructor
public class AccountController {

    private final SecurityUtil securityUtil;
    private final AccountService accountService;

    @PostMapping("/signup/{role}")
    public ResponseEntity<ResultResponse> signupTrainer(@RequestBody CommonRegistrationRequest commonRegistrationRequest,
                                                        @PathVariable String role) {
        RegistrationResponse registrationResponse = accountService.registerAccount(commonRegistrationRequest, role);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.ACCOUNT_CREATE_SUCCESS, registrationResponse));
    }


    /**
     * 테스트용 api
     */
    @GetMapping("/main")
    public String mainTest() {
        return "메인 페이지 입니다!";
    }

    @GetMapping("/auth")
    public Long authTest() {
        Long id = securityUtil.getLoginAccountId();
        return id;
    }

    @GetMapping("/test-logout")
    public String logoutTest() {
        return "로그아웃 되었습니다.";
    }
}
