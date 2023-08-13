package com.madeyepeople.pocketpt.domain.chattingParticipant.service;

import com.madeyepeople.pocketpt.domain.account.entity.Account;
import com.madeyepeople.pocketpt.domain.chattingParticipant.entity.ChattingParticipant;
import com.madeyepeople.pocketpt.domain.chattingParticipant.repository.ChattingParticipantRepository;
import com.madeyepeople.pocketpt.domain.chattingRoom.entity.ChattingRoom;
import com.madeyepeople.pocketpt.domain.chattingRoom.repository.ChattingRoomRepository;
import com.madeyepeople.pocketpt.global.error.ErrorCode;
import com.madeyepeople.pocketpt.global.error.exception.BusinessException;
import com.madeyepeople.pocketpt.global.error.exception.CustomExceptionMessage;
import com.madeyepeople.pocketpt.global.result.ResultCode;
import com.madeyepeople.pocketpt.global.result.ResultResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChattingParticipantService {
    private final ChattingRoomRepository chattingRoomRepository;
    private final ChattingParticipantRepository chattingParticipantRepository;

    // chattingRoom 나가기
    @Transactional
    public ResultResponse exitChattingRoom(Account account, Long chattingRoomId) {
        log.info("=======================");
        log.info("CHATTING-PARTICIPANT-SERVICE: [exitChattingRoom] START");

        // [1] 채팅방 유효성 검사
        ChattingRoom foundChattingRoom = chattingRoomRepository.findByChattingRoomIdAndIsDeletedFalse(chattingRoomId).orElseThrow(
                () -> new BusinessException(ErrorCode.CHATTING_ROOM_NOT_FOUND, CustomExceptionMessage.CHATTING_ROOM_NOT_FOUND.getMessage())
        );

        // [2] account 유효성 검사
        ChattingParticipant foundChattingParticipant = chattingParticipantRepository.findByAccountAndChattingRoomAndIsDeletedFalse(account, foundChattingRoom).orElseThrow(
                () -> new BusinessException(ErrorCode.CHATTING_PARTICIPANT_NOT_FOUND, CustomExceptionMessage.CHATTING_PARTICIPANT_NOT_FOUND.getMessage())
        );

        // [3] 채팅방 나가기
        foundChattingParticipant.setIsDeleted(true);
        chattingParticipantRepository.save(foundChattingParticipant);

        // [4] resultResponse 만들기
        ResultResponse resultResponse = new ResultResponse(ResultCode.CHATTING_ROOM_EXIT_SUCCESS, "채팅방 나가기 성공");

        log.info("CHATTING-PARTICIPANT-SERVICE: [exitChattingRoom] END");
        log.info("=======================\n\n");

        return resultResponse;
    }
}
