package com.madeyepeople.pocketpt.domain.chattingParticipant.controller;

import com.madeyepeople.pocketpt.domain.account.entity.Account;
import com.madeyepeople.pocketpt.domain.chattingParticipant.service.ChattingParticipantService;
import com.madeyepeople.pocketpt.global.result.ResultResponse;
import com.madeyepeople.pocketpt.global.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chatting/rooms/{chattingRoomId}/participants")
public class ChattingParticipantController {
    private final SecurityUtil securityUtil;
    private final ChattingParticipantService chattingParticipantService;

    // 채팅방 나가기
    @DeleteMapping
    public ResponseEntity<ResultResponse> exitChattingRoom(@PathVariable Long chattingRoomId) {
        Account account = securityUtil.getLoginAccountEntity();
        ResultResponse resultResponse = chattingParticipantService.exitChattingRoom(account, chattingRoomId);
        return ResponseEntity.ok(resultResponse);
    }
}
