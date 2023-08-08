package com.madeyepeople.pocketpt.domain.chattingRoom.controller;

import com.madeyepeople.pocketpt.domain.account.entity.Account;
import com.madeyepeople.pocketpt.domain.account.service.AccountService;
import com.madeyepeople.pocketpt.domain.chattingRoom.dto.request.ChattingRoomCreateRequest;
import com.madeyepeople.pocketpt.domain.chattingRoom.service.ChattingRoomService;
import com.madeyepeople.pocketpt.global.result.ResultResponse;
import com.madeyepeople.pocketpt.global.util.SecurityUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/chatting/rooms")
public class ChattingRoomController {

    private final SecurityUtil securityUtil;
    private final SimpMessageSendingOperations template;
    private final ChattingRoomService chattingRoomService;
    private final AccountService accountService;

    //  채팅방 만들기
    @PostMapping
    public ResponseEntity<ResultResponse> createChattingRoom(@RequestBody ChattingRoomCreateRequest chattingRoomCreateRequest,
                                                             RedirectAttributes rttr) {
        Long hostAccountId = securityUtil.getLoginAccountId();
        ResultResponse resultResponse = chattingRoomService.createChattingRoom(hostAccountId, chattingRoomCreateRequest);
        rttr.addFlashAttribute("roomName", "roomName");
        return ResponseEntity.ok(resultResponse);
    }

    // 채팅방 리스트 가져오기 - 회원 ID 기준
    @GetMapping("/accounts/{accountId}")
    public ResponseEntity<ResultResponse> getChattingRoomListByUser(@PathVariable Long accountId) {
        ResultResponse resultResponse = chattingRoomService.getChattingRoomListByUser(accountId);
        return ResponseEntity.ok(resultResponse);
    }

    // 채팅방 리스트 변경사항 전송
    @MessageMapping("/chatting/rooms/{chattingRoomId}/messages/{latestChattingMessageId}")
    @Transactional
    public void updateChattingRoomInfo(@DestinationVariable Long chattingRoomId, @DestinationVariable Long latestChattingMessageId, StompHeaderAccessor headerAccessor) {
        String accountUsername = headerAccessor.getUser().getName();
        Account account = securityUtil.getLoginAccountEntity(accountUsername);
        ResultResponse resultResponse = chattingRoomService.updateChattingRoomInfo(account, chattingRoomId, latestChattingMessageId);
        template.convertAndSend("/sub/accounts/" + account.getAccountId(), resultResponse);
    }

}
