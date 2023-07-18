package com.madeyepeople.pocketpt.domain.chattingMessage.controller;

import com.madeyepeople.pocketpt.domain.chattingMessage.dto.request.ChattingFileCreateRequest;
import com.madeyepeople.pocketpt.domain.chattingMessage.dto.request.ChattingMessageCreateRequest;
import com.madeyepeople.pocketpt.domain.chattingMessage.service.ChattingMessageService;
import com.madeyepeople.pocketpt.global.result.ResultResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chatting")
@Slf4j
public class ChattingMessageController {
    // 아래에서 사용되는 convertAndSend 를 사용하기 위해서 서언
    // convertAndSend 는 객체를 인자로 넘겨주면 자동으로 Message 객체로 변환 후 도착지로 전송한다.
    private final SimpMessageSendingOperations template;

    private final ChattingMessageService chattingMessageService;

    @MessageMapping("/chatting/messages") // MessageMapping은 RequestMapping의 영향을 받지 않는 듯함
    public void sendChattingMessage(ChattingMessageCreateRequest chattingMessageCreateRequest) {
        Map<String, Object> map = chattingMessageService.createChattingMessage(chattingMessageCreateRequest);
        ResultResponse resultResponse = (ResultResponse) map.get("resultResponse");
        Long chattingRoomId = (Long) map.get("chattingRoomId");
        template.convertAndSend("/sub/channel/" + chattingRoomId, resultResponse);
    }

    @PostMapping("/files")
    public ResponseEntity<ResultResponse> createChattingFile(@ModelAttribute ChattingFileCreateRequest chattingRoomCreateRequest) {
        ResultResponse resultResponse = chattingMessageService.createChattingFile(chattingRoomCreateRequest);
        return ResponseEntity.ok(resultResponse);
    }

    //    // 유저 퇴장 시에는 EventListener 을 통해서 유저 퇴장을 확인
    @EventListener
    public void webSocketDisconnectListener(SessionDisconnectEvent event) {
//        log.info("DisConnEvent {}", event);
//
//        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
//
//        // stomp 세션에 있던 uuid 와 roomId 를 확인해서 채팅방 유저 리스트와 room 에서 해당 유저를 삭제
//        String userUUID = (String) headerAccessor.getSessionAttributes().get("userUUID");
//        String roomId = (String) headerAccessor.getSessionAttributes().get("roomId");
//
//        log.info("headAccessor {}", headerAccessor);
//
//        // 채팅방 유저 -1
//        repository.minusUserCnt(roomId);
//
//        // 채팅방 유저 리스트에서 UUID 유저 닉네임 조회 및 리스트에서 유저 삭제
//        String username = repository.getUserName(roomId, userUUID);
//        repository.delUser(roomId, userUUID);

//        if (username != null) {
//            log.info("User Disconnected : " + username);
//
//            // builder 어노테이션 활용
//            ChatDTO chat = ChatDTO.builder()
//                    .type(ChatDTO.MessageType.LEAVE)
//                    .sender(username)
//                    .message(username + " 님 퇴장!!")
//                    .build();
//
//            template.convertAndSend("/sub/chat/room/" + roomId, chat);
//        }
    }

}
