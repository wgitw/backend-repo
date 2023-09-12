package com.madeyepeople.pocketpt.domain.ptMatching.controller;

import com.madeyepeople.pocketpt.domain.ptMatching.dto.request.PaymentAmountGetRequest;
import com.madeyepeople.pocketpt.domain.ptMatching.dto.request.PtRegistrationRequest;
import com.madeyepeople.pocketpt.domain.ptMatching.dto.response.PtRegistrationResponse;
import com.madeyepeople.pocketpt.domain.ptMatching.service.PtMatchingService;
import com.madeyepeople.pocketpt.global.result.ResultCode;
import com.madeyepeople.pocketpt.global.result.ResultResponse;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/matching")
@Slf4j
@RequiredArgsConstructor
public class PtMatchingController {

    private final PtMatchingService ptMatchingService;

    @PostMapping("/payment/amount")
    public ResponseEntity<ResultResponse> getExpectedPaymentAmount(@RequestBody PaymentAmountGetRequest paymentAmountGetRequest) {
        Integer expectedPaymentAmount = ptMatchingService.getExpectedPaymentAmount(paymentAmountGetRequest);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.PT_MATCHING_PAYMENT_AMOUNT_GET_SUCCESS, expectedPaymentAmount));
    }

    // 신규 PT 매칭 요청
    @PostMapping
    public ResponseEntity<ResultResponse> registerPt(@RequestBody PtRegistrationRequest ptRegistrationRequest) {
        PtRegistrationResponse ptRegistrationResponse = ptMatchingService.registerPt(ptRegistrationRequest);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.PT_REGISTRATION_CREATE_SUCCESS, ptRegistrationResponse));
    }

    // PT 매칭 리스트 조회
    @GetMapping
    public ResponseEntity<ResultResponse> getPtMatchingList(@RequestParam
                                                                @Pattern(regexp = "^(all|pending|active|expired)$",
                                                                        message = "mode 'all', 'pending', 'active', 'expired'만 허용합니다.")
                                                            String mode) {
        ResultResponse resultResponse = ptMatchingService.getPtMatchingList(mode);
        return ResponseEntity.ok(resultResponse);
    }

    // 요청된 PT 수락
    @GetMapping("/trainer/accept/{ptMatchingId}")
    public ResponseEntity<ResultResponse> acceptPtMatching(@PathVariable Long ptMatchingId) {
        ResultResponse resultResponse = ptMatchingService.acceptPtMatching(ptMatchingId);
        return ResponseEntity.ok(resultResponse);
    }

    // 요청된 PT 거절
//    @PatchMapping("/")

    // TODO: 기존 PT 매칭 연장 (trainer only)
//    @PatchMapping
}
