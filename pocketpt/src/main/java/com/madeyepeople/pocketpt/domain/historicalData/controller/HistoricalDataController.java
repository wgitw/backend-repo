package com.madeyepeople.pocketpt.domain.historicalData.controller;

import com.madeyepeople.pocketpt.domain.account.entity.Account;
import com.madeyepeople.pocketpt.domain.historicalData.constant.Scope;
import com.madeyepeople.pocketpt.domain.historicalData.dto.request.HistoricalDataCreateRequest;
import com.madeyepeople.pocketpt.domain.historicalData.dto.request.HistoricalDataFileCreateRequest;
import com.madeyepeople.pocketpt.domain.historicalData.dto.request.HistoricalDataFileUpdateRequest;
import com.madeyepeople.pocketpt.domain.historicalData.dto.request.HistoricalDataUpdateRequest;
import com.madeyepeople.pocketpt.domain.historicalData.service.HistoricalDataService;
import com.madeyepeople.pocketpt.global.result.ResultResponse;
import com.madeyepeople.pocketpt.global.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/historical-data")
@RequiredArgsConstructor
@Slf4j
public class HistoricalDataController {
    private final SecurityUtil securityUtil;
    private final HistoricalDataService historicalDataService;

    @PostMapping
    public ResponseEntity<ResultResponse> createHistoricalData(@ModelAttribute HistoricalDataCreateRequest historicalDataCreateRequest) {
        log.info(historicalDataCreateRequest.toString());
        System.out.println(Scope.valueOf(historicalDataCreateRequest.getScope()));
        Account account = securityUtil.getLoginAccountEntity();
        ResultResponse resultResponse = historicalDataService.createHistoricalData(account, historicalDataCreateRequest);
        return ResponseEntity.ok(resultResponse);
    }

    // 단일 파일 업로드
    @PostMapping("/{historicalDataId}/file")
    public ResponseEntity<ResultResponse> createHistoricalDataFile(@PathVariable Long historicalDataId,
                                                                   @ModelAttribute HistoricalDataFileCreateRequest historicalDataFileCreateRequest) {
        Account account = securityUtil.getLoginAccountEntity();
        ResultResponse resultResponse = historicalDataService.createHistoricalDataFile(account, historicalDataId, historicalDataFileCreateRequest);
        return ResponseEntity.ok(resultResponse);
    }

    // TODO: 페이지네이션 추가
    @GetMapping
    public ResponseEntity<ResultResponse> getHistoricalDataListForTrainee(@RequestParam(required = true) String startDate,
                                                                          @RequestParam(required = true) String endDate) {
        Account account = securityUtil.getLoginAccountEntity();
        ResultResponse resultResponse = historicalDataService.getHistoricalDataListForTrainee(account, startDate, endDate);
        return ResponseEntity.ok(resultResponse);
    }

    // TODO: 페이지네이션 추가
    @GetMapping("/trainer/{trainerId}/trainee/{traineeId}")
    public ResponseEntity<ResultResponse> getHistoricalDataListByTraineeForTrainer(@PathVariable Long trainerId,
                                                                                   @PathVariable Long traineeId,
                                                                                   @RequestParam(required = true) String startDate,
                                                                                   @RequestParam(required = true) String endDate) {
        Account account = securityUtil.getLoginAccountEntity();
        ResultResponse resultResponse = historicalDataService.getHistoricalDataListByTraineeForTrainer(account, trainerId, traineeId, startDate, endDate);
        return ResponseEntity.ok(resultResponse);
    }

    @PatchMapping("/{historicalDataId}")
    public ResponseEntity<ResultResponse> updateHistoricalData(@PathVariable Long historicalDataId,
                                                               @RequestBody HistoricalDataUpdateRequest historicalDataUpdateRequest) {
        Account account = securityUtil.getLoginAccountEntity();
        ResultResponse resultResponse = historicalDataService.updateHistoricalData(account, historicalDataId, historicalDataUpdateRequest);
        return ResponseEntity.ok(resultResponse);
    }

    @PatchMapping("/{historicalDataId}/file/{historicalDataFileId}")
    public ResponseEntity<ResultResponse> updateHistoricalDataFile(@PathVariable Long historicalDataId,
                                                                   @PathVariable Long historicalDataFileId,
                                                                   @RequestBody HistoricalDataFileUpdateRequest historicalDataFileUpdateRequest) {
        Account account = securityUtil.getLoginAccountEntity();
        ResultResponse resultResponse = historicalDataService.updateHistoricalDataFile(account, historicalDataId, historicalDataFileId, historicalDataFileUpdateRequest);
        return ResponseEntity.ok(resultResponse);
    }

    @DeleteMapping("/{historicalDataId}")
    public ResponseEntity<ResultResponse> deleteHistoricalData(@PathVariable Long historicalDataId) {
        Account account = securityUtil.getLoginAccountEntity();
        ResultResponse resultResponse = historicalDataService.deleteHistoricalData(account, historicalDataId);
        return ResponseEntity.ok(resultResponse);
    }

    @DeleteMapping("/{historicalDataId}/file/{historicalDataFileId}")
    public ResponseEntity<ResultResponse> deleteHistoricalDataFile(@PathVariable Long historicalDataId,
                                                                   @PathVariable Long historicalDataFileId) {
        Account account = securityUtil.getLoginAccountEntity();
        ResultResponse resultResponse = historicalDataService.deleteHistoricalDataFile(account, historicalDataId, historicalDataFileId);
        return ResponseEntity.ok(resultResponse);
    }
}
