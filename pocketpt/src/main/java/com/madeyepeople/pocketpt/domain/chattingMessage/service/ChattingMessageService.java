package com.madeyepeople.pocketpt.domain.chattingMessage.service;

import com.madeyepeople.pocketpt.domain.chattingMessage.dto.request.ChattingFileCreateRequest;
import com.madeyepeople.pocketpt.domain.chattingMessage.dto.request.ChattingMessageContentCreateRequest;
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
import org.springframework.stereotype.Service;

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
    public ResultResponse createChattingMessage(ChattingMessageContentCreateRequest chattingMessageContentCreateRequest, Long roomId, String accountUsername) {
        // [1] 채팅방 유효성 검사
        Optional<ChattingRoom> foundChattingRoom = chattingRoomRepository.findByChattingRoomId(roomId);

        // [2] 채팅 sender 유효성 검사
        Optional<ChattingParticipant> foundChattingParticipant = chattingParticipantRepository.findByAccountUsernameAndChattingRoomIdAndIsDeletedFalse(accountUsername, roomId);

        // [3] ChattingMessage 초기화
        ChattingMessage chattingMessage = toChattingMessageEntity.toChattingMessageCreateEntity(foundChattingParticipant.get(), chattingMessageContentCreateRequest);

        // [4] 채팅방에 참여 중인 사람이 현재 채팅방 참여 중인지 체크 - 채팅 읽음 처리 로직
        List<ChattingParticipant> chattingParticipantList = chattingParticipantRepository.findAllByChattingRoomIdAndNotInAccountIdAndIsDeletedFalse(roomId, foundChattingParticipant.get().getAccountId());
        int notViewCount = foundChattingRoom.get().getNumberOfParticipant() - 1; // 본인을 제외한 나머지 수로 초기 세팅
        for(ChattingParticipant chattingParticipant: chattingParticipantList) {
            log.info("-----------");
            log.info(chattingParticipant.getChattingParticipantId().toString());
            log.info(chattingParticipant.getSimpSessionId());
            if (chattingParticipant.getSimpSessionId() != null) {
                // 채팅 메시지를 읽지 않은 사람의 수
                --notViewCount;
            } else if (chattingParticipant.getSimpSessionId() == null) {
                // 채팅 참여자가 읽지 않은 메시지 수
                int participantNotViewCount = chattingParticipant.getNotViewCount();
                chattingParticipant.setNotViewCount(++participantNotViewCount);
                chattingParticipantRepository.save(chattingParticipant);
            }
            log.info("-----------");
        }
        chattingMessage.setNotViewCount(notViewCount);

        // [5] 채팅 메시지 저장 및 정보 담기
        ChattingMessage savedChattingMessage = chattingMessageRepository.save(chattingMessage);
        ChattingMessageCreateResponse chattingMessageCreateResponse = toChattingMessageResponse.toChattingMessageCreateResponse(foundChattingRoom.get().getChattingRoomId(),
                foundChattingParticipant.get().getChattingParticipantId(), savedChattingMessage);

        // [5] 채팅방 id, 채팅 sender id, 채팅 메시지 정보가 담긴 chattingMessageCreateResponse
        ResultResponse resultResponse = new ResultResponse(ResultCode.CHATTING_MESSAGE_CREATE_SUCCESS, chattingMessageCreateResponse);

        return resultResponse;
    }

    @Transactional
    public ResultResponse createChattingFile(ChattingFileCreateRequest chattingFileCreateRequest, Long roomId, Long accountId) {
        // [1] 채팅방 유효성 검사
        Optional<ChattingRoom> foundChattingRoom = chattingRoomRepository.findByChattingRoomId(roomId);

        // [2] 채팅 sender 유효성 검사
        Optional<ChattingParticipant> foundChattingParticipant = chattingParticipantRepository.findByAccountIdAndChattingRoomIdAndIsDeletedFalse(accountId, roomId);

        // [3] 채팅 파일 S3 업로드
        String fileUrl = s3FileService.uploadFile("chatting/" + roomId + "/", chattingFileCreateRequest.getFile());

        // [4] ChattingMessage 초기화
        ChattingMessage chattingMessage = toChattingMessageEntity.toChattingFileCreateEntity(foundChattingParticipant.get(), fileUrl);

        // [5] 채팅방에 참여 중인 사람이 현재 채팅방 참여 중인지 체크 - 채팅 읽음 처리 로직
        List<ChattingParticipant> chattingParticipantList = chattingParticipantRepository.findAllByChattingRoomIdAndNotInAccountIdAndIsDeletedFalse(roomId, accountId);
        int notViewCount = foundChattingRoom.get().getNumberOfParticipant() - 1; // 본인을 제외한 나머지 수로 초기 세팅
        for(ChattingParticipant chattingParticipant: chattingParticipantList) {
            log.info("-----------");
            log.info(chattingParticipant.getChattingParticipantId().toString());
            log.info(chattingParticipant.getSimpSessionId());
            if (chattingParticipant.getSimpSessionId() != null) {
                // 채팅 메시지를 읽지 않은 사람의 수
                --notViewCount;
            } else if (chattingParticipant.getSimpSessionId() == null) {
                // 채팅 참여자가 읽지 않은 메시지 수
                int participantNotViewCount = chattingParticipant.getNotViewCount();
                chattingParticipant.setNotViewCount(++participantNotViewCount);
                chattingParticipantRepository.save(chattingParticipant);
            }
            log.info("-----------");
        }
        chattingMessage.setNotViewCount(notViewCount);

        // [6] 채팅 메시지 저장 및 정보 담기
        ChattingMessage savedChattingMessage = chattingMessageRepository.save(chattingMessage);
        ChattingMessageCreateResponse chattingMessageCreateResponse = toChattingMessageResponse.toChattingFileCreateResponse(foundChattingRoom.get().getChattingRoomId(),
                foundChattingParticipant.get().getChattingParticipantId(), savedChattingMessage);

        // [7] 채팅방 id, 채팅 sender id, 채팅 메시지 정보가 담긴 chattingMessageCreateResponse
        ResultResponse resultResponse = new ResultResponse(ResultCode.CHATTING_FILE_CREATE_SUCCESS, chattingMessageCreateResponse);

        return resultResponse;
    }

}
