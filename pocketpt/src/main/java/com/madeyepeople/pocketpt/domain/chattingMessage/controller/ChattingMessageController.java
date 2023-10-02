package com.madeyepeople.pocketpt.domain.chattingMessage.controller;

import com.madeyepeople.pocketpt.domain.chattingMessage.dto.request.ChattingFileCreateRequest;
import com.madeyepeople.pocketpt.domain.chattingMessage.dto.request.ChattingMessageContentCreateRequest;
import com.madeyepeople.pocketpt.domain.chattingMessage.service.ChattingMessageService;
import com.madeyepeople.pocketpt.global.result.ResultCode;
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

    @MessageMapping("/chatting/rooms/{chattingRoomId}/enter") // MessageMapping은 RequestMapping의 영향을 받지 않는 듯함
    public void sendChattingMessageEnter(@DestinationVariable Long chattingRoomId, StompHeaderAccessor headerAccessor) {
        String accountUsername = headerAccessor.getUser().getName();
        ResultResponse resultResponse = new ResultResponse(ResultCode.CHATTING_ROOM_ENTER_SUCCESS, accountUsername+ " enter");
        template.convertAndSend("/sub/channel/" + chattingRoomId, resultResponse);
    }

    // 채팅방 메시지 보내기
    @MessageMapping("/chatting/rooms/{chattingRoomId}") // MessageMapping은 RequestMapping의 영향을 받지 않는 듯함
    public void sendChattingMessage(@DestinationVariable Long chattingRoomId, ChattingMessageContentCreateRequest chattingMessageContentCreateRequest, StompHeaderAccessor headerAccessor) {
        log.info("=======================");
        log.info("CHATTING-MESSAGE-CONTROLLER: [sendChattingMessage] START");
        log.info("CHATTING-MESSAGE-CONTROLLER: [sendChattingMessage] chattingRoomId>> " + chattingRoomId);
        log.info("CHATTING-MESSAGE-CONTROLLER: [sendChattingMessage] content>> " + chattingMessageContentCreateRequest.getContent());
        String accountUsername = headerAccessor.getUser().getName();
        ResultResponse resultResponse = chattingMessageService.createChattingMessage(chattingMessageContentCreateRequest, chattingRoomId, accountUsername);
        log.info("CHATTING-MESSAGE-CONTROLLER: [sendChattingMessage] resultResponse>> " + resultResponse);
        template.convertAndSend("/sub/channel/" + chattingRoomId, resultResponse);
        log.info("CHATTING-MESSAGE-CONTROLLER: [sendChattingMessage] END");
        log.info("=======================");
    }

    // 채팅방 파일 보내기
    @PostMapping("/files")
    public ResponseEntity<ResultResponse> createChattingFile(@PathVariable Long chattingRoomId, @ModelAttribute ChattingFileCreateRequest chattingRoomCreateRequest) {
        Long accountId = securityUtil.getLoginAccountId();
        ResultResponse resultResponse = chattingMessageService.createChattingFile(chattingRoomCreateRequest, chattingRoomId, accountId);
        template.convertAndSend("/sub/channel/" + chattingRoomId, resultResponse);
        return ResponseEntity.ok(resultResponse);
    }

    // 채팅방 메시지 리스트 가져오기
    @GetMapping
    public ResponseEntity<ResultResponse> getChattingMessageListByRoom(@PathVariable Long chattingRoomId,
                                                                        @RequestParam(name = "page", defaultValue = "0") int page,
                                                                        @RequestParam(name = "size", defaultValue = "10") int size,
                                                                        @RequestParam(name = "totalRecord", defaultValue = "") Integer totalRecord) {
        Long accountId = securityUtil.getLoginAccountId();
        ResultResponse resultResponse = chattingMessageService.getChattingMessageListByRoom(chattingRoomId, page, size, totalRecord, accountId);
        return ResponseEntity.ok(resultResponse);
    }

    // 채팅방 파일 리스트 가져오기
    @GetMapping("/files")
    public ResponseEntity<ResultResponse> getChattingFileListByRoom(@PathVariable Long chattingRoomId,
                                                                     @RequestParam(name = "page", defaultValue = "0") int page,
                                                                     @RequestParam(name = "size", defaultValue = "10") int size,
                                                                     @RequestParam(name = "totalRecord", defaultValue = "") Integer totalRecord) {
        ResultResponse resultResponse = chattingMessageService.getChattingFileListByRoom(chattingRoomId, page, size, totalRecord);
        return ResponseEntity.ok(resultResponse);
    }

    // 메시지 수정
    @PatchMapping("/{chattingMessageId}")
    public ResponseEntity<ResultResponse> updateChattingMessage(@PathVariable Long chattingRoomId, @PathVariable Long chattingMessageId,
                                                                @RequestBody ChattingMessageContentCreateRequest chattingMessageContentCreateRequest){
        Long accountId = securityUtil.getLoginAccountId();
        ResultResponse resultResponse = chattingMessageService.updateChattingMessage(accountId, chattingRoomId, chattingMessageId, chattingMessageContentCreateRequest);
        return ResponseEntity.ok(resultResponse);
    }

    // 메시지 삭제
    @DeleteMapping("/{chattingMessageId}")
    public ResponseEntity<ResultResponse> deleteChattingMessage(@PathVariable Long chattingRoomId, @PathVariable Long chattingMessageId) {
        Long accountId = securityUtil.getLoginAccountId();
        ResultResponse resultResponse = chattingMessageService.deleteChattingMessage(accountId, chattingRoomId, chattingMessageId);
        return ResponseEntity.ok(resultResponse);
    }

    // 채팅 파일 다운로드
    @GetMapping("/{chattingMessageId}/files")
    public ResponseEntity<byte[]> downloadChattingFile(@PathVariable Long chattingRoomId, @PathVariable Long chattingMessageId) {
        Long accountId = securityUtil.getLoginAccountId();
        return chattingMessageService.downloadChattingFile(accountId, chattingRoomId, chattingMessageId);
    }
}
