package com.madeyepeople.pocketpt.domain.account.controller;

import com.madeyepeople.pocketpt.domain.account.dto.*;
import com.madeyepeople.pocketpt.domain.account.dto.request.*;
import com.madeyepeople.pocketpt.domain.account.dto.response.*;
import com.madeyepeople.pocketpt.domain.account.service.AccountService;
import com.madeyepeople.pocketpt.global.result.ResultCode;
import com.madeyepeople.pocketpt.global.result.ResultResponse;
import com.madeyepeople.pocketpt.global.util.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;


@Validated // path variable에 정규식을 사용하기 위해 추가
@RestController
@RequestMapping("/api/v1/account")
@Slf4j
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    private final SecurityUtil securityUtil;


    /**
     * 공통 API
     */
    // 2차 회원가입 여부 확인
    @GetMapping("/check/signup")
    public ResponseEntity<ResultResponse> checkSignup() {
        CheckAccountSignupResponse checkAccountSignupResponse = accountService.checkSignup();
        return ResponseEntity.ok(ResultResponse.of(ResultCode.ACCOUNT_CHECK_SIGNED_UP_SUCCESS, checkAccountSignupResponse));
    }

    // 2차 회원가입
    @PatchMapping("/signup/{role}")
    public ResponseEntity<ResultResponse> signup(@RequestBody @Valid CommonRegistrationRequest commonRegistrationRequest,
                                                 @PathVariable
                                                 @Pattern(
                                                         regexp = "^(trainer|trainee)$",
                                                         message = "role은 'trainer' 또는 'trainee'만 허용합니다.")
                                                 String role) {
        AccountRegistrationResponse accountRegistrationResponse = accountService.registerAccount(commonRegistrationRequest, role);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.ACCOUNT_CREATE_SUCCESS, accountRegistrationResponse));
    }

    @DeleteMapping("/logout")
    public ResponseEntity<ResultResponse> logout(HttpServletRequest httpServletRequest) {
        accountService.logout(httpServletRequest);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.ACCOUNT_LOGOUT_SUCCESS));
    }

    @DeleteMapping("/withdrawal")
    public ResponseEntity<ResultResponse> withdrawal(HttpServletRequest httpServletRequest) {
        WithdrawalResponse withdrawalResponse = accountService.withdrawal(httpServletRequest);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.ACCOUNT_WITHDRAWAL_SUCCESS, withdrawalResponse));
    }

    // 내 정보 상세 조회
    @GetMapping("/detail")
    public ResponseEntity<ResultResponse> getAccount() {
        AccountDetailGetResponse accountDetailGetResponse = accountService.getAccount();
        return ResponseEntity.ok(ResultResponse.of(ResultCode.ACCOUNT_GET_SUCCESS, accountDetailGetResponse));
    }

    // 내 정보 수정
    @PatchMapping("/detail")
    public ResponseEntity<ResultResponse> updateAccount(@RequestBody @Valid AccountUpdateRequest accountUpdateRequest) {
        AccountUpdateResponse accountUpdateResponse = accountService.updateIntroduce(accountUpdateRequest);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.ACCOUNT_INTRODUCE_UPDATE_SUCCESS, accountUpdateResponse));
    }

    // 내 프로필사진 수정
    @PatchMapping("/detail/profile-picture")
    public ResponseEntity<ResultResponse> updateProfilePicture(@ModelAttribute AccountProfilePictureUpdateRequest accountProfilePictureUpdateRequest) {

        AccountUpdateResponse accountUpdateResponse = accountService.updateProfilePicture(accountProfilePictureUpdateRequest);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.ACCOUNT_PROFILE_PICTURE_UPDATE_SUCCESS, accountUpdateResponse));
    }

    // 상대방 프로필 정보 조회
    @GetMapping("/profile/{accountId}")
    public ResponseEntity<ResultResponse> getProfile(@PathVariable Long accountId) {
        ProfileGetResponse profileGetResponse = accountService.getProfile(accountId);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.ACCOUNT_PROFILE_GET_SUCCESS, profileGetResponse));
    }

    // 트레이너의 월별 PT 단가 조회
    @GetMapping("/price")
    public ResponseEntity<ResultResponse> getTrainerPtPrice(@RequestParam String trainerCode) {
        MonthlyPtPriceGetResponse monthlyPtPriceGetResponse = accountService.getTrainerAllPtPrice(trainerCode);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.ACCOUNT_PT_PRICE_GET_SUCCESS, monthlyPtPriceGetResponse));
    }

    // 목표 CRUD
    @PostMapping("/purpose")
    public ResponseEntity<ResultResponse> createPurpose(@RequestBody @Valid PurposeCreateRequest purposeCreateRequest) {
        PurposeDto purposeDto = accountService.createPurpose(purposeCreateRequest);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.ACCOUNT_PURPOSE_CREATE_SUCCESS, purposeDto));
    }

    @GetMapping("/purpose/{accountId}")
    public ResponseEntity<ResultResponse> getPurpose(@PathVariable Long accountId) {
        List<PurposeDto> purposeDtoList = accountService.getPurpose(accountId);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.ACCOUNT_PURPOSE_GET_SUCCESS, purposeDtoList));
    }

    @PatchMapping("/purpose/{purposeId}")
    public ResponseEntity<ResultResponse> updatePurpose(@PathVariable Long purposeId,
                                                        @RequestBody PurposeUpdateRequest purposeUpdateRequest) {
        PurposeDto purposeDto = accountService.updatePurpose(purposeId, purposeUpdateRequest);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.ACCOUNT_PURPOSE_UPDATE_SUCCESS, purposeDto));
    }

    @DeleteMapping("/purpose/{purposeId}")
    public ResponseEntity<ResultResponse> deletePurpose(@PathVariable Long purposeId) {
        String deleteSuccessMessage = accountService.deletePurpose(purposeId);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.ACCOUNT_PURPOSE_DELETE_SUCCESS, deleteSuccessMessage));
    }

    // 체성분 정보 CRUD
    @PostMapping("/physical-info")
    public ResponseEntity<ResultResponse> createPhysicalInfo(@RequestBody PhysicalInfoCreateRequest physicalInfoCreateRequest) {
        PhysicalInfoDto physicalInfoDto = accountService.createPhysicalInfo(physicalInfoCreateRequest);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.ACCOUNT_PHYSICAL_INFO_CREATE_SUCCESS, physicalInfoDto));
    }

    @GetMapping("/physical-info/{accountId}")
    public ResponseEntity<ResultResponse> getPhysicalInfo(@PathVariable Long accountId) {
        List<PhysicalInfoDto> physicalInfoDtoList = accountService.getPhysicalInfo(accountId);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.ACCOUNT_PHYSICAL_INFO_GET_SUCCESS, physicalInfoDtoList));
    }

//    @PatchMapping("/physical-info/{physicalInfoId}")
//    public ResponseEntity<ResultResponse> updatePhysicalInfo(@PathVariable Long physicalInfoId,
//                                                             @RequestBody PhysicalInfoUpdateRequest physicalInfoUpdateRequest) {
//        PhysicalInfoDto physicalInfoDto = accountService.updatePhysicalInfo(physicalInfoId, physicalInfoUpdateRequest);
//        return ResponseEntity.ok(ResultResponse.of(ResultCode.ACCOUNT_PHYSICAL_INFO_UPDATE_SUCCESS, physicalInfoDto));
//    }


    /**
     * 트레이너용 API
     * 1. PT 단가 CUD
     * 2. 이력 CRUD
     * 3. 총/월별 매출 조회
     * 4. 순수익 조회
     */

    @PostMapping("/trainer/price")
    public ResponseEntity<ResultResponse> createTrainerMonthlyPtPrice(@RequestBody TrainerMonthlyPtPriceCreateAndUpdateRequest trainerMonthlyPtPriceCreateAndUpdateRequest) {
        MonthlyPtPriceDto monthlyPtPriceDto = accountService.createTrainerMonthlyPtPrice(trainerMonthlyPtPriceCreateAndUpdateRequest);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.ACCOUNT_TRAINER_MONTHLY_PT_PRICE_CREATE_SUCCESS, monthlyPtPriceDto));
    }

    @PatchMapping("/trainer/price/{ptPriceId}")
    public ResponseEntity<ResultResponse> updateTrainerMonthlyPtPrice(@PathVariable Long ptPriceId,
                                                                      @RequestBody TrainerMonthlyPtPriceCreateAndUpdateRequest trainerMonthlyPtPriceCreateAndUpdateRequest) {
        MonthlyPtPriceDto monthlyPtPriceDto = accountService.updateTrainerMonthlyPtPrice(ptPriceId, trainerMonthlyPtPriceCreateAndUpdateRequest);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.ACCOUNT_TRAINER_MONTHLY_PT_PRICE_UPDATE_SUCCESS, monthlyPtPriceDto));
    }

    @DeleteMapping("/trainer/price/{ptPriceId}")
    public ResponseEntity<ResultResponse> deleteTrainerMonthlyPtPrice(@PathVariable Long ptPriceId) {
        String deleteSuccessMessage = accountService.deleteTrainerMonthlyPtPrice(ptPriceId);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.ACCOUNT_TRAINER_MONTHLY_PT_PRICE_DELETE_SUCCESS, deleteSuccessMessage));
    }

    @PostMapping("/trainer/career")
    public ResponseEntity<ResultResponse> createTrainerCareer(@RequestBody TrainerCareerCreateRequest trainerCareerCreateRequest) {
        TrainerCareerCreateAndGetResponse trainerCareerCreateAndGetResponse = accountService.createTrainerCareer(trainerCareerCreateRequest);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.ACCOUNT_TRAINER_CAREER_CREATE_SUCCESS, trainerCareerCreateAndGetResponse));
    }

    @GetMapping("/trainer/career/{accountId}")
    public ResponseEntity<ResultResponse> getTrainerCareer(@PathVariable Long accountId) {
        TrainerCareerCreateAndGetResponse trainerCareerCreateAndGetResponse = accountService.getTrainerCareer(accountId);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.ACCOUNT_TRAINER_CAREER_GET_SUCCESS, trainerCareerCreateAndGetResponse));
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

    @GetMapping("rollback-signup")
    public String removeRole() {
        return accountService.removeRoleAndMonthlyPtPrice();
    }

    @GetMapping("/flush-redis")
    public String flushRedis() {
        return accountService.flushRedis();
    }
}
