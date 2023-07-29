package com.madeyepeople.pocketpt.domain.chattingMessage.controller;

import com.madeyepeople.pocketpt.domain.chattingMessage.dto.request.ChattingFileCreateRequest;
import com.madeyepeople.pocketpt.domain.chattingMessage.dto.request.ChattingMessageContentCreateRequest;
import com.madeyepeople.pocketpt.domain.chattingMessage.service.ChattingMessageService;
import com.madeyepeople.pocketpt.global.result.ResultResponse;
import com.madeyepeople.pocketpt.global.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chatting/rooms/{chattingRoomId}/messages")
@Slf4j
public class ChattingMessageController {
    // 아래에서 사용되는 convertAndSend 를 사용하기 위해서 서언
    // convertAndSend 는 객체를 인자로 넘겨주면 자동으로 Message 객체로 변환 후 도착지로 전송한다.
    private final SimpMessageSendingOperations template;
    private final ChattingMessageService chattingMessageService;
    private final SecurityUtil securityUtil;

    // 채팅방 메시지 보내기
    @MessageMapping("/chatting/rooms/{chattingRoomId}") // MessageMapping은 RequestMapping의 영향을 받지 않는 듯함
    public void sendChattingMessage(@DestinationVariable Long chattingRoomId, ChattingMessageContentCreateRequest chattingMessageContentCreateRequest, StompHeaderAccessor headerAccessor) {
        String accountUsername = headerAccessor.getUser().getName();
        ResultResponse resultResponse = chattingMessageService.createChattingMessage(chattingMessageContentCreateRequest, chattingRoomId, accountUsername);
        template.convertAndSend("/sub/channel/" + chattingRoomId, resultResponse);
    }

    // 채팅방 파일 보내기
    @PostMapping("/files")
    public ResponseEntity<ResultResponse> createChattingFile(@PathVariable Long chattingRoomId, @ModelAttribute ChattingFileCreateRequest chattingRoomCreateRequest) {
        Long accountId = securityUtil.getLoginUsername();
        ResultResponse resultResponse = chattingMessageService.createChattingFile(chattingRoomCreateRequest, chattingRoomId, accountId);
        return ResponseEntity.ok(resultResponse);
    }

    // 채팅방 메시지 리스트 가져오기
    @GetMapping
    public  ResponseEntity<ResultResponse> getChattingMessageListByRoom(@PathVariable Long chattingRoomId) {
        ResultResponse resultResponse = chattingMessageService.getChattingMessageListByRoom(chattingRoomId);
        return ResponseEntity.ok(resultResponse);
    }

    // 채팅방 파일 리스트 가져오기
    @GetMapping("/files")
    public  ResponseEntity<ResultResponse> getChattingFileListByRoom(@PathVariable Long chattingRoomId) {
        ResultResponse resultResponse = chattingMessageService.getChattingFileListByRoom(chattingRoomId);
        return ResponseEntity.ok(resultResponse);
    }
}
