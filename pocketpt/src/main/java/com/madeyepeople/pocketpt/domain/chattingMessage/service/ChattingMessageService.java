package com.madeyepeople.pocketpt.domain.chattingMessage.service;

import com.madeyepeople.pocketpt.domain.chattingMessage.dto.request.ChattingFileCreateRequest;
import com.madeyepeople.pocketpt.domain.chattingMessage.dto.request.ChattingMessageCreateRequest;
import com.madeyepeople.pocketpt.domain.chattingMessage.dto.response.ChattingMessageCreateResponse;
import com.madeyepeople.pocketpt.domain.chattingMessage.entity.ChattingMessage;
import com.madeyepeople.pocketpt.domain.chattingMessage.mapper.ToChattingMessageEntity;
import com.madeyepeople.pocketpt.domain.chattingMessage.mapper.ToChattingMessageResponse;
import com.madeyepeople.pocketpt.domain.chattingMessage.repository.ChattingMessageRepository;
import com.madeyepeople.pocketpt.domain.chattingParticipant.entity.ChattingParticipant;
import com.madeyepeople.pocketpt.domain.chattingParticipant.repository.ChattingParticipantRepository;
import com.madeyepeople.pocketpt.domain.chattingRoom.entity.ChattingRoom;
import com.madeyepeople.pocketpt.domain.chattingRoom.repository.ChattingRoomRepository;
import com.madeyepeople.pocketpt.global.result.ResultCode;
import com.madeyepeople.pocketpt.global.result.ResultResponse;
import com.madeyepeople.pocketpt.global.s3.S3FileService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChattingMessageService {
    private final ChattingMessageRepository chattingMessageRepository;

    private final ChattingRoomRepository chattingRoomRepository;

    private final ChattingParticipantRepository chattingParticipantRepository;

    private final ToChattingMessageEntity toChattingMessageEntity;

    private final ToChattingMessageResponse toChattingMessageResponse;

    private final S3FileService s3FileService;

    @Transactional
    public Map<String, Object> createChattingMessage(ChattingMessageCreateRequest chattingMessageCreateRequest) {
        // [1] 채팅방 유효성 검사
        Optional<ChattingRoom> foundChattingRoom = chattingRoomRepository.findByChattingRoomId(chattingMessageCreateRequest.getChattingRoomId());

        // [2] 채팅 sender 유효성 검사
        Optional<ChattingParticipant> chattingParticipant = chattingParticipantRepository.findByChattingParticipantId(chattingMessageCreateRequest.getChattingParticipantId());

        // [3] 채팅 메시지 저장 및 정보 담기
        ChattingMessage chattingMessage = toChattingMessageEntity.toChattingMessageCreateEntity(chattingParticipant.get(), chattingMessageCreateRequest);
        ChattingMessage savedChattingMessage = chattingMessageRepository.save(chattingMessage);
        ChattingMessageCreateResponse chattingMessageCreateResponse = toChattingMessageResponse.toChattingMessageCreateResponse(foundChattingRoom.get().getChattingRoomId(),
                chattingParticipant.get().getChattingParticipantId(), savedChattingMessage);

        // [4] 채팅방 id, 채팅 sender id, 채팅 메시지 정보가 담긴 chattingMessageCreateResponse
        ResultResponse resultResponse = new ResultResponse(ResultCode.CHATTING_MESSAGE_CREATE_SUCCESS, chattingMessageCreateResponse);

        Map<String, Object> map = new HashMap<>();
        map.put("chattingRoomId", chattingMessageCreateResponse.getChattingRoomId());
        map.put("resultResponse", resultResponse);

        return map;
    }

    @Transactional
    public ResultResponse createChattingFile(ChattingFileCreateRequest chattingFileCreateRequest) {
        // [1] 채팅방 유효성 검사
        Optional<ChattingRoom> foundChattingRoom = chattingRoomRepository.findByChattingRoomId(chattingFileCreateRequest.getChattingRoomId());

        // [2] 채팅 sender 유효성 검사
        Optional<ChattingParticipant> foundChattingParticipant = chattingParticipantRepository.findByChattingParticipantId(chattingFileCreateRequest.getChattingParticipantId());

        // [3] 채팅 파일 S3 업로드
        String fileUrl = s3FileService.uploadFile("chatting/" + chattingFileCreateRequest.getChattingRoomId() + "/", chattingFileCreateRequest.getFile());

        // [4] 채팅 파일 저장 및 정보 담기
        ChattingMessage chattingMessage = toChattingMessageEntity.toChattingFileCreateEntity(foundChattingParticipant.get(), fileUrl);
        ChattingMessage savedChattingMessage = chattingMessageRepository.save(chattingMessage);
        ChattingMessageCreateResponse chattingMessageCreateResponse = toChattingMessageResponse.toChattingFileCreateResponse(foundChattingRoom.get().getChattingRoomId(),
                foundChattingParticipant.get().getChattingParticipantId(), savedChattingMessage);

        // [5] 채팅방 id, 채팅 sender id, 채팅 메시지 정보가 담긴 chattingMessageCreateResponse
        ResultResponse resultResponse = new ResultResponse(ResultCode.CHATTING_FILE_CREATE_SUCCESS, chattingMessageCreateResponse);

        return resultResponse;
    }

    public ResultResponse exitChattingRoom(SessionDisconnectEvent event) {
        log.info("DisConnEvent {}", event);

        // [1] headerAccessor 객체 생성
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        // [2] stomp 세션에 있던 uuid 와 roomId 를 확인 및 유효성 검증
        String userUUID = (String) headerAccessor.getSessionAttributes().get("userUUID");
        String roomId = (String) headerAccessor.getSessionAttributes().get("roomId");

        // [3] user와 roomId를 통해 유저가 채팅방 나간 시간 기록


        return null;
    }

}
