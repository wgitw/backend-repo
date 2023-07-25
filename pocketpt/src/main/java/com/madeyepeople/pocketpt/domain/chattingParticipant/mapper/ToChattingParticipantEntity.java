package com.madeyepeople.pocketpt.domain.chattingParticipant.mapper;

import com.madeyepeople.pocketpt.domain.chattingParticipant.entity.ChattingParticipant;
import com.madeyepeople.pocketpt.domain.chattingRoom.entity.ChattingRoom;
import org.springframework.stereotype.Component;


@Component
public class ToChattingParticipantEntity {

    public ChattingParticipant toChattingHostCreateEntity(ChattingRoom chattingRoom, Long accountId) {
        return ChattingParticipant.builder()
                .chattingRoom(chattingRoom)
                .accountId(accountId)
                .isHost(Boolean.TRUE)
//                .chattingRoomEntryTime(LocalDateTime.now()) // TODO: 추후 제거
                .build();
    }

    public ChattingParticipant toChattingParticipantCreateEntity(ChattingRoom chattingRoom, Long accountId) {
        return ChattingParticipant.builder()
                .chattingRoom(chattingRoom)
                .accountId(accountId)
                .isHost(Boolean.FALSE)
                .build();
    }
}
