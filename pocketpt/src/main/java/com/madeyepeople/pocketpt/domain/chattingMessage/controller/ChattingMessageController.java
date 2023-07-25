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
@RequestMapping("/api/v1/chatting")
@Slf4j
public class ChattingMessageController {
    // 아래에서 사용되는 convertAndSend 를 사용하기 위해서 서언
    // convertAndSend 는 객체를 인자로 넘겨주면 자동으로 Message 객체로 변환 후 도착지로 전송한다.
    private final SimpMessageSendingOperations template;
    private final ChattingMessageService chattingMessageService;
    private final SecurityUtil securityUtil;

    @MessageMapping("/chatting/{roomId}") // MessageMapping은 RequestMapping의 영향을 받지 않는 듯함
    public void sendChattingMessage(@DestinationVariable Long roomId, ChattingMessageContentCreateRequest chattingMessageContentCreateRequest, StompHeaderAccessor headerAccessor) {
        String accountUsername = headerAccessor.getUser().getName();
        ResultResponse resultResponse = chattingMessageService.createChattingMessage(chattingMessageContentCreateRequest, roomId, accountUsername);
        template.convertAndSend("/sub/channel/" + roomId, resultResponse);
    }

    @PostMapping("/{roomId}/files")
    public ResponseEntity<ResultResponse> createChattingFile(@PathVariable Long roomId, @ModelAttribute ChattingFileCreateRequest chattingRoomCreateRequest) {
        Long accountId = securityUtil.getLoginUsername();
        ResultResponse resultResponse = chattingMessageService.createChattingFile(chattingRoomCreateRequest, roomId, accountId);
        return ResponseEntity.ok(resultResponse);
    }

}
