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
    @GetMapping("/accounts")
    public ResponseEntity<ResultResponse> getChattingRoomListByUser() {
        Long accountId = securityUtil.getLoginAccountId();
        ResultResponse resultResponse = chattingRoomService.getChattingRoomListByUser(accountId);
        return ResponseEntity.ok(resultResponse);
    }

    // 채팅방 리스트 변경사항 전송 - 새로운 메시지
    @MessageMapping("/chatting/rooms/{chattingRoomId}/messages/{latestChattingMessageId}")
    @Transactional
    public void updateChattingRoomInfoForNewMessage(@DestinationVariable Long chattingRoomId, @DestinationVariable Long latestChattingMessageId, StompHeaderAccessor headerAccessor) {
        String accountUsername = headerAccessor.getUser().getName();
        Account account = securityUtil.getLoginAccountEntity(accountUsername);
        ResultResponse resultResponse = chattingRoomService.updateChattingRoomInfoForNewMessage(account, chattingRoomId, latestChattingMessageId);
        template.convertAndSend("/sub/accounts/" + account.getAccountId(), resultResponse);
    }

    // 채팅방 리스트 변경사항 전송 - 새로운 방 생성
    @MessageMapping("/chatting/rooms/{chattingRoomId}/accounts/{hostAccountId}")
    @Transactional
    public void updateChattingRoomInfoForNewRoom(@DestinationVariable Long chattingRoomId, @DestinationVariable Long hostAccountId, StompHeaderAccessor headerAccessor) {
        String accountUsername = headerAccessor.getUser().getName();
        Account account = securityUtil.getLoginAccountEntity(accountUsername);
        ResultResponse resultResponse = chattingRoomService.updateChattingRoomInfoForNewRoom(account, chattingRoomId, hostAccountId);
        template.convertAndSend("/sub/accounts/" + account.getAccountId(), resultResponse);
    }

    // 채팅방 삭제
    @DeleteMapping("/{chattingRoomId}")
    public ResponseEntity<ResultResponse> deleteChattingRoom(@PathVariable Long chattingRoomId) {
        Account account = securityUtil.getLoginAccountEntity();
        ResultResponse resultResponse = chattingRoomService.deleteChattingRoom(account, chattingRoomId);
        return ResponseEntity.ok(resultResponse);
    }

    // 채팅방 상단고정 생성
    @PostMapping("/{chattingRoomId}/top")
    public ResponseEntity<ResultResponse> createTopChattingRoom(@PathVariable Long chattingRoomId) {
        Account account = securityUtil.getLoginAccountEntity();
        ResultResponse resultResponse = chattingRoomService.createTopChattingRoom(account, chattingRoomId);
        return ResponseEntity.ok(resultResponse);
    }

    // 채팅방 상단고정 리스트 조회
    @GetMapping("/top")
    public ResponseEntity<ResultResponse> getTopChattingRoomList() {
        Account account = securityUtil.getLoginAccountEntity();
        ResultResponse resultResponse = chattingRoomService.getTopChattingRoomList(account);
        return ResponseEntity.ok(resultResponse);
    }

    // 채팅방 상단고정 삭제
    @DeleteMapping("/{chattingRoomId}/top")
    public ResponseEntity<ResultResponse> deleteTopChattingRoom(@PathVariable Long chattingRoomId) {
        Account account = securityUtil.getLoginAccountEntity();
        ResultResponse resultResponse = chattingRoomService.deleteTopChattingRoom(account, chattingRoomId);
        return ResponseEntity.ok(resultResponse);
    }

}
