package com.madeyepeople.pocketpt.domain.account.controller;

import com.madeyepeople.pocketpt.domain.account.dto.MonthlyPtPriceDto;
import com.madeyepeople.pocketpt.domain.account.dto.request.CommonRegistrationRequest;
import com.madeyepeople.pocketpt.domain.account.dto.response.AccountDetailGetResponse;
import com.madeyepeople.pocketpt.domain.account.dto.response.AccountRegistrationResponse;
import com.madeyepeople.pocketpt.domain.account.dto.response.CheckAccountSignupResponse;
import com.madeyepeople.pocketpt.domain.account.dto.response.MonthlyPtPriceGetResponse;
import com.madeyepeople.pocketpt.domain.account.service.AccountService;
import com.madeyepeople.pocketpt.global.result.ResultCode;
import com.madeyepeople.pocketpt.global.result.ResultResponse;
import com.madeyepeople.pocketpt.global.util.SecurityUtil;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;


@Validated
@RestController
@RequestMapping("/api/v1/account")
@Slf4j
@RequiredArgsConstructor
public class AccountController {

    private final SecurityUtil securityUtil;
    private final AccountService accountService;

    @GetMapping("/check/signup")
    public ResponseEntity<ResultResponse> checkSignup() {
        CheckAccountSignupResponse checkAccountSignupResponse = accountService.checkSignup();
        return ResponseEntity.ok(ResultResponse.of(ResultCode.ACCOUNT_CHECK_SIGNED_UP_SUCCESS, checkAccountSignupResponse));
    }

    @PostMapping("/{role}")
    public ResponseEntity<ResultResponse> signup(@RequestBody @Valid CommonRegistrationRequest commonRegistrationRequest,
                                                        @PathVariable
                                                        @Pattern(
                                                                regexp = "^(trainer|trainee)$",
                                                                message = "role은 'trainer' 또는 'trainee'만 허용합니다.")
                                                        String role) {
        AccountRegistrationResponse accountRegistrationResponse = accountService.registerAccount(commonRegistrationRequest, role);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.ACCOUNT_CREATE_SUCCESS, accountRegistrationResponse));
    }

    @GetMapping("/detail")
    public ResponseEntity<ResultResponse> getAccount() {
        AccountDetailGetResponse accountDetailGetResponse = accountService.getAccount();
        return ResponseEntity.ok(ResultResponse.of(ResultCode.ACCOUNT_GET_SUCCESS, accountDetailGetResponse));
    }

    @GetMapping("/price")
    public ResponseEntity<ResultResponse> getAccountPrice(@RequestParam String trainerCode) {
        MonthlyPtPriceGetResponse monthlyPtPriceGetResponse = accountService.getPtPrice(trainerCode);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.ACCOUNT_PT_PRICE_GET_SUCCESS, monthlyPtPriceGetResponse));
    }

    // TODO: 회원정보 간단 조회
//    @GetMapping("/summary")


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
