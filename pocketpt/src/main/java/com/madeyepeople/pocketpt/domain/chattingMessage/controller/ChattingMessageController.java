package com.madeyepeople.pocketpt.domain.chattingMessage.controller;

import com.madeyepeople.pocketpt.domain.chattingMessage.dto.ChatDTO;
import com.madeyepeople.pocketpt.domain.chattingMessage.dto.request.ChattingMessageCreateRequest;
import com.madeyepeople.pocketpt.domain.chattingMessage.dto.response.ChattingMessageResponse;
import com.madeyepeople.pocketpt.domain.chattingMessage.entity.ChattingMessage;
import com.madeyepeople.pocketpt.domain.chattingMessage.repository.ChatRepository;
import com.madeyepeople.pocketpt.domain.chattingMessage.service.ChattingMessageService;
import com.madeyepeople.pocketpt.domain.chattingRoom.dto.response.ChattingRoomResponse;
import com.madeyepeople.pocketpt.domain.chattingRoom.entity.ChattingRoom;
import com.madeyepeople.pocketpt.global.result.ResultResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.ArrayList;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chatting-message")
public class ChattingMessageController {
    // 아래에서 사용되는 convertAndSend 를 사용하기 위해서 서언
    // convertAndSend 는 객체를 인자로 넘겨주면 자동으로 Message 객체로 변환 후 도착지로 전송한다.
    private final SimpMessageSendingOperations template;

    private final ChattingMessageService chattingMessageService;

    @MessageMapping("/chatting-message") // MessageMapping은 RequestMapping의 영향을 받지 않는 듯함
    public void sendChattingMessage(ChattingMessageCreateRequest chattingMessageCreateRequest) {
        Map<String, Object> map = chattingMessageService.createChattingMessage(chattingMessageCreateRequest);
        ResultResponse resultResponse = (ResultResponse) map.get("resultResponse");
        Long chattingRoomId = (Long) map.get("chattingRoomId");
        template.convertAndSend("/sub/channel/" + chattingRoomId, resultResponse);
    }

}
