package com.madeyepeople.pocketpt.domain.account.controller;

import com.madeyepeople.pocketpt.domain.account.dto.request.CommonRegistrationRequest;
import com.madeyepeople.pocketpt.domain.account.dto.request.TrainerCareerCreateRequest;
import com.madeyepeople.pocketpt.domain.account.dto.response.*;
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


@Validated
@RestController
@RequestMapping("/api/v1/account")
@Slf4j
@RequiredArgsConstructor
public class AccountController {

    private final SecurityUtil securityUtil;
    private final AccountService accountService;

    /**
     * 공통 API
     */
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
    public ResponseEntity<ResultResponse> getTrainerPtPrice(@RequestParam String trainerCode) {
        MonthlyPtPriceGetResponse monthlyPtPriceGetResponse = accountService.getTrainerPtPrice(trainerCode);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.ACCOUNT_PT_PRICE_GET_SUCCESS, monthlyPtPriceGetResponse));
    }

    // TODO: 회원정보 간단 조회
//    @GetMapping("/summary")

    /**
     * 트레이너용 API
     */
    @PostMapping("/trainer/career")
    public ResponseEntity<ResultResponse> createTrainerCareer(@RequestBody TrainerCareerCreateRequest trainerCareerCreateRequest) {
        TrainerCareerCreateResponse trainerCareerCreateResponse = accountService.createTrainerCareer(trainerCareerCreateRequest);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.ACCOUNT_TRAINER_CAREER_CREATE_SUCCESS, trainerCareerCreateResponse));
    }

    @GetMapping("/trainer/sales/total")
    public ResponseEntity<ResultResponse> getTrainerTotalSales() {
        TrainerTotalSalesGetResponse trainerTotalSalesGetResponse = accountService.getTrainerTotalSales();
        return ResponseEntity.ok(ResultResponse.of(ResultCode.ACCOUNT_TOTAL_SALES_GET_SUCCESS, trainerTotalSalesGetResponse));
    }

    @GetMapping("/trainer/sales/monthly")
    public ResponseEntity<ResultResponse> getTrainerMonthlySales(@RequestParam Integer year,
                                                                 @RequestParam Integer month) {
        TrainerTotalSalesGetResponse trainerMonthlySalesGetResponse = accountService.getTrainerMonthlySales(year, month);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.ACCOUNT_MONTHLY_SALES_GET_SUCCESS, trainerMonthlySalesGetResponse));
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
