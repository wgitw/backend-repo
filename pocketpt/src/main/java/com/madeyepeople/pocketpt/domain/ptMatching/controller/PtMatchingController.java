package com.madeyepeople.pocketpt.domain.ptMatching.controller;

import com.madeyepeople.pocketpt.domain.ptMatching.dto.request.PtRegistrationRequest;
import com.madeyepeople.pocketpt.domain.ptMatching.dto.response.PtRegistrationResponse;
import com.madeyepeople.pocketpt.domain.ptMatching.service.PtMatchingService;
import com.madeyepeople.pocketpt.global.result.ResultCode;
import com.madeyepeople.pocketpt.global.result.ResultResponse;
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

    @PostMapping
    public ResponseEntity<ResultResponse> registerPt(@RequestBody PtRegistrationRequest ptRegistrationRequest) {
        PtRegistrationResponse ptRegistrationResponse = ptMatchingService.registerPt(ptRegistrationRequest);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.PT_REGISTRATION_CREATE_SUCCESS, ptRegistrationResponse));
    }

//    @GetMapping("/requested")

    // TODO: 요청 수락
//    @GetMapping("/accept")

    // TODO: 매칭 목록 조회 -> 채팅 친구 목록 조회와 동일
//    @GetMapping

}
