package com.madeyepeople.pocketpt.domain.chattingParticipant.mapper;

import com.madeyepeople.pocketpt.domain.chattingParticipant.dto.request.ChattingParticipantCreateRequest;
import com.madeyepeople.pocketpt.domain.chattingParticipant.entity.ChattingParticipant;
import com.madeyepeople.pocketpt.domain.chattingRoom.entity.ChattingRoom;
import org.springframework.stereotype.Component;

@Component
public class ToChattingParticipantEntity {

    public ChattingParticipant toChattingParticipantCreateEntity(ChattingRoom chattingRoom, ChattingParticipantCreateRequest chattingParticipantCreateRequest) {
        return ChattingParticipant.builder()
                .chattingRoom(chattingRoom)
                .participantId(chattingParticipantCreateRequest.getParticipantId())
                .isHost(chattingParticipantCreateRequest.getIsHost())
                .build();
    }
}
