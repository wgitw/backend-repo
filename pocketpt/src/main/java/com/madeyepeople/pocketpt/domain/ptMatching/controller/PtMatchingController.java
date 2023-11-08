package com.madeyepeople.pocketpt.domain.ptMatching.controller;

import com.madeyepeople.pocketpt.domain.ptMatching.dto.TrainerPtMemoDto;
import com.madeyepeople.pocketpt.domain.ptMatching.dto.request.PaymentAmountGetRequest;
import com.madeyepeople.pocketpt.domain.ptMatching.dto.request.PtRegistrationRequest;
import com.madeyepeople.pocketpt.domain.ptMatching.dto.request.PtRejectionRequest;
import com.madeyepeople.pocketpt.domain.ptMatching.dto.request.TrainerPtMemoCreateRequest;
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

    // 결제 예상 금액 조회
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
    @PatchMapping("/trainer/accept/{ptMatchingId}")
    public ResponseEntity<ResultResponse> acceptPtMatching(@PathVariable Long ptMatchingId) {
        ResultResponse resultResponse = ptMatchingService.acceptPtMatching(ptMatchingId);
        return ResponseEntity.ok(resultResponse);
    }

    // 요청된 PT 거절
    @PatchMapping("/trainer/reject/{ptMatchingId}")
    public ResponseEntity<ResultResponse> rejectPtMatching(@PathVariable Long ptMatchingId,
                                                           @RequestBody PtRejectionRequest ptRejectionRequest) {
        ResultResponse resultResponse = ptMatchingService.rejectPtMatching(ptMatchingId, ptRejectionRequest);
        return ResponseEntity.ok(resultResponse);
    }

    // PT 메모 생성 (trainer only)
    @PostMapping("/trainer/memo/{ptMatchingId}")
    public ResponseEntity<ResultResponse> createTrainerPtMemo(@PathVariable Long ptMatchingId,
                                                              @RequestBody TrainerPtMemoCreateRequest trainerPtMemoCreateRequest) {
        TrainerPtMemoDto trainerPtMemoDto = ptMatchingService.createTrainerPtMemo(ptMatchingId, trainerPtMemoCreateRequest);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.PT_MATCHING_MEMO_CREATE_SUCCESS, trainerPtMemoDto));
    }

    // PT 메모 조회 (trainer only)
    @GetMapping("/trainer/memo/{ptMatchingId}")
    public ResponseEntity<ResultResponse> getTrainerPtMemo(@PathVariable Long ptMatchingId) {
        TrainerPtMemoDto trainerPtMemoDto = ptMatchingService.getTrainerPtMemo(ptMatchingId);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.PT_MATCHING_MEMO_GET_SUCCESS, trainerPtMemoDto));
    }

    // TODO: 기존 PT 매칭 연장 (trainer only)
//    @PatchMapping
}
