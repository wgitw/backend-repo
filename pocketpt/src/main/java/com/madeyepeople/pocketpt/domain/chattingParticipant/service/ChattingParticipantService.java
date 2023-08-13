package com.madeyepeople.pocketpt.domain.chattingParticipant.service;

import com.madeyepeople.pocketpt.domain.account.entity.Account;
import com.madeyepeople.pocketpt.domain.chattingParticipant.entity.ChattingParticipant;
import com.madeyepeople.pocketpt.domain.chattingParticipant.repository.ChattingParticipantRepository;
import com.madeyepeople.pocketpt.domain.chattingRoom.entity.ChattingRoom;
import com.madeyepeople.pocketpt.domain.chattingRoom.repository.ChattingRoomRepository;
import com.madeyepeople.pocketpt.global.result.ResultCode;
import com.madeyepeople.pocketpt.global.result.ResultResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChattingParticipantService {
    private final ChattingRoomRepository chattingRoomRepository;
    private final ChattingParticipantRepository chattingParticipantRepository;

    // chattingRoom 나가기
    @Transactional
    public ResultResponse exitChattingRoom(Account account, Long chattingRoomId) {
        // [1] 채팅방 유효성 검사
        ChattingRoom foundChattingRoom = chattingRoomRepository.findByChattingRoomIdAndIsDeletedFalse(chattingRoomId).orElseThrow();

        // [2] account 유효성 검사
        ChattingParticipant foundChattingParticipant = chattingParticipantRepository.findByAccountAndChattingRoomAndIsDeletedFalse(account, foundChattingRoom).orElseThrow();

        // [3] 채팅방 나가기
        foundChattingParticipant.setIsDeleted(true);
        chattingParticipantRepository.save(foundChattingParticipant);

        // [4] resultResponse 만들기
        ResultResponse resultResponse = new ResultResponse(ResultCode.CHATTING_ROOM_EXIT_SUCCESS, "채팅방 나가기 성공");

        return resultResponse;
    }
}
