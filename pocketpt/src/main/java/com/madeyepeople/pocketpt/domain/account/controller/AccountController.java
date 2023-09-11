package com.madeyepeople.pocketpt.domain.account.controller;

import com.madeyepeople.pocketpt.domain.account.dto.CareerDto;
import com.madeyepeople.pocketpt.domain.account.dto.CareerUpdateDto;
import com.madeyepeople.pocketpt.domain.account.dto.request.CommonRegistrationRequest;
import com.madeyepeople.pocketpt.domain.account.dto.request.TrainerCareerCreateRequest;
import com.madeyepeople.pocketpt.domain.account.dto.request.TrainerIncomeGetRequest;
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
        TrainerCareerCreateAndGetResponse trainerCareerCreateAndGetResponse = accountService.createTrainerCareer(trainerCareerCreateRequest);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.ACCOUNT_TRAINER_CAREER_CREATE_SUCCESS, trainerCareerCreateAndGetResponse));
    }

    @GetMapping("/trainer/career")
    public ResponseEntity<ResultResponse> getTrainerCareer() {
        TrainerCareerCreateAndGetResponse trainerCareerGetResponse = accountService.getTrainerCareer();
        return ResponseEntity.ok(ResultResponse.of(ResultCode.ACCOUNT_TRAINER_CAREER_GET_SUCCESS, trainerCareerGetResponse));
    }

    @PatchMapping("/trainer/career/{careerId}")
    public ResponseEntity<ResultResponse> updateTrainerCareer(@RequestBody CareerUpdateDto careerUpdateDto,
                                                              @PathVariable Long careerId) {
        CareerDto careerDto = accountService.updateTrainerCareer(careerId, careerUpdateDto);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.ACCOUNT_TRAINER_CAREER_UPDATE_SUCCESS, careerDto));
    }

    @DeleteMapping("/trainer/career/{careerId}")
    public ResponseEntity<ResultResponse> deleteTrainerCareer(@PathVariable Long careerId) {
        accountService.deleteTrainerCareer(careerId);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.ACCOUNT_TRAINER_CAREER_DELETE_SUCCESS, "deleted careerId = " + careerId));
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

    //  서비스 수수료 2가지 정책(정액 정책, 매출비례정책)에 따라 트레이너의 순수익을 조회하는 API
    @GetMapping("/trainer/income/{plan}")
    public ResponseEntity<ResultResponse> getTrainerTotalIncome(@RequestBody TrainerIncomeGetRequest trainerIncomeGetRequest,
                                                                @PathVariable @Pattern(regexp = "^(fixed|relative)$",
                                                                        message = "plan은 'fixed' 또는 'relative'만 허용합니다.")
                                                                        String plan) {
        TrainerIncomeGetResponse trainerIncomeGetResponse = accountService.getTrainerIncome(trainerIncomeGetRequest.getSales(), plan);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.ACCOUNT_INCOME_GET_SUCCESS, trainerIncomeGetResponse));
    }

    // @GetMapping("/trainer/income/monthly")

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
